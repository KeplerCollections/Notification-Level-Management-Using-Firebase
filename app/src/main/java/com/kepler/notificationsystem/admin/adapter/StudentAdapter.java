package com.kepler.notificationsystem.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.support.OnLoadMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 07-04-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {
    private final Context context;
    private final OnLoadMore onLoadMore;
    private List<Student> students = new ArrayList<>();

    public StudentAdapter(Context context, OnLoadMore onLoadMore) {
        this.context = context;
        this.onLoadMore = onLoadMore;
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
        students.addAll(data);
        notifyDataSetChanged();
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

        public StudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindStudent(Student student) {
            this.student = student;
//            titleTextView.setText(students.getTitle());
            _name.setText(student.getName());
            _emailid.setText(student.getEmailid());
            _more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            _more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            if (students.size() % com.kepler.notificationsystem.services.Student.OFFSET == 0
                    && getAdapterPosition() == students.size() - 1) {
                onLoadMore.refresh();
                // Get more items

            }
        }
    }
}