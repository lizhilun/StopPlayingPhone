package com.lizl.spp.module.config.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.preferencesSetKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.MutableLiveData
import com.lizl.spp.module.config.annotation.*
import com.lizl.spp.module.config.constant.ConfigConstant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object ConfigUtil
{
    private val defaultConfigMap = HashMap<String, Any>()

    private lateinit var dataStore: DataStore<Preferences>

    private val configObserverMap = HashMap<String, MutableLiveData<Any>>()

    fun initConfig(context: Context)
    {
        dataStore = context.createDataStore(name = "settings")

        ConfigConstant::class.java.declaredMethods.forEach { method ->
            val configKey = method.name.replace("\$annotations", "")
            method.annotations.forEach {
                when (it)
                {
                    is BooleanConfig -> defaultConfigMap[configKey] = it.defaultValue
                    is LongConfig -> defaultConfigMap[configKey] = it.defaultValue
                    is StringConfig -> defaultConfigMap[configKey] = it.defaultValue
                    is StringSetConfig -> defaultConfigMap[configKey] = setOf<String>()
                }
            }
        }
    }

    fun obConfig(configKey: String): MutableLiveData<Any>
    {
        var liveData = configObserverMap[configKey]
        if (liveData == null)
        {
            liveData = MutableLiveData()
            configObserverMap[configKey] = liveData
        }
        configObserverMap[configKey] = liveData
        return liveData
    }

    suspend fun getBoolean(configKey: String): Boolean = getValue(configKey, false)

    suspend fun getLong(configKey: String): Long = getValue(configKey, 0L)

    suspend fun getString(configKey: String): String = getValue(configKey, "")

    suspend fun getStringSet(configKey: String): Set<String> = getSetValue(configKey)

    suspend fun set(configKey: String, value: Any)
    {
        when (value)
        {
            is Boolean -> saveValue(configKey, value)
            is String -> saveValue(configKey, value)
            is Long -> saveValue(configKey, value)
            is Set<*> -> saveSetValue(configKey, value as Set<String>)
        }

        configObserverMap[configKey]?.postValue(value)
    }

    private suspend inline fun <reified T : Any> getValue(configKey: String, valueIfNotFind: T): T
    {
        return dataStore.data.map { preferences -> preferences[preferencesKey<T>(configKey)] ?: getDefault(configKey, valueIfNotFind) }.first()
    }

    private suspend inline fun <reified T : Any> getSetValue(configKey: String): Set<T>
    {
        return dataStore.data.map { preferences -> preferences[preferencesSetKey<T>(configKey)] ?: getDefault(configKey, setOf()) }.first()
    }


    private suspend inline fun <reified T : Any> saveValue(configKey: String, value: T)
    {
        dataStore.edit { preferences -> preferences[preferencesKey<T>(configKey)] = value }
    }

    private suspend inline fun <reified T : Any> saveSetValue(configKey: String, value: Set<T>)
    {
        dataStore.edit { preferences -> preferences[preferencesSetKey<T>(configKey)] = value }
    }

    private inline fun <reified T> getDefault(configKey: String, valueIfNotFind: T): T
    {
        val defaultValue = defaultConfigMap[configKey]
        return if (defaultValue is T) defaultValue else valueIfNotFind
    }
}