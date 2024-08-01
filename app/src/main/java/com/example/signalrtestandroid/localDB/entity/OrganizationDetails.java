package com.example.signalrtestandroid.localDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "organizationDetails")
public class OrganizationDetails {
    @PrimaryKey(autoGenerate = true)
    public int colum_Id;

    @ColumnInfo(name = "id")
    public int id = 0;
    @ColumnInfo(name = "orgName")
    public String orgName;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "displayname")
    public String displayname;

    public int getColum_Id() {
        return colum_Id;
    }

    public void setColum_Id(int colum_Id) {
        this.colum_Id = colum_Id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
