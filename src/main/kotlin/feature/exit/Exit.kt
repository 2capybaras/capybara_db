package feature.exit

import feature.Feature

object Exit: Feature {
    override val executeCommand = ExitExecuteCommand
    override val queryCommand = ExitQueryCommand
}