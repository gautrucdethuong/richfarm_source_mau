package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Models.WalletListModel;
import com.infowindtech.eguru.databinding.TransactionListitemBinding;
import com.infowindtech.eguru.databinding.WalletListitemBinding;

import java.util.List;

public class EarningListAdapter extends BaseAdapter {
    private List<WalletListModel> mListenerList;
    Context mContext;
    TransactionListitemBinding binding;
    String strCurrency="";

    public EarningListAdapter(List<WalletListModel> mListenerList, Context mContext) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListenerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mListenerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.transaction_listitem, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (TransactionListitemBinding) view.getTag();
        }
        String currency = mListenerList.get(i).getCurrency();
        if(currency.equals("USD"))
        {
            strCurrency = "$";
        }
        else if(currency.equals("INR")) {
            strCurrency = "Rs.";
        }


        binding.nameValue.setText(mListenerList.get(i).getName());
        binding.amountValue.setText(strCurrency+" "+mListenerList.get(i).getAmount());
        binding.idValue.setText(mListenerList.get(i).getWithdrawal_id());
        binding.statusValue.setText(mListenerList.get(i).getStatus());
        binding.createdValue.setText(mListenerList.get(i).getCreated_at());
        binding.methodValue.setText(mListenerList.get(i).getSelected_method());
        binding.transactionValue.setText(mListenerList.get(i).getTransaction());


        return binding.getRoot();
    }
}
