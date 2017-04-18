package com.kepler.notificationsystem.dao;

/**
 * Created by Amit on 17-04-2017.
 */

public class Push {
    private int id;
    private String image;
    private boolean is_background;
    private String file;
    private int msg_type;
    private String title;
    private String message;
    private String timestamp;
    private String topic;
    private String topic_name;

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean is_background() {
        return is_background;
    }

    public void setIs_background(boolean is_background) {
        this.is_background = is_background;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
