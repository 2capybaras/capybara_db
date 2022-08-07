package domain.ql

import java.text.ParseException

class ExpressionParser(str: String) {
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
            "SELECT" -> {
                QLSelect(parseTableName(), parseRange())
            }
            else -> throw ParseException("Bad terminate token", idx)
        }
    }

    private fun parseRange(): QLRange? {
        if (idx == tokens.size) return null
        return if (idx+2 < tokens.size && tokens[idx] == "FROM" && tokens[idx+2] == "TO") {
            idx++
            QLRange(tokens[idx++].toInt() - 1, tokens[++idx].toInt())
        } else if (tokens[idx] == "INDEX" && idx+1 < tokens.size) {
            QLRange(tokens[++idx].toInt() - 1, tokens[idx].toInt())
        } else throw ParseException("Bad range token", idx)
    }

    private fun parseTableName(): QLTableName {
        if (tokens[idx++] != from) throw ParseException("No from keyword", idx)
        return QLTableName(parseString("table name"))
    }

    private fun parseString(property: String): String {
        if (idx == tokens.size) throw ParseException("No string token for $property", idx)
        return tokens[idx++]
    }
}