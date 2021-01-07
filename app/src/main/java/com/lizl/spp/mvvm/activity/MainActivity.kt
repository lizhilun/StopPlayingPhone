package com.lizl.spp.mvvm.activity

import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.spp.R
import com.lizl.spp.databinding.ActivityMainBinding
import com.lizl.spp.mvvm.base.BaseActivity
import com.lizl.spp.adapter.AppInfoListAdapter
import com.lizl.spp.mvvm.viewmodel.AppInfoViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
    private val appInfoListAdapter by lazy { AppInfoListAdapter() }

    private val appInfoViewModel by lazy { ViewModelProvider.AndroidViewModelFactory(application).create(AppInfoViewModel::class.java) }

    override fun initView()
    {
        dataBinding.appInfoAdapter = appInfoListAdapter
    }

    override fun initData()
    {
        appInfoViewModel.obAppInfoList().observe(this, {
            appInfoListAdapter.setDiffNewData(it)
        })

        appInfoViewModel.refreshAppInfoList()
    }

    override fun onBackPressed()
    {
        ActivityUtils.startHomeActivity()
    }
}