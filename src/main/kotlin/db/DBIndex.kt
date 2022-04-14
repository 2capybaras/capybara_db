package db

object DBIndex {
    private val indexes = HashMap<IndexInfo, HashMap<Double, Int>>()
    fun dropIndexes(indexInfo: IndexInfo) {
         indexes.remove(indexInfo)
    }

    fun addIndex(indexInfo: IndexInfo, map: HashMap<Double, Int>) {
        indexes[indexInfo] = map
    }

    fun getIndex(indexInfo: IndexInfo): Map<Double, Int> {
        return indexes[indexInfo].orEmpty()
    }

    fun isPresented(indexInfo: IndexInfo): Boolean {
        return indexes.contains(indexInfo)
    }
}

data class IndexInfo(val tableName: String, val column: String)