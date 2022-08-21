package core.engine

interface ExecuteCommand {
    fun execute(arguments: List<Any?>, context: ExecutionContext): String
}