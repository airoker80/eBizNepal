package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.ContactsModel;
import com.paybyonline.ebiz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sameer on 8/31/2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<ContactsModel> {
    List<ContactsModel> autoCompleteModelList;
    List<ContactsModel> tempCustomer, suggestions;

    public AutoCompleteAdapter(Context context, List<ContactsModel> autoCompleteModelList) {
        super(context, android.R.layout.simple_list_item_1, autoCompleteModelList);
        this.autoCompleteModelList = autoCompleteModelList;
        this.tempCustomer = new ArrayList<>(autoCompleteModelList);
        this.suggestions = new ArrayList<>(autoCompleteModelList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ContactsModel autoCompleteModel = autoCompleteModelList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_list_items, parent, false);
        }
        TextView userNo = (TextView) convertView.findViewById(R.id.userNo);
        TextView accNo = (TextView) convertView.findViewById(R.id.accNo);

//        userImg.setImageResource(autoCompleteModel.getImageId());
        userNo.setText(autoCompleteModel.getContactPhone());
        accNo.setText(autoCompleteModel.getContactName());
//        return super.getView(position, convertView, parent);
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ContactsModel customer = (ContactsModel) resultValue;
            return customer.getContactPhone();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ContactsModel people : tempCustomer) {
                    if (people.getContactPhone().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ContactsModel> c = (List<ContactsModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ContactsModel cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
