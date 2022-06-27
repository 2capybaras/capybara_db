package db

interface DBLayout {
    fun getReader(): DBReader
    fun getWriter(): DBWriter
}

/**
 * Simple Layout:
 *
 * table.txt
 * __________
 * column1,column2,column3
 * 1,2.3,3
 * 3.4,0,5
 */
class SimpleLayout: DBLayout {
    override fun getReader(): DBReader {
        return SimpleLayoutReader
    }

    override fun getWriter(): DBWriter {
        return SimpleLayoutWriter
    }
}

/**
 * Packed Layout:
 *
 * table.meta
 * __________
 * column1
 * column2
 * column3
 * column4
 *
 * table.data
 * __________
 * 1.02.234.5
 *
 * uses byte double size to read correctly
 */
class PackedLayout: DBLayout {
    override fun getReader(): DBReader {
        TODO("Not yet implemented")
    }

    override fun getWriter(): DBWriter {
        TODO("Not yet implemented")
    }

}