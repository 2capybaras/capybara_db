import DBReader.readTable
import DBWriter.writeRandomData
import DBWriter.writeTable
import java.io.File

fun main(args: Array<String>) {
    println("Hello World!")
    println("Program arguments: ${args.joinToString()}")
//    val table = readTable(File("tables/example"))
//    table.data.forEach { println(it) }
//    writeTable(File("tables/example1"), table)
//    println(getStringRowByIndex(table, 1))
//    writeRandomData(File("tables/example3"), 5)
//    val table2 = readTable(File("tables/example3"))
//    println(getStringRowByIndex(table2, 2))
    val tkn = QLSimpleExpressionParser("SELECT * FROM example").parseExpression()
    execute(tkn as QLTerminate)
}

