package com.inout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 08468 on 6/9/2016.
 */
public class PunchDetailsAdapter extends ArrayAdapter<Punch> {

    ArrayList<Punch> punches;

    public PunchDetailsAdapter(Context context, int resource, ArrayList<Punch> punches) {
        super(context, resource);
        this.punches = punches;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.punch_details_list_items, parent, false);
            viewHolder._no = (TextView) convertView.findViewById(R.id.no);
            viewHolder._Date = (TextView) convertView.findViewById(R.id.cDate);
            viewHolder._inTime = (TextView) convertView.findViewById(R.id.cInTime);
            viewHolder._outTime = (TextView) convertView.findViewById(R.id.cOutTime);
            viewHolder._Duration = (TextView) convertView.findViewById(R.id.cDuration);
            convertView.setTag(viewHolder);

        }
        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder._no.setText(punches.get(position).getNo());
        viewHolder._Date.setText(punches.get(position).getDate());
        viewHolder._inTime.setText(punches.get(position).getInTime());
        viewHolder._outTime.setText(punches.get(position).getOutTime() != null ? punches.get(position).getOutTime() : "");
        viewHolder._Duration.setText(punches.get(position).getDiffTime() != null ? punches.get(position).getDiffTime() : "");

        return convertView;
    }

    @Override
    public int getCount() {
        return punches != null ? punches.size() : 0;
    }


    private static class ViewHolder {
        TextView _no;
        TextView _Date;
        TextView _inTime;
        TextView _outTime;
        TextView _Duration;
    }
}
