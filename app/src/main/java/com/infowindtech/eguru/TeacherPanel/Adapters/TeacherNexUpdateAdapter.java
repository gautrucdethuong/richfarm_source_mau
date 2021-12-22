package com.infowindtech.eguru.TeacherPanel.Adapters;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Fragments.TeacherNextUpdate_Fragment;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.databinding.SubjectclassSelectionItemBinding;

import java.util.ArrayList;
import java.util.List;

public class TeacherNexUpdateAdapter extends BaseAdapter {
    private List<UserType> mListenerList;
    public ArrayList<UserType> arraylistListener;
    Context mContext;
    SubjectclassSelectionItemBinding binding;
    TeacherNextUpdate_Fragment Fragment;
    public ArrayList<String> List_class;
    int numberOfCheckboxesChecked = 0;
    int checkAccumulator;

    public TeacherNexUpdateAdapter(List<UserType> mListenerList, Context mContext,TeacherNextUpdate_Fragment Fragment,ArrayList<String> List_class) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;

        this.Fragment=Fragment;
        this.List_class=List_class;
        checkAccumulator = 0;
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
                    R.layout.subjectclass_selection_item, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SubjectclassSelectionItemBinding) view.getTag();
        }
        binding.textViewIdSpinner.setText(mListenerList.get(i).getId());
        binding.specCheckbox.setText(mListenerList.get(i).getType());
        binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view ;
                UserType country = (UserType) cb.getTag();
                country.setSelected(cb.isChecked());
                Fragment.Click();

            }
        });
        UserType country = mListenerList.get(i);
        binding.specCheckbox.setChecked(country.isSelected());
        binding.specCheckbox.setTag(country);

        binding.specCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final UserType usertype = mListenerList.get(i);
                String expandedListText = usertype.getType();
                Log.d("getdatecount",String.valueOf(numberOfCheckboxesChecked));
                countCheck(b);

                if (b && numberOfCheckboxesChecked >=2) {
                    Toast.makeText(mContext,"please select any One Place",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (b) {
                        List_class.add(expandedListText);

                    } else {
                        List_class.remove(expandedListText);
                    }}

                binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (usertype.isSelected()) {
                        usertype.setSelected(false);
                    } else
                        usertype.setSelected(true);

                    Fragment.Click();
                }
            });

        };
    });
    return binding.getRoot();
    }

    private void countCheck(boolean isChecked) {

        checkAccumulator += isChecked ? 1 : -1 ;
    }
    public void claerList(){
        List_class.clear();
    }
}








