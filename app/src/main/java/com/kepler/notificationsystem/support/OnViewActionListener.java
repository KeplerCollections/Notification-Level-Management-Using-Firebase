package com.kepler.notificationsystem.support;

import com.kepler.notificationsystem.dao.Student;

/**
 * Created by Amit on 08-04-2017.
 */

public interface OnViewActionListener {
    public void refresh();
    public void onProfileBtnClicked(Student student);
    public void onSendMessageBtnClicked(Student student);
}

