package es.coru.andiag.welegends.common;

/**
 * Created by Canalejas on 08/12/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by andyq on 13/09/2016.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Aller.ttf"));
    }
}
