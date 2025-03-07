package com.techtravelcoder.alluniversityinformations.notes;

public class FriendsModel {
    String address,date,department,facebook,institution,name,notes,phone,key ;
    private boolean isExpanded;


    public FriendsModel(){
        this.isExpanded=false;
    }


    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "FriendsModel{" +
                "address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", department='" + department + '\'' +
                ", institution='" + institution + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
