package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.util.Constants;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 04-Jan-18.
 */

public class MessageAdapter extends RecyclerView.Adapter{
    private List<SMS> messageList;
    private ItemClickListener listener;

    public MessageAdapter() {
    }

    public MessageAdapter(List<SMS> messageList, ItemClickListener listener) {
        this.messageList = messageList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.VIEW_TYPE_MESSAGE_RECEIVED:
                return new ReceiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_message, parent, false));

            case Constants.VIEW_TYPE_MESSAGE_SENT:
                return new SendHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Constants.VIEW_TYPE_MESSAGE_SENT:
                ((SendHolder) holder).bind(messageList.get(position));
                break;

            case Constants.VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveHolder) holder).bind(messageList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (messageList == null) ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSmsType().equals("Inbox")) {
            return Constants.VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            return Constants.VIEW_TYPE_MESSAGE_SENT;
        }
    }

    private class SendHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView text_sendSmsBody;
        private TextView text_sendSmsDate;
        private View view_send;


        public SendHolder(View itemView) {
            super(itemView);
            text_sendSmsBody = itemView.findViewById(R.id.text_sendSmsBody);
            text_sendSmsDate = itemView.findViewById(R.id.text_sendSmsDate);
            view_send = itemView.findViewById(R.id.view_send);
            view_send.setOnLongClickListener(this);
        }

        private void bind(SMS sms) {
            text_sendSmsBody.setText(sms.getSmsBody());
            text_sendSmsDate.setText(sms.getSmsDate());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(messageList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }

    private class ReceiveHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView text_receiveSmsBody;
        private TextView text_receiveSmsDate;
        private View view_receive;

        public ReceiveHolder(View itemView) {
            super(itemView);
            text_receiveSmsBody = itemView.findViewById(R.id.text_receiveSmsBody);
            text_receiveSmsDate = itemView.findViewById(R.id.text_receiveSmsDate);
            view_receive = itemView.findViewById(R.id.view_receive);
            view_receive.setOnLongClickListener(this);
        }

        private void bind(SMS sms) {
            text_receiveSmsBody.setText(sms.getSmsBody());
            text_receiveSmsDate.setText(sms.getSmsDate());

        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(messageList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}
