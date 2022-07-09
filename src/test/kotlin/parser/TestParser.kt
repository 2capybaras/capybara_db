package parser

import org.junit.jupiter.api.Test
import ql.*

class TestParser {
    private val tableName = "test"

    @Test
    fun select() {
        val parser = QLExpressionParser("SELECT * FROM $tableName")
        assert(parser.parseTerminate() is QLSelect )
    }

    @Test
    fun create() {
        val parser = QLExpressionParser("CREATE $tableName")
        assert(parser.parseTerminate() is QLCreate )
    }

    @Test
    fun insert() {
        val parser = QLExpressionParser("INSERT $tableName RANDOM")
        assert(parser.parseTerminate() is QLInsert )
    }

    @Test
    fun drop() {
        val parser = QLExpressionParser("DROP $tableName")
        assert(parser.parseTerminate() is QLDrop)
    }

    @Test
    fun index() {
        val parser = QLExpressionParser("INDEX $tableName ON a")
        assert(parser.parseTerminate() is QLIndex)
    }

}