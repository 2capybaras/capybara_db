package core.layout

import core.model.pageOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class TestPackedLayout {
    private val pages = listOf(
        pageOf(100, 2, 3)
    )
    private val layout = PackedLayout()

    private val writer = layout.getWriter()
    private val reader = layout.getReader()

    @Test
    fun testFilesCreated(): Unit = runBlocking {
        writer.writeTable("test", pages)

        assertThat(File("test.data").exists())
    }

    @Test
    fun testFilesAreReadable() {
        val table = reader.readTable("test")

        assertThat(table.data).isEqualTo(pages)
    }
}