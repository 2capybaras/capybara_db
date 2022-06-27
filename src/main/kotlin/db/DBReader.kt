package db

import db.DBConfiguration.delimiter
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

interface DBReader {
    fun readTable(fileName: String): DBTable
    fun readRow(fileName: String, row: Int): List<Double>
    fun readColumns(fileName: String): List<String>
}

/**
 * Simple Layout:
 *
 * table.txt
 * column1, column2, column3
 * 1, 2.3, 3
 * 3.4, 0, 5
 */
object SimpleLayoutReader: DBReader {
    override fun readTable(fileName: String): DBTable {
        val file = File(fileName)
        val lines = file.readLines()
        val columns = lines[0].split(delimiter)
        val data = ArrayList<List<Double>>()
        lines.drop(1).filter { it.isNotEmpty() }.forEach {
            val values = it.split(delimiter).map { s: String -> s.toDouble() }
            data.add(values)
        }
        return DBTable(columns = columns, data = data)
    }

    override fun readRow(fileName: String, row: Int): List<Double> {
        val file = File(fileName)
        var i = 0
        val sc = Scanner(file)
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            if (line.isNotBlank()) {
                if (i == row) {
                    return line.split(delimiter).map { it -> it.toDouble() }
                }
                i++
            }
        }
        return emptyList()
    }

    override fun readColumns(fileName: String): List<String> {
        val file = File(fileName)
        val sc = Scanner(file)
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            if (line.isNotBlank()) {
                return line.split(delimiter)
            }
        }
        return emptyList()
    }
}