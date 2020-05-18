
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * viewPager 内webView 滚动冲突
 */
public class ScrollWebView extends WebView {

    /**
     * 是否X轴滚动到边界
     */
    private boolean isScrollX = false;

    /**
     * 是否Y轴滚动到边界
     */
    private boolean isScrollY = false;

    /**
     * down-x坐标起始点
     */
    private float x;

    /**
     * down-y坐标起始点
     */
    private float y;

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        isScrollX = clampedX;
        isScrollY = clampedY;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrollX = false;
                isScrollY = false;
                x = event.getRawX();
                y = event.getRawY();
                // 事件由webview处理
                getParent().requestDisallowInterceptTouchEvent(true);
            case MotionEvent.ACTION_MOVE:
                // 嵌套Viewpager时
                float dx = Math.abs(event.getRawX() - x);
                float dy = Math.abs(event.getRawY() - y);
                if (dx > dy) {
                    getParent().requestDisallowInterceptTouchEvent(!isScrollX);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(!isScrollY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
