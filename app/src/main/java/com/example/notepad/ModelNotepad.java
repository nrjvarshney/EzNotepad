package com.example.notepad;

/**
 * Created by neeraj.varshney on 6/15/2016.
 */
public class ModelNotepad {
    private String name;
    private String desc;
    private int img;

    public ModelNotepad(String name, String desc, int img) {
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
}
