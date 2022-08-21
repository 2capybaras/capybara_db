package feature

import core.engine.ExecuteCommand
import core.engine.ExecutionContext
import core.query.QueryCommand
import core.query.RootToken
import kotlin.system.exitProcess

object Exit: Feature {
    override val executeCommand = ExitExecuteCommand
    override val queryCommand = ExitQueryCommand
}

object ExitExecuteCommand: ExecuteCommand {
    override fun execute(arguments: List<Any?>, context: ExecutionContext): String {
        exitProcess(0)
    }
}

object ExitQueryCommand: QueryCommand {
    override val statement: String = "EXIT"
    override val token: RootToken = ExitToken
}

object ExitToken: RootToken()

