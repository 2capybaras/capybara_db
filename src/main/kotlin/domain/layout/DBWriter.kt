package domain.layout

import config.Config.packedColumnsDelimiter
import config.Config.simpleColumnsDelimiter
import domain.model.Table
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.RandomAccessFile

interface DBWriter {
    suspend fun writeTable(fileName: String, table: Table)
    suspend fun writeTableContinuous(fileName: String, table: Table)
}

class SimpleLayoutWriter: DBWriter {
    override suspend fun writeTable(fileName: String, table: Table) = withContext(Dispatchers.IO) {
        val writer = File(fileName).bufferedWriter()
        writer.write(table.columns.joinToString(separator = simpleColumnsDelimiter, postfix = "\n" ))
        table.data.forEach {
            writer.write(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n" ))
        }
        writer.close()
    }

    override suspend fun writeTableContinuous(fileName: String, table: Table) = withContext(Dispatchers.IO) {
        val writer = BufferedWriter(FileWriter(File(fileName), true))
        table.data.forEach {
            writer.write(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
        }
        writer.close()
    }
}

class PackedLayoutWriter: DBWriter {
    override suspend fun writeTable(fileName: String, table: Table) = withContext(Dispatchers.IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        table.data.forEach { doubles ->
            doubles.forEach {
                dataFile.writeDouble(it)
            }
        }

        val writer = File("$fileName.meta").bufferedWriter()
        writer.write(table.data.size.toString())
        writer.write(table.columns.joinToString(separator = packedColumnsDelimiter, prefix = "\n"))
        writer.close()
    }

    override suspend fun writeTableContinuous(fileName: String, table: Table) = withContext(Dispatchers.IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        dataFile.seek(dataFile.length())
        table.data.forEach { doubles ->
            doubles.forEach {
                dataFile.writeDouble(it)
            }
        }
        val reader = File("$fileName.meta").bufferedReader()
        val prevRows = reader.readLine().toInt()
        val columns = reader.readText().split(packedColumnsDelimiter)
        val writer = File("$fileName.meta").bufferedWriter()
        writer.write((table.data.size+prevRows).toString())
        writer.write(columns.joinToString(separator = packedColumnsDelimiter, prefix = "\n"))
        writer.close()
    }

}