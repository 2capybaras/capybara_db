package db

import db.DBConfiguration.delimiter
import java.io.File

object DBReader {
    fun readTable(file: File): DBTable {
        val lines = file.readLines()
        val columns = lines[0].split(delimiter)
        val data = ArrayList<List<Double>>()
        lines.drop(1).filter { it.isNotEmpty() }.forEach {
            val values = it.split(delimiter).map { s: String -> s.toDouble() }
            data.add(values)
        }
        return DBTable(columns = columns, data = data)
    }
}