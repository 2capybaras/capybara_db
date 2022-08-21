package feature.scan

import core.engine.ExecuteCommand
import core.engine.ExecutionContext

object ScanExecuteCommand: ExecuteCommand {
    override fun execute(arguments: List<Any?>, context: ExecutionContext): String = with(context) {
        val table = layout.getReader().readTable(arguments[0] as String)
        return table.data.joinToString(separator = ", ")
    }
}