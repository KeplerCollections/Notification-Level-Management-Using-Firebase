package com.kepler.notificationsystem.student.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Push;
import com.kepler.notificationsystem.services.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 07-04-2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private final Context context;
    private List<Push> messages = new ArrayList<>();
    private List<Push> original_messages = new ArrayList<>();

    //    private StudentNameFilter studentNameFilter=new StudentNameFilter();
    public MessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(context);
        View view = layoutInflater.inflate(
                R.layout.message_item,
                parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Push message = messages.get(position);
        holder.MessageHolder(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addAll(List<Push> data) {
        original_messages.addAll(data);
        messages.addAll(data);
    }

    public void add(Push data) {
        original_messages.add(data);
        messages.add(data);
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private Push message;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id._message)
        TextView _message;
        @BindView(R.id.time_stamp)
        TextView time_stamp;
        @BindView(R.id._download)
        TextView _download;
        @BindView(R.id.holder)
        LinearLayout holder;
        @BindView(R.id.holder_bottom)
        LinearLayout holder_bottom;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void MessageHolder(final Push message) {
            this.message = message;
            holder.setBackgroundColor(Color.parseColor(getDarkColor(message.getMsg_type())));
            holder_bottom.setBackgroundColor(Color.parseColor(getLightColor(message.getMsg_type())));
            title.setText(message.getTitle());
            time_stamp.setText(message.getTimestamp());
            _message.setText(message.getMessage());
            if (message.getFile().trim().length() > 0) {
                _download.setVisibility(View.VISIBLE);
                _download.setText(message.getFile());
                _download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//                _message.setText(message.getMessage());
            }

        }
    }

//    public Filter getFilter() {
//        return studentNameFilter;
//    }


//    private class StudentNameFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            final String filterStreing = charSequence.toString().toLowerCase().trim();
//            FilterResults results = new FilterResults();
//            final ArrayList<Student> nlist = new ArrayList<>(original_students_data.size());
//            for (int i = 0; i < original_students_data.size(); i++) {
//                Student student = original_students_data.get(i);
//                if (student.getName().toLowerCase().contains(filterStreing)) {
//                    nlist.add(student);
//                }
//            }
//            results.values = nlist;
//            results.count = nlist.size();
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            students = (ArrayList<Student>) filterResults.values;
//            notifyDataSetChanged();
//        }
//    }

    private String getDarkColor(int msg_type) {
        switch (msg_type) {
            case Student.WARNING_TYPE:
                return "#EF9B0F";
            case Student.IMAPORTANT_TYPE:
                return "#990F02";
            default:
                return "#11772D";
        }
    }

    private String getLightColor(int msg_type) {
        switch (msg_type) {
            case Student.WARNING_TYPE:
                return "#FFBF00";
            case Student.IMAPORTANT_TYPE:
                return "#D0312D";
            default:
                return "#00BB27";
        }
    }
}