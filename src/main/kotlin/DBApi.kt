import DBConfiguration.columnsEnd
import DBConfiguration.delimiter
import DBReader.readTable
import java.io.File

fun getStringRowByIdex(DBTable: DBTable, idx: Int): String {
    val rowData = DBTable.data[idx-1]
    val builder = StringBuilder()
    for (i in 0 until DBTable.columns.size) {
        builder.append(DBTable.columns[i])
        builder.append(": ${rowData[i]}\t")
    }
    return builder.toString()
}

fun select(qlData: QLData, qlRange: QLRange) {
    val table = readTable(File("tables/" + qlData.data.name))
    val columns = table.columns
    val data = table.data
    val sb = StringBuilder()
    sb.append(columns.joinToString(separator = delimiter, postfix = "\n"))
    data.forEach {
        sb.append(it.joinToString(separator = delimiter, postfix = "\n" ))
    }
    println(sb.toString())
}

fun execute(tkn: QLTerminate) {
    when (tkn) {
        is QLSelect -> {
            select(tkn.qlData, tkn.qlRange)
        }
        else -> {}
    }
}