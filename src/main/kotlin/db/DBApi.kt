import db.DBConfiguration.delimiter
import db.DBConfiguration.noFilter
import db.DBConfiguration.path
import db.DBReader.readTable
import db.DBTable
import db.DBWriter.writeTable
import db.DBWriter.writeTableContinuous
import ql.*
import java.io.File

fun getStringRowByIndex(DBTable: DBTable, idx: Int): String {
    val rowData = DBTable.data[idx-1]
    val builder = StringBuilder()
    for (i in 0 until DBTable.columns.size) {
        builder.append(DBTable.columns[i])
        builder.append(": ${rowData[i]}\t")
    }
    return builder.toString()
}

fun select(qlData: QLData, qlFilter: QLFilter) {
    val table = readTable(File(path + qlData.data.name))
    var columns = table.columns
    val data = table.data
    val sb = StringBuilder()
    sb.append(columns.joinToString(separator = delimiter))

    if (qlData is QLJoinData) {
        val table2 = readTable(File(path + qlData.data2.name))
        columns = qlData.columns
        if (!table.columns.containsAll(columns) || !table2.columns.containsAll(columns))
            throw Error("JOIN must have keys presented in both tables")
        sb.append(table2.columns.filter { it -> !columns.contains(it) }.joinToString(separator = delimiter, prefix = delimiter, postfix = "\n"))
        hashJoin(table, table2, qlData.columns, sb)
    }
    else {
        sb.append("\t")
        if (qlFilter.column == noFilter) {
            val a = qlFilter.range.a.toInt()
            val b = qlFilter.range.b.toInt()
            data.drop(a)
                .take(b - a)
                .forEach {
                    sb.append(it.joinToString(separator = delimiter, postfix = "\n"))
                }
        } else {
            val a = qlFilter.range.a
            val b = qlFilter.range.b
            val pos = columns.indexOf(qlFilter.column)
            data.filter { it[pos] in a..b }.forEach {
                sb.append(it.joinToString(separator = delimiter, postfix = "\n"))
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
            stringBuilder.append(map[curr]!!.joinToString(separator = delimiter, postfix = delimiter)
                    + row.joinToString(separator = delimiter, postfix = "\n"))
        }
    }
}

fun create(table: DBTable) {
    writeTable(File(path + table.name), table)
}

fun insert(table: DBTable) {
    writeTableContinuous(File(path + table.name), table)
}

fun drop(table: DBTable) {
    File(path+table.name).delete()
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
        else -> {}
    }
}