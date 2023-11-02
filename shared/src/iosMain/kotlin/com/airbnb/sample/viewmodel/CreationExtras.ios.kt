package com.airbnb.sample.viewmodel


actual interface CreationExtrasKey<T>

actual abstract class CreationExtras internal actual constructor() {
    internal val map: MutableMap<Key<*>, Any?> = mutableMapOf()

    actual abstract operator fun <T> get(key: Key<T>): T?
}

actual object EmptyCreationExtras : CreationExtras() {
    override fun <T> get(key: Key<T>): T? = null
}

actual class MutableCreationExtras actual constructor(initialExtras: CreationExtras) : CreationExtras() {
    init {
        map.putAll(initialExtras.map)
    }

    /**
     * Associates the given [key] with [t]
     */
    actual operator fun <T> set(key: Key<T>, t: T) {
        map[key] = t
    }

    override fun <T> get(key: Key<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return map[key] as T?
    }
}


actual val VIEW_MODEL_KEY: Key<String> = object : Key<String> {}