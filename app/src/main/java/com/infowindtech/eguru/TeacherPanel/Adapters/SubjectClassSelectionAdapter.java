package com.infowindtech.eguru.TeacherPanel.Adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.infowindtech.eguru.TeacherPanel.Fragments.SubjectClassRegistration;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.databinding.SubjectclassSelectionItemBinding;

import java.util.ArrayList;

public class SubjectClassSelectionAdapter extends BaseAdapter {
    public ArrayList<UserType> mListenerList;
    Context mContext;
    SubjectclassSelectionItemBinding binding;
    SubjectClassRegistration Fragment;
    public ArrayList<String> List_class;
    public ArrayList<String> List_ID;
    public ArrayList<String> List_gender;
    public ArrayList<String> List_place;


    public SubjectClassSelectionAdapter(ArrayList<UserType> mListenerList, Context mContext, SubjectClassRegistration Fragment, ArrayList<String> List_class, ArrayList<String> List_gender, ArrayList<String> List_place, ArrayList<String> List_ID) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.Fragment = Fragment;
        this.List_class = List_class;
        this.List_gender = List_gender;
        this.List_place = List_place;
        this.List_ID=List_ID;
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
//        binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox cb = (CheckBox) view;
//                UserType country = (UserType) cb.getTag();
//                country.setSelected(cb.isChecked());
//                Fragment.Click();
//            }
//        });
        final UserType country = mListenerList.get(i);
        binding.specCheckbox.setChecked(country.isSelected());
        binding.specCheckbox.setTag(country);


        binding.specCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final UserType usertype = mListenerList.get(i);
                String expandedListText = usertype.getType();
                String expandedListTextID = "";
                expandedListTextID = usertype.getId();

                if (b) {
                    List_class.add(expandedListText);
                    List_ID.add(expandedListTextID);
                    Fragment.Click();

                    //List_gender.add(expandedListTextID);
                } else {
                    List_class.remove(expandedListText);
                    List_ID.remove(expandedListTextID);
                }
//                binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (usertype.isSelected()) {
//                            usertype.setSelected(false);
//                        } else
//                            usertype.setSelected(true);
//
//                        Fragment.Click();
//                    }
//                });
            }
        });
        return binding.getRoot();
    }

    public void list_clear() {
        List_class.clear();
        //List_gender.clear();
    }


}



