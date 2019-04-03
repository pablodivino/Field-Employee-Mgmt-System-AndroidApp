package com.vigneet.macgray_v010;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class TaskListAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;
    List<TaskRowItem> rowItems;

    public TaskListAdapter(Context context,List<TaskRowItem> rowItems){
        this.context = context;
        this.rowItems = rowItems;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (!rowItems.isEmpty()) {
            return rowItems.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    public class Holder
    {
        ImageView imageView;
        TextView companyNameTV;
        TextView taskTypeTV;
        TextView timeTV;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        rowView = inflater.inflate(R.layout.task_list_row, null);

        holder.imageView = (ImageView) rowView.findViewById(R.id.textimage);
        holder.companyNameTV =(TextView) rowView.findViewById(R.id.companyDetails);
        holder.taskTypeTV =(TextView) rowView.findViewById(R.id.taskType);
        holder.timeTV =(TextView) rowView.findViewById(R.id.time);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(rowItems.get(position).getTaskType().trim().toUpperCase());
        String ttC = rowItems.get(position).getTaskType().trim().toUpperCase().charAt(0)+"";
        holder.imageView.setImageDrawable(TextDrawable.builder().buildRound(ttC, color));
        holder.companyNameTV.setText(rowItems.get(position).getCompany());
        holder.taskTypeTV.setText(rowItems.get(position).getTaskType());
        holder.timeTV.setText(rowItems.get(position).getTime());

        return rowView;
    }
}
