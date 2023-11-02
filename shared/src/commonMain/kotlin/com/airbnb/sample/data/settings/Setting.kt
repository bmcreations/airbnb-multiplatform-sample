package com.airbnb.sample.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.boolean
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getBooleanOrNullFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.int
import com.russhwolf.settings.long
import com.russhwolf.settings.nullableBoolean
import com.russhwolf.settings.nullableString
import com.russhwolf.settings.string
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class Setting<V> : PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, V>> {
    open fun get(): V {
        val value by this
        return value
    }

    fun set(update: V) {
        var value by this
        value = update
    }

    abstract fun observe(): Flow<V>
}

@OptIn(ExperimentalSettingsApi::class)
open class BooleanSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val defaultValue: Boolean,
    private val getter: (Boolean) -> Boolean = { it }
) : Setting<Boolean>() {
    override fun get(): Boolean {
        return getter(super.get())
    }
    override fun observe() = delegate.getBooleanFlow(key, defaultValue)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.boolean(key, defaultValue)
}

@OptIn(ExperimentalSettingsApi::class)
open class BooleanOrNullSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val getter: (Boolean?) -> Boolean? = { it }
) : Setting<Boolean?>() {
    override fun get(): Boolean? {
        return getter(super.get())
    }
    override fun observe() = delegate.getBooleanOrNullFlow(key)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.nullableBoolean(key)
}

@OptIn(ExperimentalSettingsApi::class)
class LongSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val defaultValue: Long,
    private val getter: (Long) -> Long = { it }
) : Setting<Long>() {

    override fun get(): Long {
        return getter(super.get())
    }
    override fun observe() = delegate.getLongFlow(key, defaultValue)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.long(key, defaultValue)
}

@OptIn(ExperimentalSettingsApi::class)
class IntSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val defaultValue: Int,
    private val getter: (Int) -> Int = { it }
) : Setting<Int>() {

    override fun get(): Int {
        return getter(super.get())
    }

    override fun observe() = delegate.getIntFlow(key, defaultValue)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.int(key, defaultValue)
}

@OptIn(ExperimentalSettingsApi::class)
class StringSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val defaultValue: String,
    private val getter: (String) -> String = { it }
) : Setting<String>() {

    override fun get(): String {
        return getter(super.get())
    }

    override fun observe() = delegate.getStringFlow(key, defaultValue)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.string(key, defaultValue)
}

@OptIn(ExperimentalSettingsApi::class)
class StringOrNullSetting(
    private val delegate: ObservableSettings,
    private val key: String,
    private val getter: (String?) -> String? = { it }
) : Setting<String?>() {

    override fun get(): String? {
        return getter(super.get())
    }

    override fun observe() = delegate.getStringOrNullFlow(key)
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        delegate.nullableString(key)
}

@OptIn(ExperimentalSettingsApi::class)
class EnumSetting<E : Enum<E>>(
    private val delegate: ObservableSettings,
    private val key: String,
    private val defaultValue: E,
    private val mapping: Map<E, Int>,
    private val getter: (E) -> E = { it }
) : Setting<E>() {
    private val inverseMapping = mapping.map { it.value to it.key }.toMap()
    private val mappedDefaultValue = mapping[defaultValue]!!

    override fun get(): E {
        return getter(super.get())
    }
    override fun observe() =
        delegate.getIntFlow(key, mappedDefaultValue).map { inverseMapping[it] ?: defaultValue }

    override fun provideDelegate(thisRef: Any?, property: KProperty<*>) =
        object : ReadWriteProperty<Any?, E> {
            override fun getValue(thisRef: Any?, property: KProperty<*>) =
                inverseMapping[delegate.getInt(key, mappedDefaultValue)] ?: defaultValue

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: E) =
                delegate.putInt(key, mapping[value]!!)
        }
}

internal inline fun <reified E : Enum<E>> enumMapping(mapper: (E) -> Int): Map<E, Int> =
    enumValues<E>().associateWith(mapper)