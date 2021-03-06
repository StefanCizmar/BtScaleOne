package com.kiwi.btscaleone;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "TIMES".
 */
@Entity
public class Times implements java.io.Serializable {

    private static final long serialVersionUID = 222;

    @Id
    private Long id;
    private String type;
    private String times;
    private Integer elapsedTime;

    @Generated(hash = 1098255073)
    public Times() {
    }

    public Times(Long id) {
        this.id = id;
    }

    @Generated(hash = 977958496)
    public Times(Long id, String type, String times, Integer elapsedTime) {
        this.id = id;
        this.type = type;
        this.times = times;
        this.elapsedTime = elapsedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}
