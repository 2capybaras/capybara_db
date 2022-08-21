package feature

import core.engine.ExecuteCommand
import core.engine.ExecutionContext
import core.query.*

object Scan: Feature {
    override val executeCommand = ScanExecuteCommand
    override val queryCommand = ScanQueryCommand
}

object ScanExecuteCommand: ExecuteCommand {
    override fun execute(arguments: List<Any?>, context: ExecutionContext): String = with(context) {
        val table = layout.getReader().readTable(arguments[0] as String)
        return table.data.joinToString(separator = ", ")
    }
}

object ScanQueryCommand: QueryCommand {
    override val statement: String = "SCAN"
    override val token: RootToken = ScanToken
}

object ScanToken: RootToken(AnyStringToken, OptionalRangeToken)
