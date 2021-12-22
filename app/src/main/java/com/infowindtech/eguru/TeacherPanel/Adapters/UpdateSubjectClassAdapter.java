package com.infowindtech.eguru.TeacherPanel.Adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.infowindtech.eguru.StudentPanel.UpdateCommonActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.databinding.SubjectclassSelectionItemBinding;

import java.util.ArrayList;
import java.util.List;

public class UpdateSubjectClassAdapter extends BaseAdapter {
    private List<UserType> mListenerList;
    public ArrayList<UserType> arraylistListener;
    Context mContext;
    SubjectclassSelectionItemBinding binding;
    UpdateCommonActivity activity;
    public ArrayList<String> List_class;
    public ArrayList<String> List_class_ID;

    public UpdateSubjectClassAdapter(List<UserType> mListenerList, Context mContext, UpdateCommonActivity activity, ArrayList<String> List_class,ArrayList<String> List_class_ID) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.List_class = List_class;
        this.List_class_ID = List_class_ID;
        this.arraylistListener = new ArrayList<UserType>();
        this.arraylistListener.addAll(mListenerList);
        this.activity = activity;
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
        binding.tvChildName.setVisibility(View.GONE);
        binding.textViewIdSpinner.setText(mListenerList.get(i).getId());
        binding.specCheckbox.setText(mListenerList.get(i).getType());
        binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                UserType country = (UserType) cb.getTag();
                country.setSelected(cb.isChecked());
                activity.Click();
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

                final UserType usertypeID = mListenerList.get(i);
                String usertypeID_str = usertypeID.getId();

                if (b) {
                    List_class.add(expandedListText);
                    List_class_ID.add(usertypeID_str);
                } else {
                    List_class.remove(expandedListText);
                    List_class_ID.add(usertypeID_str);
                }
                binding.specCheckbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (usertype.isSelected()) {
                            usertype.setSelected(false);
                        } else
                            usertype.setSelected(true);

                        activity.Click();
                    }
                });
            }
        });


        return binding.getRoot();
    }

}

