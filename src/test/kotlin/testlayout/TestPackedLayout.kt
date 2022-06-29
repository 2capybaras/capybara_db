package testlayout

import db.DBTable
import db.PackedLayoutReader
import db.PackedLayoutWriter
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertContentEquals

class TestPackedLayout {
    private val cols = listOf("a", "b")
    private val data = listOf(
        listOf(100.0, 2.0)
    )
    @Test
    fun testFilesCreated() {
        PackedLayoutWriter.writeTable("tables/test", DBTable(
            columns = cols,
            data = data
        ))

        assert(File("tables/test.data").exists())
        assert(File("tables/test.meta").exists())
    }

    @Test
    fun testFilesAreReadable() {
        val dbTable = PackedLayoutReader.readTable("tables/test")
        assertContentEquals(dbTable.data, data)
        assertContentEquals(dbTable.columns, cols)
    }
}