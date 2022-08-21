package feature.exit

import core.engine.ExecuteCommand
import core.engine.ExecutionContext
import kotlin.system.exitProcess

object ExitExecuteCommand: ExecuteCommand {
    override fun execute(arguments: List<Any?>, context: ExecutionContext): String {
        exitProcess(0)
    }
}