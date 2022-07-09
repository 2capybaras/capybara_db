package domain.model

data class Table(
    val name: String = "",
    val columns: List<String> = ArrayList(),
    val data: List<List<Double>> = ArrayList()
)