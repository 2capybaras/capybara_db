package parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ql.*
import java.text.ParseException

class TestParserError {
    private val tableName = "test"

    @Test
    fun `fail when no terminate`() {
        val parser = QLExpressionParser("")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when no valid terminate`() {
        val parser = QLExpressionParser("DO something FROM $tableName")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when select without from`() {
        val parser = QLExpressionParser("SELECT * FROM")
        assertThrows<IndexOutOfBoundsException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when select with invalid range`() {
        val parser = QLExpressionParser("SELECT * FROM $tableName FIRST")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when index without 'on' keyword`() {
        val parser = QLExpressionParser("INDEX $tableName a")
        assertThrows<ParseException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when index without columns`() {
        val parser = QLExpressionParser("INDEX $tableName ON")
        assertThrows<NoSuchElementException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when insert without values`() {
        val parser = QLExpressionParser("INSERT $tableName")
        assertThrows<IndexOutOfBoundsException> { parser.parseTerminate() }
    }

    @Test
    fun `fail when insert with invalid values`() {
        val parser = QLExpressionParser("INSERT $tableName RAN")
        assertThrows<ParseException> { parser.parseTerminate() }
    }
}