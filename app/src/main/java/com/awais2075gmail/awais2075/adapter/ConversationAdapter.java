package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.model.SMS;

import org.w3c.dom.Text;

import java.util.List;


public class ConversationAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<SMS> conversationList;
    private ItemClickListener listener;

    public ConversationAdapter(Context context, List<SMS> conversationList, ItemClickListener listener) {
        this.context = context;
        this.conversationList = conversationList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).bind(conversationList.get(position));

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //private View view;
        private TextView text_heading;
        private TextView text_rightToHeading;
        private TextView text_subHeading;

        public MyHolder(View itemView) {
            super(itemView);
            init(itemView);
            //this.view = itemView;
        }

        private void init(View view) {
            text_heading = view.findViewById(R.id.text_heading);
            text_rightToHeading = view.findViewById(R.id.text_rightToHeading);
            text_subHeading = view.findViewById(R.id.text_subHeading);

            view.findViewById(R.id.check_phone).setVisibility(View.GONE);
            view.findViewById(R.id.view_activity).setOnClickListener(this);
            view.findViewById(R.id.view_activity).setOnClickListener(this);
            view.findViewById(R.id.view_activity).setOnLongClickListener(this);


        }

        public void bind(final SMS sms) {

            text_heading.setText(sms.getSmsAddress());
            text_rightToHeading.setText(sms.getSmsDate());
            text_subHeading.setText(sms.getSmsBody());




/**/
            if (sms.getSmsReadState().equals("0")) {

                text_heading.setTextColor(ContextCompat.getColor(context, R.color.button_googleSignIn));
                text_subHeading.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                text_rightToHeading.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

            }

            if (sms.isSpam()) {
                text_heading.setTypeface(null, Typeface.BOLD);
            }
        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(conversationList.get(getAdapterPosition()), getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(conversationList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}