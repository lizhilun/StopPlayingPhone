package com.lizl.spp.module.appinfo.util

import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.lizl.spp.module.appinfo.model.AppInfoModel

object AppInfoUtil
{
    private val appInfoLiveData = MutableLiveData<MutableList<AppInfoModel>>()

    fun init()
    {
        if (appInfoLiveData.value == null)
        {
            refreshAppInfo()
        }
    }

    fun refreshAppInfo()
    {
        val now = System.currentTimeMillis()
        val weekAgo = now - 7 * 24 * 60 * 60 * 1000
        val usageStatsManager = Utils.getApp().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val appInfoList = mutableListOf<AppInfoModel>()
        usageStatsManager.queryAndAggregateUsageStats(weekAgo, now).forEach { (_, u) ->
            AppUtils.getAppInfo(u.packageName).let {
                if (it.isSystem) return@forEach
                appInfoList.add(AppInfoModel(it, u))
            }
        }
        appInfoList.sortByDescending { it.usage.totalTimeInForeground }
        appInfoLiveData.postValue(appInfoList)
    }

    fun obAppInfoList() = appInfoLiveData
}