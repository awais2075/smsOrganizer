package com.awais2075gmail.awais2075.layout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;

/**
 * Created by Muhammad Awais Rashi on 25-Jan-18.
 */

public class CustomItemLayout extends ConstraintLayout{


    public CustomItemLayout(Context context) {
        super(context);
        init();
    }

    public CustomItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.item_activity, this);
        /*private TextView text_heading;
        private TextView text_subHeading;
        private TextView text_rightToHeading;
        private View view;*/


        TextView text_heading = findViewById(R.id.text_heading);
        TextView text_subHeading = findViewById(R.id.text_subHeading);
        TextView text_rightToHeading= findViewById(R.id.text_rightToHeading);
        View view = findViewById(R.id.view_activity);

    }
}
