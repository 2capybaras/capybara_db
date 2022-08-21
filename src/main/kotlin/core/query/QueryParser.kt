package core.query

import java.text.ParseException

class QueryParser(private val query: String, private val rootToken: Map<String, RootToken> ) {
    private var pos = 0
    private val size = query.length
    var rootStatement: String? = null
    var arguments = emptyList<Any?>()

    fun parse() {
        if (query.isEmpty()) throw EmptyQueryError

        val root = parseRootToken() ?: throw UnsupportedRootTokenError
        arguments = parse(root)
    }

    private fun skipSpaces() {
        while (pos < size && query[pos].isWhitespace()) pos++
    }

    private fun isEndOfWord(): Boolean = query[pos].isWhitespace()

    private fun parseRootToken(): RootToken? {
        rootStatement = parseAnyString()
        return rootToken[rootStatement]
    }

    private fun parse(tkn: Token): Any? {
        return when (tkn) {
            is AnyStringToken -> parseAnyString()
            is StringToken -> parseString(tkn.equal)
            is AnyIntToken -> parseAnyInt()
            is OptionalDependableToken -> parse(tkn)
            is DependableToken -> parse(tkn)
            else -> throw IllegalStateException("Illegal state for $tkn")
        }
    }

    private fun parse(tkn: DependableToken) = tkn.dependencies.map { parse(it) }

    private fun parse(tkn: OptionalDependableToken): Any? {
        skipSpaces()
        if (pos == size) return null
        val before = pos
        tkn.dependencies.forEach {
            pos = before
            try {
                return parse(it)
            } catch (ignored: QueryParserError) {}
        }
        return null // TODO: maybe throw instead of null
    }

    private fun parseAnyString(): String {
        skipSpaces()
        if (pos == size) throw NoTokenError(pos)
        val buffer = StringBuilder()
        while (pos < size && !isEndOfWord()) {
            buffer.append(query[pos++])
        }
        return buffer.toString()
    }

    private fun parseString(equal: String): String {
        val string = parseAnyString()
        if (string != equal) throw ParseException("Expected other $equal, got $string", pos)
        return string
    }

    private fun parseAnyInt(): Int {
        return parseAnyString().toInt()
    }
}