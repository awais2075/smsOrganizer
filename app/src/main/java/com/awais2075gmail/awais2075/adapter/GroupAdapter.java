package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.Group;

import java.util.List;

/**
 * Created by Muhammad Awais Rashid on 25-Jan-18.
 */

public class GroupAdapter extends RecyclerView.Adapter {

    private List<Group> groupList;
    private ItemClickListener listener;

    public GroupAdapter(List<Group> groupList, ItemClickListener listener) {
        this.groupList = groupList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).bind(groupList.get(position));

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private View view;

        public MyHolder(View view) {
            super(view);
            this.view = view;
        }

        public void bind(Group group) {
            ((TextView)view.findViewById(R.id.text_heading)).setText(group.getGroupName());
            view.findViewById(R.id.text_subHeading).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.text_rightToHeading).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.check_phone).setVisibility(View.GONE);
            view.findViewById(R.id.view_activity).setOnClickListener(this);
            view.findViewById(R.id.view_activity).setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(groupList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(groupList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}