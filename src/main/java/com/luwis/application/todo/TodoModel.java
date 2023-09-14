package com.luwis.application.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Boolean status = false;
    private Long userid;

    protected TodoModel() {}

    public TodoModel(String title, String description, Long userid) {
        this.title = title;
        this.description = description;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return String.format("TodoModel[id=%d, title='%s', description='%s', status='%b', userId='%d']", id, title, description, status, userid);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getStatus() {
        return status;
    }

    public Long getUserid() {
        return userid;
    }    
    
}
