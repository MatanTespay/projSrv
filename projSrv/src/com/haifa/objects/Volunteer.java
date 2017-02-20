package com.haifa.objects;

import java.util.ArrayList;

/**
 * Created by Faina0502 on 21/01/2017.
 */
public class Volunteer {

    private int id;
    private String fName;
    private String lName;
    private String birthDate;
    private String address;
    private String email;
    private String password;
    private byte[] profilePic;
    //private ArrayList<VolEvent> volEvents;

    public Volunteer(String fName, String lName, String birthDate, String address, String email , String password,byte[] profilePic){
        this.fName = fName;
        this.lName =lName;
        this.birthDate =birthDate;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePic =profilePic;
        //this.volEvents=new ArrayList<>();
    }

    public Volunteer() {
    }

    public  Volunteer (int id , String fName, String lName, String birthDate, String address, String email, String password, byte[] profilePic ){
        this.id =id;
        this.fName = fName;
        this.lName =lName;
        this.birthDate =birthDate;
        this.address = address;

        this.email = email;
        this.profilePic = profilePic;
        this.password = password;
        //this.volEvents=new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

/*    public ArrayList<VolEvent> getVolEvents() {
        return volEvents;
    }

    public void setVolEvents(ArrayList<VolEvent> volEvents) {
        this.volEvents = volEvents;
    }*/

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}
