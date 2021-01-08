package com.lizl.spp.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.spp.R
import com.lizl.spp.databinding.ActivityMainBinding
import com.lizl.spp.mvvm.base.BaseActivity
import com.lizl.spp.adapter.AppInfoListAdapter
import com.lizl.spp.module.appinfo.util.AppInfoUtil

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
    private val appInfoListAdapter by lazy { AppInfoListAdapter() }

    override fun initView()
    {
        dataBinding.appInfoAdapter = appInfoListAdapter
    }

    override fun initData()
    {
        AppInfoUtil.obAppInfoList().observe(this, {
            appInfoListAdapter.replaceData(it)
        })
    }

    override fun onBackPressed()
    {
        ActivityUtils.startHomeActivity()
    }
}