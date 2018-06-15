package com.awais2075gmail.awais2075.layout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.awais2075gmail.awais2075.R;

/**
 * Created by Muhammad Awais Rashi on 25-Jan-18.
 */

public class CustomActivityLayout extends ConstraintLayout {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public CustomActivityLayout(Context context) {
        super(context);
        init();
    }

    public CustomActivityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomActivityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.general_recyclerview_fab, this);
        this.toolbar = findViewById(R.id.toolbar);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.fab = findViewById(R.id.fab);
    }


}
