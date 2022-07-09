package layout

import db.DBTable
import db.PackedLayoutReader
import db.PackedLayoutWriter
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertContentEquals

class TestPackedLayout {
    private val cols = listOf("a", "b")
    private val data = listOf(
        listOf(100.0, 2.0)
    )

    private val writer = PackedLayoutWriter()
    private val reader = PackedLayoutReader()

    @Test
    fun testFilesCreated() = runBlocking {
        writer.writeTable("tables/test", DBTable(
            columns = cols,
            data = data
        ))

        assert(File("tables/test.data").exists())
        assert(File("tables/test.meta").exists())
    }

    @Test
    fun testFilesAreReadable() = runBlocking {
        val dbTable = reader.readTable("tables/test")

        assertContentEquals(dbTable.data, data)
        assertContentEquals(dbTable.columns, cols)
    }
}