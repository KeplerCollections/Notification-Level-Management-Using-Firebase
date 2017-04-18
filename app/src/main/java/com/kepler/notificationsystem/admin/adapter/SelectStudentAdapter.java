package com.kepler.notificationsystem.admin.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kepler.notificationsystem.R;
import com.kepler.notificationsystem.dao.Student;
import com.kepler.notificationsystem.support.OnViewActionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 07-04-2017.
 */

public class SelectStudentAdapter extends RecyclerView.Adapter<SelectStudentAdapter.StudentHolder> {
    private final Context context;
    private HashSet<String> selected = new HashSet<>();
    private List<Student> students = new ArrayList<>();
    private List<Student> original_students_data = new ArrayList<>();
    private StudentNameFilter studentNameFilter = new StudentNameFilter();

    public SelectStudentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(context);
        View view = layoutInflater.inflate(
                R.layout.select_student_items,
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

    public void addAll(List<Student> data) {
        original_students_data.addAll(data);
        students.addAll(data);
        notifyDataSetChanged();
    }

    public String getSelected() {
        return TextUtils.join(",", selected);
    }

    class StudentHolder extends RecyclerView.ViewHolder {
        private Student student;
        @BindView(R.id.chb_tv)
        CheckedTextView chb_tv;

        public StudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindStudent(final Student student) {
            this.student = student;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                chb_tv.setText(Html.fromHtml("<big>" + student.getName() + "</big><br>&nbsp<font color='#aaa'>" + student.getEmailid() + "</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                chb_tv.setText(Html.fromHtml("<big>" + student.getName() + "</big><br>&nbsp<font color='#aaa'>" + student.getEmailid() + "</font>"));
            }
            chb_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chb_tv.isChecked()) {
                        chb_tv.setChecked(false);
                        if (student.getReg_id().trim().length() != 0)
                            selected.add(student.getId());
                        notifyDataSetChanged();
                    } else {
                        chb_tv.setChecked(true);
                        if (student.getReg_id().trim().length() != 0)
                            selected.remove(student.getId());
                        notifyDataSetChanged();
                    }
                }
            });
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