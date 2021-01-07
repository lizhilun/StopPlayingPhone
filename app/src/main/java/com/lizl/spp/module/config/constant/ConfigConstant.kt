package com.lizl.spp.module.config.constant

import com.lizl.spp.module.config.annotation.StringConfig
import com.lizl.spp.module.config.annotation.StringSetConfig

object ConfigConstant
{
    const val APP_NIGHT_MODE_ON = "APP_NIGHT_MODE_ON"
    const val APP_NIGHT_MODE_OFF = "APP_NIGHT_MODE_OFF"
    const val APP_NIGHT_MODE_FOLLOW_SYSTEM = "APP_NIGHT_MODE_FOLLOW_SYSTEM"

    //暗黑模式
    @StringConfig(APP_NIGHT_MODE_FOLLOW_SYSTEM)
    const val CONFIG_DARK_MODE = "CONFIG_DARK_MODE"

    @StringSetConfig
    const val CONFIG_LOCKED_APP_SET = "CONFIG_LOCKED_APP_SET"
}