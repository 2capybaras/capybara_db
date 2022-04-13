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
    val tkn = QLExpressionParser("SELECT * FROM example FROM 2 TO 3").parseTerminate()
    execute(tkn)
    execute(QLExpressionParser("CREATE example4(A, B, C)").parseTerminate())
    execute(QLExpressionParser("INSERT example4 RANDOM").parseTerminate())
    execute(QLExpressionParser("DROP example4").parseTerminate())
}

