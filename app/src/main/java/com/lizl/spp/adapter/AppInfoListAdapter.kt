package com.lizl.spp.adapter

import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.AppUtils.AppInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.spp.R
import com.lizl.spp.custom.other.CustomDiffUtil
import com.lizl.spp.databinding.ItemAppInfoBinding

class AppInfoListAdapter : BaseQuickAdapter<AppInfo, BaseViewHolder>(R.layout.item_app_info)
{
    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.packageName == newItem.packageName }, { oldItem, newItem ->
            oldItem.name == newItem.name && oldItem.icon == newItem.icon
        }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemAppInfoBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: AppInfo)
    {
        helper.getBinding<ItemAppInfoBinding>()?.apply {
            appInfo = item
            executePendingBindings()
        }
    }
}