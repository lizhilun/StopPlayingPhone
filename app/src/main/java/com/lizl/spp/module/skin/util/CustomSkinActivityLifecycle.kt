package com.lizl.spp.module.skin.util

import android.app.Activity
import android.app.Application
import android.os.Bundle

object CustomSkinActivityLifecycle : Application.ActivityLifecycleCallbacks
{
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
    {
        SkinUtil.updateStatusBarColor(activity)
    }

    override fun onActivityStarted(activity: Activity)
    {
    }

    override fun onActivityResumed(activity: Activity)
    {
    }

    override fun onActivityPaused(activity: Activity)
    {
    }

    override fun onActivityStopped(activity: Activity)
    {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)
    {
    }

    override fun onActivityDestroyed(activity: Activity)
    {
    }
}