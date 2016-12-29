package com.andiag.welegends.views.adapters

import android.graphics.Color
import android.widget.TextView
import com.andiag.welegends.R
import com.andiag.welegends.common.base.ActivityBase
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by andyq on 24/12/2016.
 */
class AdapterNavDrawer(layoutResId: Int, data: List<com.andiag.welegends.views.adapters.items.ItemNavDrawer>) : BaseQuickAdapter<com.andiag.welegends.views.adapters.items.ItemNavDrawer, BaseViewHolder>(layoutResId, data) {

    private var selected = 0
    private var colorTextSelected: Int = 0
    private var firstTime = true

    //    private static int getThemeAccentColor (final Context context) {
    //        final TypedValue value = new TypedValue();
    //        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
    //        return value.data;
    //    }

    fun setSelected(position: Int) {
        if (position != selected) {
            selected = position
            notifyDataSetChanged()
        }
    }

    override fun convert(holder: BaseViewHolder, item: com.andiag.welegends.views.adapters.items.ItemNavDrawer) {
        if (firstTime) {
            colorTextSelected = ActivityBase.resolveColorAttribute(mContext,R.attr.mainColor)
            firstTime = false
        }
        holder.setImageResource(R.id.imageItem, item.imageId)
        val title = holder.getView<TextView>(R.id.textItem)
        title.setText(item.title)
        if (holder.adapterPosition == selected) {
            title.setTextColor(colorTextSelected)
        } else {
            title.setTextColor(Color.GRAY)
        }
    }
}