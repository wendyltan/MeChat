package xyz.wendyltanpcy.mechat.helper;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.wendyltanpcy.mechat.FriendAddActivity;
import xyz.wendyltanpcy.mechat.R;

/**
 * Created by Wendy on 2017/10/13.
 */

public class TitleBarLayout extends LinearLayout {

    @BindView(R.id.mychat_image)
    public ImageView addImage;

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        ButterKnife.bind(this);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(),FriendAddActivity.class));
            }
        });
    }

    



}
