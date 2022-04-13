package ql

import utils.Utils.numberGenerator
import db.DBTable
import java.text.ParseException
import kotlin.io.path.Path

class QLExpressionParser(str: String) {
    private val from = "FROM"
    private val tokens = str.split(" ")
    private var idx = 0

    fun parseTerminate(): QLTerminate {
        val sb = StringBuffer()
        val oldIdx = idx
        if (tokens.isNotEmpty()) sb.append(tokens[idx++])
        if (idx == oldIdx) {
            throw ParseException("No terminate token", 0)
        }
        val str = sb.toString()
        return tokenFromString(str)
    }

    private fun tokenFromString(str: String): QLTerminate {
        return when (str) {
            "CREATE" -> QLCreate(parseTableWithoutFrom())
            "INSERT" -> QLInsert(mergeTableValues(parseTableWithoutFrom(), parseValues()))
            "SELECT" -> QLSelect(parseColumns(), parseJoin(), parseFilter())
            "DROP" -> QLDrop(parseTableWithoutFrom())
            else -> throw ParseException("Bad terminate token", idx)
        }
    }

    private fun parseFilter(): QLFilter {
        return if (idx < tokens.size && tokens[idx] == "WHERE") {
            idx++
            QLFilter(tokens[idx++], parseDoubleRange())
        } else QLFilter(range = parseDoubleRange())
    }

    private fun parseJoin(): QLData {
        val table = parseTable()
        return if (idx < tokens.size && tokens[idx] == "JOIN") {
            idx++
            val table2 = parseTableWithoutFrom()
            if (idx < tokens.size && tokens[idx++] == "USING") {
                val columns = parseColumns()
                QLJoinData(table.data, table2.data, columns)
            } else throw ParseException("No key for JOIN", idx)

        } else table

    }
    
    private fun parseRange(): QLRange {
        if (idx == tokens.size) return QLRange()
        if (tokens[idx] == "FROM" && tokens[idx+2] == "TO") {
            idx++
            return QLRange(tokens[idx++].toInt() - 1, tokens[++idx].toInt())
        } else if (tokens[idx] == "INDEX" && idx+1 < tokens.size) {
            return QLRange(tokens[++idx].toInt() - 1, tokens[idx].toInt())
        }
        else throw ParseException("Bad range token", idx)
    }
    private fun parseDoubleRange(): QLDoubleRange {
        if (idx >= tokens.size) return QLDoubleRange()
        return if (tokens[idx] == "FROM" && tokens[idx+2] == "TO") {
            idx++
            QLDoubleRange(tokens[idx++].toDouble()-1, tokens[++idx].toDouble())
        } else if (tokens[idx] == "INDEX" && idx+1 < tokens.size) {
            QLDoubleRange(tokens[++idx].toDouble()-1, tokens[idx].toDouble())
        } else throw ParseException("Bad range token", idx)
    }

    private fun mergeTableValues(parsedTable: QLData, parsedValues: QLData): QLData {
        val res = DBTable(parsedTable.data.name, parsedTable.data.columns, parsedValues.data.data)
        return QLData(res)
    }

    private fun parseValues(): QLData {
        if (tokens[idx++] == "RANDOM") return QLData(DBTable(data = randomData()))
        else throw ParseException("Unexpected values", idx)
    }

    private fun randomData(rows: Int = 1000, columns: Int = 3): List<List<Double>> {
        val data = ArrayList<ArrayList<Double>>()
        for (i in 0 until rows) {
            val row = ArrayList<Double>()
            for (j in 0 until columns) {
                row.add(numberGenerator())
            }
            data.add(row)
        }
        return data
    }

    private fun parseTable(): QLData {
        if (tokens[idx++] != from) throw ParseException("No from keyword", idx)
        return parseTableWithoutFrom()
    }

    private fun parseTableWithoutFrom(): QLData {
        var nameAndColumns = tokens[idx++]
        while (idx < tokens.size && (tokens[idx].endsWith(",") || tokens[idx].endsWith(")")) ) nameAndColumns += tokens[idx++]
        val name = if (nameAndColumns.last().isLetter()) {
            nameAndColumns
        } else {
            nameAndColumns.substringBefore('(')
        }

        val columns = nameAndColumns.substringAfter("$name(").substringBefore(')').split(",")
        return QLData(DBTable(name, columns))
    }

    private fun parseColumns(): List<String> {
        val columns = ArrayList<String>()
        if (idx >= tokens.size) return columns
        if (tokens[idx] == "*") {
            idx++
            return columns
        }
        columns += tokens[idx++].substringBefore(',')
        while (idx < tokens.size && tokens[idx].endsWith(",")) columns += tokens[idx++]
        if (tokens[idx-1].endsWith(",")) columns += tokens[idx++]
        return columns
    }
}