package com.example.notepad;

/**
 * Created by neeraj.varshney on 6/13/2016.
 */
public class Contact {
    int _id;
    String _name;
    //String _phone_number;
    String _photo;
    String _contents;
    String _gallery_image;
    String _captured_image;
    String _audios;
    int _delelte_var=0;
    int _recycle_delete=0;
    public Contact(){   }
    public Contact(int id, String name,String _audios,String _captured_image,String _contents, int _delelte_var, String _gallery_image, int _recycle_delete,String _photo ){
        this._id = id;
        this._name = name;
        this._contents=_contents;
        this._audios=_audios;
        this._photo=_photo;
        this._gallery_image=_gallery_image;
        this._captured_image=_captured_image;

    }

    public Contact(String name,String _photo ,String _contents, String _gallery_image,String _captured_image,String _audios){
        this._name = name;
        this._contents=_contents;
        this._audios=_audios;
        this._photo=_photo;
        this._gallery_image=_gallery_image;
        this._captured_image=_captured_image;
    }

    public String get_contents() {
        return _contents;
    }

    public void set_contents(String _contents) {
        this._contents = _contents;
    }

    public String get_audios() {
        return _audios;
    }

    public void set_audios(String _audios) {
        this._audios = _audios;
    }

    public String get_captured_image() {
        return _captured_image;
    }

    public void set_captured_image(String _captured_image) {
        this._captured_image = _captured_image;
    }

    public int get_delelte_var() {
        return _delelte_var;
    }

    public void set_delelte_var(int _delelte_var) {
        this._delelte_var = _delelte_var;
    }

    public String get_gallery_image() {
        return _gallery_image;
    }

    public void set_gallery_image(String _gallery_image) {
        this._gallery_image = _gallery_image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_photo() {
        return _photo;
    }

    public void set_photo(String _photo) {
        this._photo = _photo;
    }

    public int get_recycle_delete() {
        return _recycle_delete;
    }

    public void set_recycle_delete(int _recycle_delete) {
        this._recycle_delete = _recycle_delete;
    }
}