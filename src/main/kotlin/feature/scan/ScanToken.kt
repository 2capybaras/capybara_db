package feature.scan

import core.query.AnyStringToken
import core.query.OptionalRangeToken
import core.query.RootToken

object ScanToken: RootToken(AnyStringToken, OptionalRangeToken)