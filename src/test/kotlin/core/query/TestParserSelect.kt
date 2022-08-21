package core.query

//import domain.ql.ExpressionParser
//import domain.ql.QLSelect
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import java.text.ParseException
//
//class TestParserSelect {
//    private val testTableName = "test"
//
////    @Test
////    fun select() {
////        val parser = ExpressionParser("SELECT FROM $testTableName")
////        val token = parser.parse()
////
////        assertThat(token).isInstanceOf(QLSelect::class.java)
////        with(token as QLSelect) {
////            assertThat(tableName.name).isEqualTo("test")
////            assertThat(range).isNull()
////        }
////    }
////
////    @Test
////    fun selectIndex() {
////        val parser = ExpressionParser("SELECT FROM $testTableName INDEX 1")
////        val token = parser.parse()
////
////        assertThat(token).isInstanceOf(QLSelect::class.java)
////        with(token as QLSelect) {
////            assertThat(tableName.name).isEqualTo(testTableName)
////            assertThat(range).isNotNull
////            assertThat(range!!.a).isEqualTo(0)
////            assertThat(range!!.b).isEqualTo(1)
////        }
////    }
////
////    @Test
////    fun selectFromTo() {
////        val parser = ExpressionParser("SELECT FROM $testTableName FROM 1 TO 2")
////        val token = parser.parse()
////
////        assertThat(token).isInstanceOf(QLSelect::class.java)
////        with(token as QLSelect) {
////            assertThat(tableName.name).isEqualTo(testTableName)
////            assertThat(range).isNotNull
////            assertThat(range!!.a).isEqualTo(0)
////            assertThat(range!!.b).isEqualTo(2)
////        }
////    }
//
//    @Test
//    fun selectNoTableName() {
//        val parser = ExpressionParser("SELECT FROM")
//
//        val exception = assertThrows<ParseException> {
//            parser.parse()
//        }
//
//        assertThat(exception.message).isEqualTo("No string token for table name")
//    }
//
//    @Test
//    fun badToken() {
//        val parser = ExpressionParser("CALL LOCK")
//
//        val exception = assertThrows<ParseException> {
//            parser.parse()
//        }
//
//        assertThat(exception.message).isEqualTo("Bad terminate token")
//    }
//}