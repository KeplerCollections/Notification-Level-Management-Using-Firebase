package com.kepler.notificationsystem.admin.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.support.Utils;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Amit on 08-04-2017.
 */

public class NavigationAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> objects;

    public NavigationAdapter(Context context, List<String> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder = null;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.drawer_list_view_items, null);
            viewHolder = new ViewHolder();
            viewHolder.drawer_title = (TextView) v.findViewById(R.id.drawer_title);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.drawer_title.setText(String.valueOf(getItem(position)));
        return v;
    }

    private class ViewHolder {
        TextView drawer_title;
    }
}
