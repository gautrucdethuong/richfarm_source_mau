package com.infowindtech.eguru.TeacherPanel.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.StudentPanel.Model.ExpandedSubjectData;
import com.infowindtech.eguru.TeacherPanel.Fragments.SubjectClassRegistration;
import com.infowindtech.eguru.TeacherPanel.Models.UserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subject_Registration_ExpandAdapter extends BaseExpandableListAdapter {
    private List<ExpandedSubjectData> groupitem;
    Context mContext;
    ImageView down, up;
    private ArrayList<UserType> arrayList;
    SubjectClassRegistration fragment;
    CheckBox checkBox;
    HashMap<String, String> mCheckBoxData;

    private final Set<Pair<Long, Long>> mCheckedItems = new HashSet<Pair<Long, Long>>();

    public Subject_Registration_ExpandAdapter(List<ExpandedSubjectData> groupitem, Context mContext, SubjectClassRegistration fragment, HashMap<String, String> checkBoxData_1) {
        this.groupitem = groupitem;
        this.mContext = mContext;
        this.fragment = fragment;
        this.mCheckBoxData = checkBoxData_1;
    }

    @Override
    public int getGroupCount() {
        return groupitem.size();
    }

    @Override
    public int getChildrenCount(int i) {
        ArrayList<UserType> productList = groupitem.get(i).getChildList();
        return productList.size();
    }

    @Override
    public Object getGroup(int i) {
        return groupitem.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        ArrayList<UserType> productList = groupitem.get(i).getChildList();
        return productList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ExpandedSubjectData usertype = groupitem.get(i);
        String listTitle = usertype.getName();
        String image = usertype.getImage();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.expaned_list_groupview, null);
        }
        TextView listTitleTextView = view
                .findViewById(R.id.tv_group_name);
        ImageView subimage = view
                .findViewById(R.id.im_subject);
        down = view.findViewById(R.id.im_down);
        up = view.findViewById(R.id.im_up);
        listTitleTextView.setText(listTitle);
        Glide.with(mContext).load(image)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(subimage);
        if (b) {
            down.setVisibility(View.INVISIBLE);
            up.setVisibility(View.VISIBLE);
        } else {
            down.setVisibility(View.VISIBLE);
            up.setVisibility(View.INVISIBLE);
        }
        if (getChildrenCount(i) == 0) {
            down.setVisibility(View.GONE);
            up.setVisibility(View.GONE);
        }
        arrayList = new ArrayList<>();
        arrayList.addAll(usertype.getChildList());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final UserType usertype = (UserType) getChild(i, i1);
        final String expandedListText = usertype.getType();

        final String expandedListTextid = usertype.getId();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.subjectclass_selection_item, null);
        }
        final CheckBox checkBox = view.findViewById(R.id.spec_checkbox);
        checkBox.setText(expandedListText);

        UserType country = (UserType) getChild(i, i1);
        checkBox.setChecked(country.isSelected());
        checkBox.setTag(country);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("setVisiblendfjb", expandedListText + "  " + expandedListTextid);

                    mCheckBoxData.put(expandedListText, expandedListTextid);
                } else {
                    mCheckBoxData.remove(expandedListText);

                }
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usertype.isSelected()) {
                    usertype.setSelected(false);
                } else
                    usertype.setSelected(true);

                fragment.Click();
            }
        });
        return view;
    }

    public HashMap<String, String> getCheckBoxData() {
        Log.d("mdngndfgj", String.valueOf(mCheckBoxData.size()));
        return mCheckBoxData;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }
}

