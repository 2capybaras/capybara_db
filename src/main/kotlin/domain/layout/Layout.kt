package domain.layout

interface Layout {
    fun getReader(): LayoutReader
    fun getWriter(): LayoutWriter
}

/**
 * Packed Layout:
 *
 * table.data
 * __________
 * 122345
 *
 * uses byte size of int to read correctly
 */
class PackedLayout: Layout {
    override fun getReader(): LayoutReader {
        return PackedLayoutReader()
    }

    override fun getWriter(): LayoutWriter {
        return PackedLayoutWriter()
    }

}