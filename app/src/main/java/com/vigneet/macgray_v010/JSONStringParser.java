package com.vigneet.macgray_v010;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class JSONStringParser {
    JSONArray jArray = null;

    public JSONStringParser(String inputString){
        try {
            this.jArray = new JSONArray(inputString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<TaskRowItem> getTaskRowItemsList(){
        try{
        List<TaskRowItem> taskRowItemsList = new ArrayList<TaskRowItem>();
        JSONObject json_data=null;
            if (jArray!=null) {
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    String id = json_data.getString("id");
                    String company = json_data.getString("companyname");
                    String taskType = json_data.getString("tasktype");
                    String time = json_data.getString("time");
                    taskRowItemsList.add(new TaskRowItem(id, company, taskType, time));

                }
                return taskRowItemsList;
            }else {
                return null;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public TaskDetails getTaskDetails(){
        TaskDetails taskDetails = null;
        JSONObject json_data=null;
        try{
            json_data = jArray.getJSONObject(0);
            taskDetails = new TaskDetails(json_data.getString("id"),
                    json_data.getString("companyname"),json_data.getString("customername"),json_data.getString("address"),
                    json_data.getString("area"),json_data.getString("city"),json_data.getString("contact"),
                    json_data.getString("email"),json_data.getString("tasktype"),json_data.getString("subtype"),
                    json_data.getString("scheduleddate"),json_data.getString("scheduledtime"),json_data.getString("description"),
                    json_data.getString("starttime"),json_data.getString("endtime"),json_data.getString("comments"),
                    json_data.getString("status"),json_data.getString("lat"),json_data.getString("lng"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return taskDetails;
    }

    public Employee getEmployee(){
        Employee employee= null;
        JSONObject json_data=null;
        try{
            if(jArray!=null) {
                json_data = jArray.getJSONObject(0);
                employee = new Employee(json_data.getString("id"),json_data.getString("firstname"),json_data.getString("lastname"),json_data.getString("empId"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return employee;
    }

    public String getString(String key){
        String str = null;
        JSONObject json_data=null;
        try{
            if(jArray!=null) {
                json_data = jArray.getJSONObject(0);
                str = json_data.getString(key);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return str;
    }
}
