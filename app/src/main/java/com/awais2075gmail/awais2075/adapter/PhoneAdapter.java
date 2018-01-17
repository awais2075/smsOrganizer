package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.model.Contact;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 03-Dec-17.
 */

public class PhoneAdapter extends RecyclerView.Adapter {
    private List<Contact> phoneList;
    private Context context;

    public PhoneAdapter(List<Contact> phoneList, Context context) {
        this.phoneList = phoneList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).bind(phoneList.get(position));

    }

    @Override
    public int getItemCount() {
        return (phoneList == null) ? 0 : phoneList.size();
    }

    public List<Contact> selectedContacts() {
        List<Contact> list = new ArrayList<>();
        for (int i=0; i<phoneList.size(); i++) {
            if (phoneList.get(i).isSelected()) {
                list.add(phoneList.get(i));
            }
        }
        return list;
    }

    public boolean selectAll() {
        for (int i=0; i<phoneList.size(); i++) {
            phoneList.get(i).setSelected(true);
        }
        notifyDataSetChanged();
        return true;
    }

    public boolean unSelectAll() {
        for (int i=0; i<phoneList.size(); i++) {
            phoneList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
        return true;
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CustomCheckBox check_phone;
        private TextView text_phoneName;
        private TextView text_phoneNumber;


        private MyHolder(View itemView) {
            super(itemView);

            text_phoneName = itemView.findViewById(R.id.text_phoneName);
            text_phoneNumber = itemView.findViewById(R.id.text_phoneNumber);
            check_phone = itemView.findViewById(R.id.check_phone);
            itemView.findViewById(R.id.phoneView).setOnClickListener(this);

        }

        private void bind(Contact contact) {
            text_phoneName.setText(contact.getContactName());
            text_phoneNumber.setText(contact.getContactNumber());
            check_phone.setTag(phoneList.get(getAdapterPosition()));
            check_phone.setOnCheckedChangeListener(null);
            //check_phone.setChecked(contact.isSelected(),true);
            check_phone.setClickable(false);

            //check_phone.setChecked(!check_phone.isChecked(), true);

        }

        @Override
        public void onClick(View v) {
            check_phone.setChecked(!check_phone.isChecked(), true);
            phoneList.get(getAdapterPosition()).setSelected(check_phone.isChecked());
            Toast.makeText(context, phoneList.get(getAdapterPosition()).getContactName()+"", Toast.LENGTH_SHORT).show();
        }
    }
}
