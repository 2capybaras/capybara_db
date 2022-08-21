package feature.scan

import core.query.QueryCommand
import core.query.RootToken

object ScanQueryCommand: QueryCommand {
    override val statement: String = "SCAN"
    override val token: RootToken = ScanToken
}