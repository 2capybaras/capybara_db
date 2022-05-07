package db

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object DBIndex {
    private val indexes = HashMap<IndexInfo, TreeMap<Double, ArrayList<Int>>>()
    // used for fast checking
    private val tableIndex = HashMap<String, MutableSet<IndexInfo>>()
    fun dropIndex(indexInfo: IndexInfo) {
        require(indexes.contains(indexInfo) && tableIndex.containsKey(indexInfo.tableName)) {
            "Index $indexInfo not found!"
        }
        indexes.remove(indexInfo)
        // TODO: throw err if not present
        tableIndex[indexInfo.tableName]?.remove(indexInfo)
    }

    fun addIndex(indexInfo: IndexInfo, map: TreeMap<Double, ArrayList<Int>>) {
        if (tableIndex[indexInfo.tableName] == null) tableIndex[indexInfo.tableName] = HashSet()
        tableIndex[indexInfo.tableName]?.add(indexInfo)
        indexes[indexInfo] = map
    }

    fun getTableIndexes(tableName: String): Set<IndexInfo>{
        if (tableIndex.containsKey(tableName)) return tableIndex[tableName]!!
        return emptySet()
    }

    fun getIndex(indexInfo: IndexInfo): Map<Double, List<Int>> {
        return indexes[indexInfo].orEmpty()
    }

    fun isPresented(indexInfo: IndexInfo): Boolean {
        return indexes.contains(indexInfo)
    }
}

data class IndexInfo(val tableName: String, val column: String)