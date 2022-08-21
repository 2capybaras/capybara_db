package feature.scan

import feature.Feature

object Scan: Feature {
    override val executeCommand = ScanExecuteCommand
    override val queryCommand = ScanQueryCommand
}

