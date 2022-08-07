package domain.layout

import domain.file.Page
import domain.model.Table
import java.io.EOFException
import java.io.IOException
import java.io.RandomAccessFile

interface LayoutReader {
    fun readTable(fileName: String): Table
}

class PackedLayoutReader: LayoutReader {
    override fun readTable(fileName: String): Table {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        val pages = mutableListOf<Page>()

        read@ while (true) {
            val page = Page()
            for (i in 0 until page.pageSize) {
                try {
                    page.pageData.add(dataFile.readInt())
                } catch (eof: EOFException) {
                    break@read
                } catch (ex: IOException) {
                    break@read
                }
            }
            pages.add(page)
        }
        return Table(fileName, pages)
    }
}