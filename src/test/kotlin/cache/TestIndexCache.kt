package cache

import domain.cache.DBIndex
import domain.cache.IndexInfo
import org.junit.jupiter.api.Test
import java.util.*

class TestIndexCache {
    @Test
    fun testClearedAfterGC() {
        val idx = IndexInfo("test", "test")
        var map: TreeMap<Double, ArrayList<Int>>? = TreeMap<Double, ArrayList<Int>>()
        map!![1.0] = ArrayList()
        map[1.0]!!.add(1)
        DBIndex.addIndex(idx, map)
        map = null
        System.gc()
        assert(!DBIndex.isPresented(idx))
    }
}