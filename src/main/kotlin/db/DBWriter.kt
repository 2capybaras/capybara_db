package db

import db.DBConfiguration.delimiter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

interface DBWriter {
    fun writeTable(fileName: String, DBTable: DBTable)
    fun writeTableContinuous(fileName: String, DBTable: DBTable)
}

object SimpleLayoutWriter: DBWriter {
    override fun writeTable(fileName: String, DBTable: DBTable) {
        val writer = File(fileName).bufferedWriter()
        writer.write(DBTable.columns.joinToString(separator = delimiter, postfix = "\n" ))
        DBTable.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n" ))
        }
        writer.close()
    }

    override fun writeTableContinuous(fileName: String, DBTable: DBTable) {
        val writer = BufferedWriter(FileWriter(File(fileName), true))
        DBTable.data.forEach {
            writer.write(it.joinToString(separator = delimiter, postfix = "\n"))
        }
        writer.close()
    }
}