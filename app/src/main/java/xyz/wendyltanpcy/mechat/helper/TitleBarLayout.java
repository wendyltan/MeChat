package xyz.wendyltanpcy.mechat.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import xyz.wendyltanpcy.mechat.R;

/**
 * Created by Wendy on 2017/10/13.
 */

public class TitleBarLayout extends LinearLayout {

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
    }
    



}
