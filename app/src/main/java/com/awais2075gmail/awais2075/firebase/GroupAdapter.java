package com.awais2075gmail.awais2075.firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;

import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 18-Jan-18.
 */

public class GroupAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Group> groupList;

    public GroupAdapter() {
    }

    public GroupAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).bind(groupList.get(position));

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        private TextView text_groupName;
        public MyHolder(View itemView) {
            super(itemView);

            text_groupName = itemView.findViewById(R.id.text_groupName);
        }

        public void bind(Group group) {
            text_groupName.setText(group.getGroupName());
        }
    }
}
