package core.query

sealed interface Token
open class ValueToken<T>(val value: T): Token
object AnyStringToken: Token
open class StringToken(val equal: String): Token
object FromToken: StringToken(QueryKeywords.from)
object AnyIntToken: Token
//class IntToken(defaultValue: Int): ValueToken<Int>(defaultValue)
open class DependableToken(val dependencies: List<Token>): Token
open class OptionalDependableToken(vararg dependencies: Token): DependableToken(dependencies.toList())
open class OneFromToken(vararg dependencies: Token): DependableToken(dependencies.toList())
open class RootToken(vararg dependencies: Token): DependableToken(dependencies.toList())
open class RangeToken(vararg dependencies: Token): DependableToken(dependencies.toList())
//object FromToRangeToken: RangeToken(
//    FromToken,
//    IntToken(0),
//    StringToken("TO"),
//    IntToken(MAX_VALUE)
//)
object IndexRangeTokenToken: RangeToken(StringToken("INDEX"), AnyIntToken)
object AnyRangeToken: OneFromToken(IndexRangeTokenToken)
object OptionalRangeToken: OptionalDependableToken(AnyRangeToken)
