package com.lizl.spp.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.spp.R
import com.lizl.spp.custom.other.CustomDiffUtil
import com.lizl.spp.databinding.ItemAppInfoBinding
import com.lizl.spp.module.appinfo.model.AppInfoModel

class AppInfoListAdapter : BaseQuickAdapter<AppInfoModel, BaseViewHolder>(R.layout.item_app_info)
{
    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.appInfo.packageName == newItem.appInfo.packageName }, { oldItem, newItem ->
            oldItem.appInfo.name == newItem.appInfo.name && oldItem.appInfo.icon == newItem.appInfo.icon
        }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemAppInfoBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: AppInfoModel)
    {
        helper.getBinding<ItemAppInfoBinding>()?.apply {
            appInfoModel = item
            executePendingBindings()
        }
    }
}