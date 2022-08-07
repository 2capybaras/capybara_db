package domain.model

import domain.file.Page

data class Table(
    val name: String = "",
    val data: List<Page> = ArrayList()
)