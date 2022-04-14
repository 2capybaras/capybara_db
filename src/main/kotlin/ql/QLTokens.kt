package ql

import db.DBConfiguration.noFilter
import db.DBTable

sealed class QLToken

open class QLData(val data: DBTable): QLToken()

class QLJoinData(data: DBTable, val data2: DBTable, val columns: List<String>): QLData(data)

class QLFilter(val column: String = noFilter, val range: QLDoubleRange = QLDoubleRange()): QLToken()

class QLRange(val a: Int = 0, val b: Int = Int.MAX_VALUE): QLToken()

class QLDoubleRange(val a: Double = Double.MIN_VALUE, val b: Double = Double.MAX_VALUE): QLToken()

open class QLTerminate(val qlData: QLData): QLToken()

class QLIndex(qlData: QLData, val column: String): QLTerminate(qlData)

class QLInsert(qlData: QLData): QLTerminate(qlData)

class QLDrop(qlData: QLData): QLTerminate(qlData)

class QLCreate(qlData: QLData): QLTerminate(qlData)

class QLSelect(val peek: List<String>, qlData: QLData, val qlFilter: QLFilter): QLTerminate(qlData)

