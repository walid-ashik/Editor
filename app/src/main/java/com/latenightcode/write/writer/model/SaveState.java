package com.latenightcode.write.writer.model;

import com.orm.SugarRecord;

public class SaveState extends SugarRecord {

    private String date;
    private String written;
    private String preferredTimeForAlarm;


    public SaveState() {
    }

    public SaveState(String date, String written, String preferredTimeForAlarm) {
        this.date = date;
        this.written = written;
        this.preferredTimeForAlarm = preferredTimeForAlarm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWritten() {
        return written;
    }

    public void setWritten(String written) {
        this.written = written;
    }

    public String getPreferredTimeForAlarm() {
        return preferredTimeForAlarm;
    }

    public void setPreferredTimeForAlarm(String preferredTimeForAlarm) {
        this.preferredTimeForAlarm = preferredTimeForAlarm;
    }
}
