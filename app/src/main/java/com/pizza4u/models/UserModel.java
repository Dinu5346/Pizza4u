package com.pizza4u.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String acctype;
    private String fname;
    private String lname;
    private String email;
    private int phone;
    private String password;
    private int branchid;
    private int employeeid;
    private String profilepic;
    private String userID;

    public UserModel() {};

    public UserModel(String acctype, String fname, String lname, String email, int phone, String password, int branchid, int employeeid, String profilepic, String userID) {
        this.acctype = acctype;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.branchid = branchid;
        this.employeeid = employeeid;
        this.profilepic = profilepic;
        this.userID = userID;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(int employeeid) {
        this.employeeid = employeeid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
