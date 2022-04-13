import DBConfiguration.delimiter
import Utils.numberGenerator
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import kotlin.collections.ArrayList


object DBWriter {
    private val pseudoRandomColumns = listOf("Apple", "Orange", "Mango")

    fun writeTable(file: File, DBTable: DBTable) {
        val writer = file.bufferedWriter()
        writer.write(DBTable.columns.joinToString(separator = delimiter, postfix = "\n" ))
        DBTable.data.forEach {
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

    fun writeTableContinuous(file: File, DBTable: DBTable) {
        val writer = BufferedWriter(FileWriter(file, true))
        DBTable.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n"))
        }
        writer.close()
    }
}