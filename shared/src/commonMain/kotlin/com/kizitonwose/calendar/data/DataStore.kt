package com.kizitonwose.calendar.data

/**
 * Basically [MutableMap.getOrPut] but allows us read the map
 * in multiple places without calling `getOrPut` everywhere.
 */
class DataStore<V>(private val create: (offset: Int) -> V) {

    val map = mutableMapOf<Int, V>()

    operator fun get(key: Int): V {
        val value = map[key]
        return if (value == null) {
            val data = create(key)
            map[key] = data
            data
        } else {
            value
        }
    }

    fun clear() = map.clear()
}