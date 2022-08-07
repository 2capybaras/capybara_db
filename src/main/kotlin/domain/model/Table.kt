package domain.model

data class Table(
    val name: String = "",
    val data: List<Page> = ArrayList()
)