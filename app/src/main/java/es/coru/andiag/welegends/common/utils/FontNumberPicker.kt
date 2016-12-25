package es.coru.andiag.welegends.common.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.ActivityBase


/**
 * Created by andyq on 25/12/2016.
 */
class FontNumberPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {

//    var type: Typeface? = null

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int,
                         params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, index, params)
//        type = Typeface.createFromAsset(context.assets,
//                "fonts/Beaufort.otf")
        updateView(child)
    }

    override fun addView(child: View, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, params)

//        type = Typeface.createFromAsset(context.assets,
//                "fonts/Beaufort.otf")
        updateView(child)
    }

    private fun updateView(view: View) {

        if (view is EditText) {
            //view.typeface = type
            view.textSize = 25f
            view.setTextColor(ActivityBase.resolveColorAttribute(context,R.attr.mainColor))
        }

    }

}