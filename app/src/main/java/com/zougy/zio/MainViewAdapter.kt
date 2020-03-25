package com.zougy.zio

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.xutils.x

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class MainViewAdapter(beanList: MutableList<TideDemoBean>) :
    BaseQuickAdapter<TideDemoBean, BaseViewHolder>(R.layout.layout_item_view, beanList) {

    override fun convert(helper: BaseViewHolder, item: TideDemoBean?) {
        helper.setText(R.id.layoutItemTvName, item!!.name.zhHans)
        x.image().bind(helper.getView(R.id.layoutItemImg), item.cover_url)
        helper.addOnClickListener(R.id.layoutItemBtSound)
    }

}