package com.awais2075gmail.awais2075.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.AutoResponse;

import java.util.List;

public class AutoResponseAdapter extends RecyclerView.Adapter {
    private ItemClickListener listener;
    private List<AutoResponse> autoResponseList;

    public AutoResponseAdapter(ItemClickListener listener, List<AutoResponse> autoResponseList) {
        this.listener = listener;
        this.autoResponseList = autoResponseList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_response, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).bind(autoResponseList.get(position));
    }

    @Override
    public int getItemCount() {
        return autoResponseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private View view;
        public MyHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bind(AutoResponse autoResponse) {
            ((TextView)view.findViewById(R.id.text_heading)).setText(autoResponse.getAutoResponseName());
            ((TextView)view.findViewById(R.id.text_subHeading)).setText(autoResponse.getAutoResponseText());

            view.findViewById(R.id.text_rightToHeading).setVisibility(View.GONE);
            view.findViewById(R.id.check_phone).setVisibility(View.GONE);
            view.findViewById(R.id.view_activity).setOnClickListener(this);
            view.findViewById(R.id.view_activity).setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(autoResponseList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemClicked(autoResponseList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}
