package core.model

import config.Config.defaultPageSize

data class Page(val pageSize: Int = defaultPageSize, val pageData: MutableList<Int> = ArrayList(pageSize))

fun pageOf(vararg elements: Int): Page = Page(pageData = elements.toMutableList())