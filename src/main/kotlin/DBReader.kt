import DBConfiguration.delimiter
import java.io.File

object DBReader {
    fun readTable(file: File): Table {
        val lines = file.readLines()
        val columns = lines[0].split(delimiter)
        val data = ArrayList<List<Double>>()
        lines.drop(1).forEach {
            if (it.isEmpty()) return@forEach
            val values = it.split(delimiter).map { s: String -> s.toDouble() }
            data.add(values)
        }
        return Table(columns, data)
    }
}