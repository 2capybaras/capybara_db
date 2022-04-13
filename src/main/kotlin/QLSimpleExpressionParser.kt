import Utils.numberGenerator
import java.text.ParseException

class QLSimpleExpressionParser(private val str: String) {
    var idx = 0
    fun parseExpression(): QLToken {
        return parseTerm()
    }

    private fun parseTerm(): QLTerminate  {
        val sb = StringBuffer()
        val oldIdx = idx
        while (idx < str.length && str[idx] >= 'A' && str[idx] <= 'Z') {
            sb.append(str[idx])
            idx++
        }
        if (idx == oldIdx) {
            throw ParseException("No terminate token", 0)
        }
        val str = sb.toString()
        return tokenFromString(str)
    }

    private fun tokenFromString(str: String): QLTerminate {
        return when (str) {
            "CREATE" -> QLCreate(parseTable())
            "INSERT" -> QLInsert(mergeTableValues(parseTable(), parseValues()))
            "SELECT" -> QLSelect(parseRange(), parseTable())
            "DROP" -> QLDrop(parseTable())
            else -> throw ParseException("Bad terminate token", 0)
        }
    }

    private fun skipEmpty() {
        while (str[idx] == ' ') idx++
    }

    private fun parseTable(): QLData {
        skipEmpty()
        if (str.substring(idx).startsWith("FROM")) {
            idx += 4
        } else throw ParseException("No FROM keyword", idx)
        skipEmpty()
        val tableName = parseString()
        if (idx < str.length && str[idx] != '(') {
            val columnsEnd = str.substring(idx).indexOf(')')
            val columns = str.substring(idx, columnsEnd).split(" ")
            idx = columnsEnd
            return QLData(DBTable(tableName, columns = columns))
        }
        return QLData(DBTable(tableName))
    }

    private fun parseString(): String {
        val sb = StringBuffer()
        val oldIdx = idx
        while (idx < str.length && str[idx] >= 'A' && str[idx] <= 'z') {
            sb.append(str[idx])
            idx++
        }
        if (idx == oldIdx) {
            throw ParseException("No string token", idx)
        }
        return sb.toString()
    }

    private fun parseValues(): QLData {
        if (str.substring(idx).startsWith("RANDOM")) return QLData(DBTable(data = randomData() ))
        throw ParseException("No values", idx)
    }

    private fun parseRange(): QLRange {
        skipEmpty()
        if (str[idx] == '*') {
            idx++
            return QLRange()
        }
        else return QLRange()
    }

    private fun mergeTableValues(table: QLData, values: QLData): QLData {
        val res = DBTable(table.data.name, table.data.columns ,values.data.data)
        return QLData(res)
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
}

data class Node(val op: QLToken, val nxt: Node?)
