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
    val columns = table.columns
    val data = table.data
    val sb = StringBuilder()
    sb.append(columns.joinToString(separator = delimiter, postfix = "\n"))

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
    println(sb.toString())
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