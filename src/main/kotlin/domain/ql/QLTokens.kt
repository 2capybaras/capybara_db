package domain.ql

import kotlin.Int.Companion.MAX_VALUE

sealed interface QLToken
open class QLDefaultValue<T>(val value: T): QLToken
object QLAnyString: QLToken
class QLString(val equal: String): QLToken
object QLAnyInt: QLToken
class QLInt(defaultValue: Int): QLDefaultValue<Int>(defaultValue)
open class QLDependable(val dependencies: List<QLToken>): QLToken
open class QLOptionalDependable(vararg dependencies: QLToken): QLDependable(dependencies.toList())
open class QLOneFromOptional(vararg dependencies: QLToken): QLOptionalDependable(*dependencies)
open class QLStart(vararg dependencies: QLToken): QLDependable(dependencies.toList())
open class QLRange(vararg dependencies: QLToken): QLDependable(dependencies.toList())
object QLFromToRange: QLRange(QLString("FROM"), QLInt(0), QLString("TO"), QLInt(MAX_VALUE))
object QLIndexRange: QLRange(QLString("INDEX"), QLAnyInt)
object QLAnyRange: QLOneFromOptional(QLFromToRange, QLIndexRange)
object QLSelect: QLStart(QLString("SELECT"), QLString("FROM"), QLAnyString, QLAnyRange)

