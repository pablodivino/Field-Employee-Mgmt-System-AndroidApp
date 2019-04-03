package com.vigneet.macgray_v010;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * Created by Vigneet on 10-04-2016.
 */
public class ScheduleFragment extends Fragment implements CalendarView.OnDateChangeListener{
    View Schedule;
    DateChangeInteractions dateChangeInteractions;
    CalendarView calendarView;

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        String yr = Integer.toString(year);
        String mnth,dy;
        if(month<10){
            mnth = "0"+Integer.toString(month+1);
        }else {
            mnth = Integer.toString(month+1);
        }
        if(dayOfMonth<10){
            dy = "0"+Integer.toString(dayOfMonth);
        }else {
            dy = Integer.toString(dayOfMonth);
        }
        dateChangeInteractions.dateChanged(yr,mnth,dy);
    }

    public interface DateChangeInteractions {
        public void dateChanged(String year, String month, String date);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       Schedule = inflater.inflate(R.layout.schedule_fragment,container,false);
        return Schedule;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = (CalendarView) Schedule.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dateChangeInteractions= (DateChangeInteractions) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DateChangeInteractions");
        }
    }


}
