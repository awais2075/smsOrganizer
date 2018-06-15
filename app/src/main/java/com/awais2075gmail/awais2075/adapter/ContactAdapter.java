package com.awais2075gmail.awais2075.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter {
    private List<Contact> contactList;
    private ItemClickListener listener;

    public ContactAdapter(List<Contact> contactList, ItemClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).bind(contactList.get(position));

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View view;
        public MyHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bind(Contact contact) {
            ((TextView)view.findViewById(R.id.text_heading)).setText(contact.getContactName());
            ((TextView)view.findViewById(R.id.text_subHeading)).setText(contact.getContactNumber());

            view.findViewById(R.id.text_rightToHeading).setVisibility(View.GONE);
            view.findViewById(R.id.check_phone).setVisibility(View.GONE);
            view.findViewById(R.id.view_activity).setOnClickListener(this);
            view.findViewById(R.id.view_activity).setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(contactList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(contactList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}
