package domain.api

import config.Config.simpleColumnsDelimiter
import config.Config.noFilter
import config.Config.path
import domain.model.Table
import domain.cache.DBIndex.addIndex
import domain.cache.DBIndex.dropIndex
import domain.cache.DBIndex.getIndex
import domain.cache.DBIndex.getTableIndexes
import domain.cache.DBIndex.isPresented
import domain.cache.IndexInfo
import domain.layout.DBLayout
import domain.layout.DBReader
import domain.layout.DBWriter
import domain.layout.PackedLayout
import domain.ql.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.io.File
import java.util.TreeMap

suspend fun select(qlData: QLData, qlFilter: QLFilter, channel: ByteWriteChannel, reader: DBReader) {
    val sb = StringBuilder()
    if (qlData is QLJoinData) {
        val table = reader.readTable((path + qlData.data.name))
        var columns = table.columns
        sb.append(columns.joinToString(separator = simpleColumnsDelimiter))
        val table2 = reader.readTable((path + qlData.data2.name))
        columns = qlData.columns
        if (!table.columns.containsAll(columns) || !table2.columns.containsAll(columns))
            throw Error("JOIN must have keys presented in both tables")
        sb.append(table2.columns.filter { it -> !columns.contains(it) }.joinToString(separator = simpleColumnsDelimiter, prefix = simpleColumnsDelimiter, postfix = "\n"))
        hashJoin(table, table2, qlData.columns, sb)
    }
    else {
        if (qlFilter.column == noFilter) {
            val a = qlFilter.range.a.toInt()
            val b = qlFilter.range.b.toInt()
            val table = reader.readTable((path + qlData.data.name))
            val columns = table.columns
            val data = table.data
            sb.append(columns.joinToString(separator = simpleColumnsDelimiter))
            sb.append("\n")
            data.drop(a)
                .take(b - a)
                .forEach {
                    sb.append(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
                }
        } else {
            val a = qlFilter.range.a + 1
            val b = qlFilter.range.b
            if (isPresented(IndexInfo(qlData.data.name, qlFilter.column))) {
                sb.append("Indexed\n")
                sb.append(reader.readColumns((path+qlData.data.name)).joinToString(separator = simpleColumnsDelimiter))
                sb.append("\n")
                val idx = getIndex(IndexInfo(qlData.data.name, qlFilter.column))!!
                idx.entries.filter { it.key in a..b }.flatMap {  it.value }.forEach {
                    sb.append(reader.readRow((path+qlData.data.name), it+1).joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
                }
            } else {
                val table = reader.readTable((path + qlData.data.name))
                val columns = table.columns
                val data = table.data
                sb.append(columns.joinToString(separator = simpleColumnsDelimiter))
                sb.append("\n")

                val pos = columns.indexOf(qlFilter.column)
                data.filter { it[pos] in a..b }.forEach {
                    sb.append(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
                }
            }
        }
    }
    channel.writeStringUtf8( sb.toString() )
}

private fun hashJoin(table: Table, table2: Table, columns: List<String>, stringBuilder: StringBuilder) {
    val map = HashMap<List<Double>, List<Double>>()
    val tableIndexesOfKeys = ArrayList<Int>()
    for (key in columns) {
        tableIndexesOfKeys.add(table.columns.indexOf(key))
    }
    for (row: List<Double> in table.data) {
        val curr: ArrayList<Double> = ArrayList()
        for (i in tableIndexesOfKeys) {
            curr.add(row[i])
        }
        map[curr] = row
    }

    val table2IndexesOfKeys = ArrayList<Int>()
    for (key in columns) {
        table2IndexesOfKeys.add(table2.columns.indexOf(key))
    }
    for (row: List<Double> in table2.data) {
        val curr: ArrayList<Double> = ArrayList()
        for (i in tableIndexesOfKeys) {
            curr.add(row[i])
        }
        if (map[curr].orEmpty().isNotEmpty()) {
            val rowUniq = ArrayList<Double>()
            for (i in row.indices) {
                if (!table2IndexesOfKeys.contains(i)) rowUniq.add(row[i])
            }
            stringBuilder.append(map[curr]!!.joinToString(separator = simpleColumnsDelimiter, postfix = simpleColumnsDelimiter)
                    + rowUniq.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
        }
    }
}

suspend fun create(table: Table, writer: DBWriter) {
    writer.writeTable((path + table.name), table)
}

suspend fun insert(table: Table, writer: DBWriter) {
    writer.writeTableContinuous((path + table.name), table)
    getTableIndexes(table.name).forEach { dropIndex(it) }
}

fun drop(table: Table) {
    // TODO: add check
    File(path+table.name).delete()
    // TODO: add logs
    getTableIndexes(table.name).forEach { dropIndex(it) }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun index(tableName: String, column: String, reader: DBReader) = withContext(newSingleThreadContext("index")) {
    val table = reader.readTable((path + tableName))
    val indexData = TreeMap<Double, ArrayList<Int>>()
    val pos = table.columns.indexOf(column)
    var i = 0
    for (row: List<Double> in table.data) {
        if (indexData[row[pos]] == null) indexData[row[pos]] = ArrayList()
        indexData[row[pos]]?.add(i++)
    }
    addIndex(IndexInfo(tableName, column), indexData)
}

suspend fun execute(tkn: QLTerminate, channel: ByteWriteChannel, layout: DBLayout = PackedLayout()) {
    when (tkn) {
        is QLSelect -> {
            select(tkn.qlData, tkn.qlFilter, channel, layout.getReader())
        }
        is QLCreate -> {
            create(tkn.qlData.data, layout.getWriter())
        }
        is QLInsert -> {
            insert(tkn.qlData.data, layout.getWriter())
        }
        is QLDrop -> {
            drop(tkn.qlData.data)
        }
        is QLIndex -> {
            index(tkn.qlData.data.name, tkn.column, layout.getReader())
        }
    }
}