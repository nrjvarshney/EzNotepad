package com.example.notepad;

/**
 * Created by neeraj.varshney on 6/10/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Belal on 7/22/2015.
 */
public class CustomList extends BaseAdapter {
    private Vector<String> names;
    private String[] desc;
    private Integer[] imageid;
    private Activity context;
    private ArrayList<ModelNotepad> listNotepad;

    public CustomList(Activity context, ArrayList<ModelNotepad> listNotepad) {
        this.context = context;
        this.listNotepad = listNotepad;
//
    }

//    public CustomList(Activity context, Vector<String> names, String[] desc, Integer[] imageid) {
//        super(context, R.layout.list_layout, names);
//        this.context = context;
//        this.names = names;
//        this.desc = desc;
//        this.imageid = imageid;
//        Log.e("desc size",desc.length+"");
//        Log.e("names size",names.size()+"");
//        Log.e("imageid size",imageid.length+"");
//    }


    @Override
    public int getCount() {
        return listNotepad.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotepad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

        textViewName.setText(listNotepad.get(position).getName());
        textViewDesc.setText(listNotepad.get(position).getDesc());
        image.setImageResource(listNotepad.get(position).getImg());
        return listViewItem;
    }
}