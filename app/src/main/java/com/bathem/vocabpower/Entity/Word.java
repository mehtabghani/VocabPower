package com.bathem.vocabpower.Entity;

import java.util.Date;

/**
 * Created by mehtab on 1/30/16.
 */
public class Word {

    private int id;
    private String word;
    private Date createAt;
    private int typeID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Override
    public boolean equals(Object o) {
       Integer i =  (Integer)o;

        if(Integer.valueOf(this.id) == i)
            return true;

        return super.equals(o);
    }
}
