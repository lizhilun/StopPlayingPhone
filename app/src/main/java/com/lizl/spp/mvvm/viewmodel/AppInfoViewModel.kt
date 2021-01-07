package com.lizl.spp.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.AppUtils.AppInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppInfoViewModel : ViewModel()
{
    private val appInfoListLiveData = MutableLiveData<MutableList<AppInfo>>()

    fun obAppInfoList() = appInfoListLiveData

    fun refreshAppInfoList()
    {
        GlobalScope.launch { appInfoListLiveData.postValue(AppUtils.getAppsInfo().filterNot { it.isSystem }.toMutableList()) }
    }
}