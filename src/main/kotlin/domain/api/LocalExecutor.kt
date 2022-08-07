package domain.api

import domain.layout.Layout
import domain.layout.LayoutReader
import domain.layout.PackedLayout
import domain.ql.QLRange
import domain.ql.QLSelect
import domain.ql.QLTableName
import domain.ql.QLTerminate
import io.ktor.utils.io.*

class LocalExecutor {
    suspend fun execute(tkn: QLTerminate, channel: ByteWriteChannel, layout: Layout = PackedLayout()) {
        when (tkn) {
            is QLSelect -> with(tkn) {
                select(tableName, range, channel, layout.getReader())
            }
            else -> throw IllegalStateException("Token is not supported yet :(")
        }
    }

    private suspend fun select(tableName: QLTableName, range: QLRange?, channel: ByteWriteChannel, reader: LayoutReader) {
        val answer = StringBuilder()
//        if (qlFilter.column == noFilter) {
//            val a = qlFilter.range.a.toInt()
//            val b = qlFilter.range.b.toInt()
//            val table = reader.readTable((path + qlData.data.name))
//            val columns = table.columns
//            val data = table.data
//            answer.append(columns.joinToString(separator = simpleColumnsDelimiter))
//            answer.append("\n")
//            data.drop(a)
//                .take(b - a)
//                .forEach {
//                    answer.append(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
//                }
//        } else {
//            val a = qlFilter.range.a + 1
//            val b = qlFilter.range.b
//            if (isPresented(IndexInfo(qlData.data.name, qlFilter.column))) {
//                answer.append("Indexed\n")
//                answer.append(
//                    reader.readColumns((path + qlData.data.name)).joinToString(separator = simpleColumnsDelimiter)
//                )
//                answer.append("\n")
//                val idx = getIndex(IndexInfo(qlData.data.name, qlFilter.column))!!
//                idx.entries.filter { it.key in a..b }.flatMap { it.value }.forEach {
//                    answer.append(
//                        reader.readRow((path + qlData.data.name), it + 1)
//                            .joinToString(separator = simpleColumnsDelimiter, postfix = "\n")
//                    )
//                }
//            } else {
//                val table = reader.readTable((path + qlData.data.name))
//                val columns = table.columns
//                val data = table.data
//                answer.append(columns.joinToString(separator = simpleColumnsDelimiter))
//                answer.append("\n")
//
//                val pos = columns.indexOf(qlFilter.column)
//                data.filter { it[pos] in a..b }.forEach {
//                    answer.append(it.joinToString(separator = simpleColumnsDelimiter, postfix = "\n"))
//                }
//            }
//        }
        channel.writeStringUtf8(answer.toString())
    }
}