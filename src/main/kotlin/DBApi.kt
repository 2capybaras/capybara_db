fun getStringRowByIndex(table: Table, idx: Int): String {
    val rowData = table.data[idx-1]
    val builder = StringBuilder()
    for (i in 0 until table.columns.size) {
        builder.append(table.columns[i])
        builder.append(": ${rowData[i]}\t")
    }
    return builder.toString()
}