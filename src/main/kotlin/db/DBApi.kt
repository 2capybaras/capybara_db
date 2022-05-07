package db

import db.DBConfiguration.delimiter
import db.DBConfiguration.noFilter
import db.DBConfiguration.path
import db.DBIndex.addIndex
import db.DBIndex.dropIndex
import db.DBIndex.getIndex
import db.DBIndex.getTableIndexes
import db.DBIndex.isPresented
import db.DBReader.readColumns
import db.DBReader.readRow
import db.DBReader.readTable
import db.DBWriter.writeTable
import db.DBWriter.writeTableContinuous
import ql.*
import java.io.File
import java.util.TreeMap

fun select(qlData: QLData, qlFilter: QLFilter) {
    val sb = StringBuilder()
    if (qlData is QLJoinData) {
        val table = readTable(File(path + qlData.data.name))
        var columns = table.columns
        sb.append(columns.joinToString(separator = delimiter))
        val table2 = readTable(File(path + qlData.data2.name))
        columns = qlData.columns
        if (!table.columns.containsAll(columns) || !table2.columns.containsAll(columns))
            throw Error("JOIN must have keys presented in both tables")
        sb.append(table2.columns.filter { it -> !columns.contains(it) }.joinToString(separator = delimiter, prefix = delimiter, postfix = "\n"))
        hashJoin(table, table2, qlData.columns, sb)
    }
    else {
        if (qlFilter.column == noFilter) {
            val a = qlFilter.range.a.toInt()
            val b = qlFilter.range.b.toInt()
            val table = readTable(File(path + qlData.data.name))
            val columns = table.columns
            val data = table.data
            sb.append(columns.joinToString(separator = delimiter))
            sb.append("\n")
            data.drop(a)
                .take(b - a)
                .forEach {
                    sb.append(it.joinToString(separator = delimiter, postfix = "\n"))
                }
        } else {
            val a = qlFilter.range.a + 1
            val b = qlFilter.range.b
            if (isPresented(IndexInfo(qlData.data.name, qlFilter.column))) {
                sb.append("Indexed\n")
                sb.append(readColumns(File(path+qlData.data.name)).joinToString(separator = delimiter))
                sb.append("\n")
                val idx = getIndex(IndexInfo(qlData.data.name, qlFilter.column))
                idx.entries.filter { it.key in a..b }.flatMap {  it.value }.forEach {
                    sb.append(readRow(File(path+qlData.data.name), it+1).joinToString(separator = delimiter, postfix = "\n"))
                }

            } else {
                val table = readTable(File(path + qlData.data.name))
                val columns = table.columns
                val data = table.data
                sb.append(columns.joinToString(separator = delimiter))
                sb.append("\n")

                val pos = columns.indexOf(qlFilter.column)
                data.filter { it[pos] in a..b }.forEach {
                    sb.append(it.joinToString(separator = delimiter, postfix = "\n"))
                }
            }
        }
    }
    println(sb.toString())
}

private fun hashJoin(table: DBTable, table2: DBTable, columns: List<String>, stringBuilder: StringBuilder) {
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
            stringBuilder.append(map[curr]!!.joinToString(separator = delimiter, postfix = delimiter)
                    + rowUniq.joinToString(separator = delimiter, postfix = "\n"))
        }
    }
}

fun create(table: DBTable) {
    writeTable(File(path + table.name), table)
}

fun insert(table: DBTable) {
    writeTableContinuous(File(path + table.name), table)
    getTableIndexes(table.name).forEach { dropIndex(it) }
}

fun drop(table: DBTable) {
    // TODO: add check
    File(path+table.name).delete()
    // TODO: add logs
    getTableIndexes(table.name).forEach { dropIndex(it) }
}

fun index(tableName: String, column: String) {
    val table = readTable(File(path + tableName))
    val indexData = TreeMap<Double, ArrayList<Int>>()
    val pos = table.columns.indexOf(column)
    var i = 0
    for (row: List<Double> in table.data) {
        if (indexData[row[pos]] == null) indexData[row[pos]] = ArrayList()
        indexData[row[pos]]?.add(i++)
    }
    addIndex(IndexInfo(tableName, column), indexData)
}

fun execute(tkn: QLTerminate) {
    when (tkn) {
        is QLSelect -> {
            select(tkn.qlData, tkn.qlFilter)
        }
        is QLCreate -> {
            create(tkn.qlData.data)
        }
        is QLInsert -> {
            insert(tkn.qlData.data)
        }
        is QLDrop -> {
            drop(tkn.qlData.data)
        }
        is QLIndex -> {
            index(tkn.qlData.data.name, tkn.column)
        }
        else -> {}
    }
}