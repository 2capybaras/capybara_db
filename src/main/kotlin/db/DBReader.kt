package db

import db.DBConfiguration.delimiter
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

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

    fun readRow(file: File, row: Int): List<Double> {
        try {
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    fun readColumns(file: File): List<String> {
        try {
            val sc = Scanner(file)
            while (sc.hasNextLine()) {
                val line = sc.nextLine()
                if (line.isNotBlank()) {
                    return line.split(delimiter)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList()
    }
}