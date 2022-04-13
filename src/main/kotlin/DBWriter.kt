import DBConfiguration.delimiter
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


object DBWriter {
    private val random = Random()
    private val pseudoRandomColumns = listOf("Apple", "Orange", "Mango")
    private val numberGenerator: () -> Double = { random.nextDouble() }

    fun writeTable(file: File, table: Table) {
        val writer = file.bufferedWriter()
        writer.write(table.columns.joinToString(separator = delimiter, postfix = "\n" ))
        table.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n" ))
        }
        writer.close()
    }

    fun writeRandomData(file: File, rows: Int) {
        val writer = file.bufferedWriter()
        writer.write(pseudoRandomColumns.joinToString(separator = delimiter, postfix = "\n" ))
        for (i in 0 until rows) {
            val data = ArrayList<Double>()
            for (j in pseudoRandomColumns.indices) {
                data.add(numberGenerator())
            }
            writer.write(data.joinToString(separator = delimiter, postfix = "\n" ))
        }
        writer.close()
    }
}