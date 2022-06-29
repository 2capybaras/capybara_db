package db

import db.DBConfiguration.delimiter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.RandomAccessFile

interface DBWriter {
    suspend fun writeTable(fileName: String, dbTable: DBTable)
    suspend fun writeTableContinuous(fileName: String, dbTable: DBTable)
}

class SimpleLayoutWriter: DBWriter {
    override suspend fun writeTable(fileName: String, dbTable: DBTable) = withContext(Dispatchers.IO) {
        val writer = File(fileName).bufferedWriter()
        writer.write(dbTable.columns.joinToString(separator = delimiter, postfix = "\n" ))
        dbTable.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n" ))
        }
        writer.close()
    }

    override suspend fun writeTableContinuous(fileName: String, dbTable: DBTable) = withContext(Dispatchers.IO) {
        val writer = BufferedWriter(FileWriter(File(fileName), true))
        dbTable.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n"))
        }
        writer.close()
    }
}

class PackedLayoutWriter: DBWriter {
    override suspend fun writeTable(fileName: String, dbTable: DBTable) = withContext(Dispatchers.IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        dbTable.data.forEach { doubles ->
            doubles.forEach {
                dataFile.writeDouble(it)
            }
        }

        val writer = File("$fileName.meta").bufferedWriter()
        writer.write(dbTable.data.size.toString())
        writer.write(dbTable.columns.joinToString(separator = ",", prefix = "\n"))
        writer.close()
    }

    override suspend fun writeTableContinuous(fileName: String, dbTable: DBTable) = withContext(Dispatchers.IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        dataFile.seek(dataFile.length())
        dbTable.data.forEach { doubles ->
            doubles.forEach {
                dataFile.writeDouble(it)
            }
        }
        val reader = File("$fileName.meta").bufferedReader()
        val prevRows = reader.readLine().toInt()
        val columns = reader.readText().split(",")
        val writer = File("$fileName.meta").bufferedWriter()
        writer.write((dbTable.data.size+prevRows).toString())
        writer.write(columns.joinToString(separator = ",", prefix = "\n"))
        writer.close()
    }

}