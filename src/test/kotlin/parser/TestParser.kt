package parser

import domain.ql.*
import org.junit.jupiter.api.Test

class TestParser {
    private val tableName = "test"

    @Test
    fun select() {
        val parser = ExpressionParser("SELECT * FROM $tableName")
        assert(parser.parseTerminate() is QLSelect)
    }

    @Test
    fun create() {
        val parser = ExpressionParser("CREATE $tableName")
        assert(parser.parseTerminate() is QLCreate)
    }

    @Test
    fun insert() {
        val parser = ExpressionParser("INSERT $tableName RANDOM")
        assert(parser.parseTerminate() is QLInsert)
    }

    @Test
    fun drop() {
        val parser = ExpressionParser("DROP $tableName")
        assert(parser.parseTerminate() is QLDrop)
    }

    @Test
    fun index() {
        val parser = ExpressionParser("INDEX $tableName ON a")
        assert(parser.parseTerminate() is QLIndex)
    }

}