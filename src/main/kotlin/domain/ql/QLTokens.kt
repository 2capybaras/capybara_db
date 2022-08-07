package domain.ql

import domain.model.Table

sealed interface QLToken

open class QLTerminate: QLToken

open class QLData(val data: Table): QLToken

class QLTableName(val name: String): QLToken

class QLRange(val a: Int = 0, val b: Int = Int.MAX_VALUE): QLToken

class QLSelect(val tableName: QLTableName, val range: QLRange?): QLTerminate()

