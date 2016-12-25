package es.coru.andiag.welegends.common.utils;

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

    private void init(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Beaufort.otf"));
    }

    public FontTextView(Context context) {
        super(context);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
