package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.model.SMS;

import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 21-Dec-17.
 */

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
        return (conversationList == null) ? 0 : conversationList.size();
    }


    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text_smsAddress;
        private TextView text_smsDate;
        private TextView text_smsBody;
        private View view_conversation;

        public MyHolder(View itemView) {
            super(itemView);
            init(itemView);

        }

        private void init(View itemView) {
            text_smsAddress = itemView.findViewById(R.id.text_smsAddress);
            text_smsBody = itemView.findViewById(R.id.text_smsBody);
            text_smsDate = itemView.findViewById(R.id.text_smsDate);
            itemView.findViewById(R.id.view_conversation).setOnClickListener(this);
        }

        public void bind(final SMS sms) {

            text_smsAddress.setText(sms.getSmsAddress());
            text_smsBody.setText(sms.getSmsBody());
            text_smsDate.setText(sms.getSmsDate());

            if (sms.getSmsReadState().equals("0")) {
                text_smsAddress.setTextColor(ContextCompat.getColor(context, R.color.button_googleSignIn));
                text_smsBody.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                text_smsDate.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null){

                conversationList.get(getAdapterPosition()).setSmsReadState("1");
                notifyItemChanged(getAdapterPosition());
                /*long smsId = conversationList.get(getAdapterPosition()).getSmsId();
                long smsThreadId = conversationList.get(getAdapterPosition()).getSmsThreadId();
                String smsNumber = conversationList.get(getAdapterPosition()).getSmsNumber();
                String smsAddress = conversationList.get(getAdapterPosition()).getSmsAddress();
                String smsBody = conversationList.get(getAdapterPosition()).getSmsBody();
                String smsDate = conversationList.get(getAdapterPosition()).getSmsDate();
                String smsReadState = conversationList.get(getAdapterPosition()).getSmsReadState();
                String smsType = conversationList.get(getAdapterPosition()).getSmsType();
*/
                listener.itemClicked(conversationList.get(getAdapterPosition()));
            }
        }
    }
}