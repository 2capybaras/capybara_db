package domain.layout

import domain.file.Page
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.RandomAccessFile

interface LayoutWriter {
    suspend fun writeTable(fileName: String, pages: List<Page>)
    suspend fun writeTableContinuous(fileName: String, pages: List<Page>)
}

class PackedLayoutWriter: LayoutWriter {
    override suspend fun writeTable(fileName: String, pages: List<Page>) = withContext(IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        pages.forEach { page ->
            page.pageData.forEach {
                dataFile.writeInt(it)
            }
        }
    }

    override suspend fun writeTableContinuous(fileName: String, pages: List<Page>) = withContext(IO) {
        val dataFile = RandomAccessFile("$fileName.data", "rw")
        dataFile.seek(dataFile.length())
        pages.forEach { page ->
            page.pageData.forEach {
                dataFile.write(it)
            }
        }
    }

}