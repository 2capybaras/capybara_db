package feature

import core.engine.ExecuteCommand
import core.query.QueryCommand

interface Feature {
    val executeCommand: ExecuteCommand
    val queryCommand: QueryCommand
}