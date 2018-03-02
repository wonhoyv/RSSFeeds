package be.ehb.rssfeeds.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;



//bij klikken worden methoden uit webviev opgeroepen, niet uit listview, omzeilen door gedrag uit te schakelen
public class NonClickableWebview extends WebView {
    public NonClickableWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(false);
        setLongClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
