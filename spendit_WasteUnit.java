package kvi.spendit;

/**
 * Created by Admin on 29.01.2018.
 */

import java.time.Duration;

public class WasteUnit {
    private String group;
    private String note;
    private Duration time;

    public WasteUnit() {
        group = note = "";
        time = null;
    }

    public WasteUnit(String group, String note, Duration time) {
        this.group = group;
        this.note = note;
        this.time = time;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }
}
