package domain.api

import config.Config.path
import domain.layout.Layout
import domain.layout.LayoutReader
import domain.layout.PackedLayout
import domain.ql.*

class LocalExecutor {
    fun execute(tkn: QLTerminate, layout: Layout = PackedLayout()): String {
        return when (tkn) {
            is QLSelect -> with(tkn) {
                select(tableName, range, layout.getReader())
            }
            else -> throw IllegalStateException("Token is not supported yet :(")
        }
    }

    private fun select(tableName: QLTableName, range: QLRange?, reader: LayoutReader): String {
        return reader.readTable("$path${tableName.name}").data.joinToString(", ")
    }
}