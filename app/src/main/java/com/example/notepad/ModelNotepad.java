package com.example.notepad;

/**
 * Created by neeraj.varshney on 6/15/2016.
 */
public class ModelNotepad {
    //
    private int id;
    //
    private String name;
    private String desc;
    private int img;

    public ModelNotepad(int id,String name, String desc, int img) {
        this.id=id;
        this.name = name;
        this.desc = desc;
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
