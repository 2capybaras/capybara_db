package domain.file

import config.Config.defaultPageSize

data class Page(private val pageSize: Int = defaultPageSize, val pageData: MutableList<Int> = ArrayList(pageSize))

fun pageOf(vararg elements: Int): Page = Page(pageData = elements.toMutableList())