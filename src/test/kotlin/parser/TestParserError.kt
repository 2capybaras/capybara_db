package parser

import domain.ql.ExpressionParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException

class TestParserError {
    private val tableName = "test"

    @Test
    fun `fail when no terminate`() {
        val parser = ExpressionParser("")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when no valid terminate`() {
        val parser = ExpressionParser("DO something FROM $tableName")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when select without from`() {
        val parser = ExpressionParser("SELECT * FROM")
        assertThrows<IndexOutOfBoundsException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when select with invalid range`() {
        val parser = ExpressionParser("SELECT * FROM $tableName FIRST")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when index without 'on' keyword`() {
        val parser = ExpressionParser("INDEX $tableName a")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when index without columns`() {
        val parser = ExpressionParser("INDEX $tableName ON")
        assertThrows<NoSuchElementException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when insert without values`() {
        val parser = ExpressionParser("INSERT $tableName")
        assertThrows<IndexOutOfBoundsException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when insert with invalid values`() {
        val parser = ExpressionParser("INSERT $tableName RAN")
        assertThrows<ParseException> { parser.parseTerminate() }
    }
}