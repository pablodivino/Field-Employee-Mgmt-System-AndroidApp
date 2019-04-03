package com.vigneet.macgray_v010;

/**
 * Created by Vigneet on 09-04-2016.
 */
public class TaskDetails {
    String taskId, companyName,customerName,address,area,city,contact,email,taskType,subType,scheduledDate,scheduledTime,description,startTime,endTime,comments,status,lat,lng;

    public TaskDetails( String taskId, String companyName, String customerName, String address, String area, String city, String contact, String email, String taskType, String subType, String scheduledDate, String scheduledTime, String description, String startTime, String endTime, String comments, String status, String lat,String lng) {
        this.lng = lng;
        this.taskId = taskId;
        this.companyName = companyName;
        this.customerName = customerName;
        this.address = address;
        this.area = area;
        this.city = city;
        this.contact = contact;
        this.email = email;
        this.taskType = taskType;
        this.subType = subType;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comments = comments;
        this.status = status;
        this.lat = lat;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
