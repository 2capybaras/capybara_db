package core.query

interface QueryCommand {
    val statement: String
    val token: RootToken
}