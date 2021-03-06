package com.lizl.spp.mvvm.activity

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.spp.R
import com.lizl.spp.databinding.ActivitySplashBinding
import com.lizl.spp.module.appinfo.util.AppInfoUtil
import com.lizl.spp.mvvm.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash)
{
    private val REQUEST_CODE_PACKAGE_USAGE_ACCESS = 516

    override fun initView()
    {
        if (hasUsagePermission())
        {
            onPermissionGranted()
        }
        else
        {
            requestUsagePermission()
        }
    }

    private fun onPermissionGranted()
    {
        GlobalScope.launch {
            AppInfoUtil.init()
            GlobalScope.launch(Dispatchers.Main) {
                ActivityUtils.startActivity(MainActivity::class.java)
            }
        }
    }

    private fun hasUsagePermission(): Boolean
    {
        val appOpsM = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsM.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsagePermission()
    {
        startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), REQUEST_CODE_PACKAGE_USAGE_ACCESS);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        Log.d(TAG, "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        if (requestCode == REQUEST_CODE_PACKAGE_USAGE_ACCESS)
        {
            if (!hasUsagePermission())
            {
                finish()
            }
            else
            {
                onPermissionGranted()
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
