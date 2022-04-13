sealed class QLToken

class QLData(val data: DBTable): QLToken()

class QLRange(val a: Int = 0, val b: Int = Int.MAX_VALUE): QLToken()

open class QLTerminate(val qlData: QLData): QLToken()

class QLInsert(qlData: QLData): QLTerminate(qlData)

class QLDrop(qlData: QLData): QLTerminate(qlData)

class QLCreate(qlData: QLData): QLTerminate(qlData)

class QLSelect(val qlRange: QLRange, qlData: QLData): QLTerminate(qlData)

