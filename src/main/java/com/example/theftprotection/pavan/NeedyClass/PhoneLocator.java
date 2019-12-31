package com.example.pavan.theftprotection.NeedyClass;

import java.util.Date;

public class PhoneLocator extends PhoneLoactorId{

    public String userId;

    public String PhoneLocatorId;

    public String Phone;
    public String lat;
    public String lng;
    public Date timestamp;
    public String email;
    public String deviceId;
    public PhoneLocator() {
    }

    public PhoneLocator(String userId, String phoneLocatorId, String phone, String lat, String lng, Date timestamp, String email, String deviceId) {
        this.userId = userId;
        PhoneLocatorId = phoneLocatorId;
        Phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.email = email;
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
