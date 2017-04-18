package com.kepler.notificationsystem.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.support.CircleTransform;
import com.kepler.notificationsystem.support.OnViewActionListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 07-04-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {
    private final Context context;
    private final OnViewActionListener onViewActionListener;
    private List<Student> students = new ArrayList<>();
    private List<Student> original_students_data = new ArrayList<>();
    private StudentNameFilter studentNameFilter=new StudentNameFilter();
    public StudentAdapter(Context context, OnViewActionListener onViewActionListener) {
        this.context = context;
        this.onViewActionListener = onViewActionListener;
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(context);
        View view = layoutInflater.inflate(
                R.layout.student_item_view,
                parent, false);
        return new StudentHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
        Student student = students.get(position);
        holder.bindStudent(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void addAll(List<com.kepler.notificationsystem.dao.Student> data) {
        original_students_data.addAll(data);
        students.addAll(data);
    }

    class StudentHolder extends RecyclerView.ViewHolder {
        private Student student;
        @BindView(R.id._name)
        TextView _name;
        @BindView(R.id._emailid)
        TextView _emailid;
        @BindView(R.id._more)
        ImageButton _more;
        @BindView(R.id._profile)
        ImageButton _profile;
        @BindView(R.id.profile_pic)
        ImageView profile_pic;

        public StudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindStudent(final Student student) {
            this.student = student;
//            titleTextView.setText(students.getTitle());
            _name.setText(student.getName());
            _emailid.setText(student.getEmailid());
            Picasso.with(context).load(student.getImg()).resize(100,100).placeholder(R.drawable.acc).
                    into(profile_pic);
            _profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewActionListener.onProfileBtnClicked(student);
                }
            });
            _more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewActionListener.onSendMessageBtnClicked(student);
                }
            });
            if (students.size() % com.kepler.notificationsystem.services.Student.OFFSET == 0
                    && getAdapterPosition() == students.size() - 1) {
                onViewActionListener.refresh();
                // Get more items

            }
        }
    }

    public Filter getFilter() {
        return studentNameFilter;
    }


    private class StudentNameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final String filterStreing = charSequence.toString().toLowerCase().trim();
            FilterResults results = new FilterResults();
            final ArrayList<Student> nlist = new ArrayList<>(original_students_data.size());
            for (int i = 0; i < original_students_data.size(); i++) {
                Student student = original_students_data.get(i);
                if (student.getName().toLowerCase().contains(filterStreing)) {
                    nlist.add(student);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            students = (ArrayList<Student>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}