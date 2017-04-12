package com.kepler.notificationsystem.admin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.support.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private List<Integer> _listDataHeader = new ArrayList<>(); // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<Student>> _listDataChild = new HashMap<>();
    private HashMap<Integer, List<Boolean>> _listDataChildSelection = new HashMap<>();


    public ExpandableListAdapter(Context context) {
        this._context = context;
    }

    @Override
    public Student getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Student student = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_child, null);
        }

        final CheckedTextView txtListChild = (CheckedTextView) convertView
                .findViewById(R.id.tv_child);

        txtListChild.setText(student.getName() + "\n" + student.getEmailid());
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtListChild.isChecked())
                    txtListChild.setChecked(false);
                else
                    txtListChild.setChecked(true);

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Integer getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final int headerTitle = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_group);
        final CheckBox cb_group = (CheckBox) convertView
                .findViewById(R.id.cb_group);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(String.valueOf(headerTitle));
        if (getTrueValuesCount(headerTitle) == getChildrenCount(groupPosition)) {
            cb_group.setChecked(true);
        } else {
            cb_group.setChecked(false);
        }
        cb_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_group.isChecked()) {
                    Collections.fill(_listDataChildSelection.get(headerTitle),true);
                    notifyDataSetChanged();
                } else {
                    Collections.fill(_listDataChildSelection.get(headerTitle),false);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    public int getTrueValuesCount(int headerTitle) {
        int count = 0;
        List<Boolean> sparseBooleanArray = _listDataChildSelection.get(headerTitle);
        for (Boolean b : sparseBooleanArray) {
            if (b) count++;
        }
        return count;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addHeader(List<Student> data) {
        _listDataHeader.clear();
        _listDataChild.clear();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year - Utils.BEFORE_CRT_YEAR; i <= year + Utils.AFTER_CRT_YEAR; i++) {
            List<Student> students = StudentBranchFilter.performFiltering(String.valueOf(i), data);
            if (!students.isEmpty()) {
                _listDataHeader.add(i);
                _listDataChild.put(i, students);
                _listDataChildSelection.put(i, new ArrayList<Boolean>(students.size()));
            }
        }
    }


    private static class StudentBranchFilter {
        public static List<Student> performFiltering(CharSequence charSequence, List<Student> data) {
            final String filterStreing = charSequence.toString().toLowerCase().trim();
            final ArrayList<Student> nlist = new ArrayList<>(data.size());
            for (int i = 0; i < data.size(); i++) {
                Student student = data.get(i);
                if (student.getBatch().toLowerCase().equals(filterStreing)) {
                    nlist.add(student);
                }
            }
            return nlist;
        }
    }
}