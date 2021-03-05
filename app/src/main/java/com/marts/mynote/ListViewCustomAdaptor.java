package com.marts.mynote;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewCustomAdaptor extends BaseAdapter {
    private ArrayList<TaskInfo> itemList;
    private LayoutInflater inflater;
    private OnClickInMyAdapterListener2  myActivityInterface;
public Activity context;
    public ListViewCustomAdaptor(Activity context, ArrayList<TaskInfo> itemList, OnClickInMyAdapterListener2  myActivityInterface) {
        super();
        this.myActivityInterface= myActivityInterface;
        this.itemList = itemList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return itemList.size();
    }

    public Object getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_row, null);
            holder.textViewurl = (TextView) convertView.findViewById(R.id.textViewurl);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskInfo task = itemList.get(position);
        holder.textViewurl.setText(task.getTName());
        holder.textViewTime.setText(task.tDate);
        Button remvbtn= (Button)convertView.findViewById(R.id.remvBM);

        remvbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Log.d("his", "rembtnBM"+position);
                myActivityInterface.removeTaskOnClick2(position);
               // ((MainActivity)context).removetask();

            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView textViewurl;
        TextView textViewTime;

    }
}
