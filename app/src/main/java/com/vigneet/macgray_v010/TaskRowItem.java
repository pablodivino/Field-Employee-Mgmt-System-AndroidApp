package com.vigneet.macgray_v010;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class TaskRowItem {
    private String id;
    private String company;
    private String taskType;
    private String time;

    public TaskRowItem(String id,String company,String taskType,String time){
        this.id=id;
        this.company=company;
        this.taskType=taskType;
        this.time=time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
