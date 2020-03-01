package com.dayakar.mgitian.Data;

import androidx.annotation.Keep;

@Keep
public class NotificationData {
    String title,desc, time;

    public NotificationData() {
    }

    public NotificationData(String title, String desc, String time) {
        this.title = title;
        this.desc = desc;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
