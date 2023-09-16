package com.luwis.application.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private boolean status = false;
    private long userid;

    protected TodoModel() {}

    public TodoModel(String title, String description, long userid) {
        this.title = title;
        this.description = description;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return String.format("TodoModel[id=%d, title='%s', description='%s', status='%b', userId='%d']", id, title, description, status, userid);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getStatus() {
        return status;
    }

    public long getUserid() {
        return userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
