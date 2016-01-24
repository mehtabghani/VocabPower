package com.bathem.vocabpower.Entity;

/**
 * Created by mehtab on 1/25/16.
 */
public class Category {

    private long id;
    private String catgoryName;

    public Category(String name) {

        this.catgoryName = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCatgoryName() {
        return catgoryName;
    }

    public void setCatgoryName(String catgoryName) {
        this.catgoryName = catgoryName;
    }
}
