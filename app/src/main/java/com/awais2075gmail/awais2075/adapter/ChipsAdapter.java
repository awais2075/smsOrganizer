package com.awais2075gmail.awais2075.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.Contact;
import com.robertlevonyan.views.chip.Chip;

import java.util.List;

public class ChipsAdapter extends RecyclerView.Adapter {

    private ItemClickListener listener;
    private List<Contact> contactList;

    public ChipsAdapter(ItemClickListener listener, List<Contact> contactList) {
        this.listener = listener;
        this.contactList = contactList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chips, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private Chip chip;

        public MyHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView) {
            chip = itemView.findViewById(R.id.chips);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Contact contact) {
            chip.setChipText(contact.getContactName());
        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(contactList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemClicked(contactList.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }
}
