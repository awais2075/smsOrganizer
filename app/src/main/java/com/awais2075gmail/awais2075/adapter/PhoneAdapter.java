package com.awais2075gmail.awais2075.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.model.Contact;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter implements Filterable{
    private List<Contact> phoneList;
    private List<Contact> phoneListFiltered;
    private ItemClickListener listener;
    private Context context;

    public PhoneAdapter(Context context, List<Contact> phoneList, ItemClickListener listener) {
        this.context = context;
        this.phoneList = phoneList;
        this.phoneListFiltered = phoneList;
        this.listener = listener;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    phoneList = phoneListFiltered;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact contact: phoneListFiltered) {
                        if (contact.getContactName().toLowerCase().contains(charString.toLowerCase()) || contact.getContactNumber().contains(charSequence)) {
                            filteredList.add(contact);
                        }
                    }

                    phoneList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = phoneList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                phoneList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CustomCheckBox check_phone;
        private TextView text_phoneName;
        private TextView text_phoneNumber;


        private MyHolder(View itemView) {
            super(itemView);
            //this.setIsRecyclable(false);

            text_phoneName = itemView.findViewById(R.id.text_phoneName);
            text_phoneNumber = itemView.findViewById(R.id.text_phoneNumber);
            check_phone = itemView.findViewById(R.id.check_phone);
            itemView.findViewById(R.id.phoneView).setOnClickListener(this);

        }

        private void bind(final Contact contact) {
            text_phoneName.setText(contact.getContactName());
            text_phoneNumber.setText(contact.getContactNumber());
            check_phone.setTag(phoneList.get(getAdapterPosition()));
            check_phone.setOnCheckedChangeListener(null);
            check_phone.setChecked(contact.isSelected());
            check_phone.setClickable(false);

            //check_phone.setChecked(!check_phone.isChecked(), true);

        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(phoneList.get(getAdapterPosition()));
            check_phone.setChecked(!check_phone.isChecked(), true);
            phoneList.get(getAdapterPosition()).setSelected(check_phone.isChecked());
//            Toast.makeText(context, phoneList.get(getAdapterPosition()).getContactName()+"", Toast.LENGTH_SHORT).show();


        }
    }

}
