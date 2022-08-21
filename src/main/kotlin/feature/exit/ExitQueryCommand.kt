package feature.exit

import core.query.QueryCommand
import core.query.RootToken

object ExitQueryCommand: QueryCommand {
    override val statement: String = "EXIT"
    override val token: RootToken = ExitToken
}