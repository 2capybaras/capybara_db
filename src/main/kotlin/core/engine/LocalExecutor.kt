package core.engine

class LocalExecutor {
    private val context = ExecutionContext()
    fun execute(command: ExecuteCommand, arguments: List<Any?>): String {
        return command.execute(arguments, context)
    }
}