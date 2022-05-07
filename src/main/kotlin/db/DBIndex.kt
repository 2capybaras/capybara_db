package db

import java.util.TreeMap

object DBIndex {
    private val indexes = HashMap<IndexInfo, TreeMap<Double, ArrayList<Int>>>()
    fun dropIndexes(indexInfo: IndexInfo) {
         indexes.remove(indexInfo)
    }

    fun addIndex(indexInfo: IndexInfo, map: TreeMap<Double, ArrayList<Int>>) {
        indexes[indexInfo] = map
    }

    fun getIndex(indexInfo: IndexInfo): Map<Double, List<Int>> {
        return indexes[indexInfo].orEmpty()
    }

    fun isPresented(indexInfo: IndexInfo): Boolean {
        return indexes.contains(indexInfo)
    }
}

data class IndexInfo(val tableName: String, val column: String)