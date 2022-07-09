package domain.cache

import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object DBIndex {
    private val indexes = HashMap<IndexInfo, WeakReference<TreeMap<Double, ArrayList<Int>>>>()
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
        indexes[indexInfo] = WeakReference(map)
    }

    fun getTableIndexes(tableName: String): Set<IndexInfo>{
        if (tableIndex.containsKey(tableName)) return tableIndex[tableName]!!
        return emptySet()
    }

    fun getIndex(indexInfo: IndexInfo): Map<Double, ArrayList<Int>>? {
        return (indexes[indexInfo] ?: return null).get().orEmpty()
    }

    fun isPresented(indexInfo: IndexInfo): Boolean {
        return indexes.contains(indexInfo) && !indexes[indexInfo]?.get().isNullOrEmpty()
    }
}

data class IndexInfo(val tableName: String, val column: String)