package com.lizl.spp.module.appinfo.util

import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.lizl.spp.module.appinfo.model.AppInfoModel
import com.lizl.spp.module.config.constant.ConfigConstant
import com.lizl.spp.module.config.util.ConfigUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AppInfoUtil
{
    private val appInfoLiveData = MutableLiveData<MutableList<AppInfoModel>>()

    init
    {
        GlobalScope.launch(Dispatchers.Main) {
            ConfigUtil.obConfig(ConfigConstant.CONFIG_LOCKED_APP_SET).observeForever { packageList ->
                val appInfoList = appInfoLiveData.value ?: return@observeForever
                if (packageList !is Set<*>) return@observeForever
                appInfoList.forEach { it.isLock = packageList.contains(it.appInfo.packageName) }
                appInfoLiveData.postValue(appInfoList)
            }
        }
    }

    suspend fun init()
    {
        if (appInfoLiveData.value == null)
        {
            refreshAppInfo()
        }
    }

    suspend fun refreshAppInfo()
    {
        val lockList = ConfigUtil.getStringSet(ConfigConstant.CONFIG_LOCKED_APP_SET)
        val now = System.currentTimeMillis()
        val weekAgo = now - 7 * 24 * 60 * 60 * 1000
        val usageStatsManager = Utils.getApp().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val appInfoList = mutableListOf<AppInfoModel>()
        usageStatsManager.queryAndAggregateUsageStats(weekAgo, now).forEach { (_, u) ->
            AppUtils.getAppInfo(u.packageName).let {
                if (it.isSystem) return@forEach
                appInfoList.add(AppInfoModel(it, u, lockList.contains(it.packageName)))
            }
        }
        appInfoList.sortByDescending { it.usage.totalTimeInForeground }
        appInfoLiveData.postValue(appInfoList)
    }

    fun updateAppLockStatus(appInfoModel: AppInfoModel)
    {
        GlobalScope.launch {
            val lockList = ConfigUtil.getStringSet(ConfigConstant.CONFIG_LOCKED_APP_SET).toMutableSet()
            appInfoModel.isLock = !appInfoModel.isLock
            if (appInfoModel.isLock)
            {
                lockList.add(appInfoModel.appInfo.packageName)
            }
            else
            {
                lockList.remove(appInfoModel.appInfo.packageName)
            }
            ConfigUtil.set(ConfigConstant.CONFIG_LOCKED_APP_SET, lockList)
        }
    }

    fun obAppInfoList() = appInfoLiveData
}