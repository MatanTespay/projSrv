package com.haifa.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haifa.utils.FilesUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Faina0502 on 21/01/2017.
 */
public class Volunteer {

    private int id;
    private String fName;
    private String lName;
    private Date birthDate;
    private String address;
    private String email;
    private String password;
    private byte[] profilePic;
    //private ArrayList<VolEvent> volEvents;

    public Volunteer(String fName, String lName, Date birthDate, String address, String email , String password,
    		byte[] profilePic){
    	this(0,  fName,  lName,  birthDate,  address,  email ,  password,
        		profilePic);

    }

    public Volunteer() {
    }

    public  Volunteer (int id , String fName, String lName, Date birthDate, String address, String email, 
    		String password, byte[] profilePic ){
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

    public Date getBirthDate() {
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

    public void setBirthDate(Date birthDate) {
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


    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
    /**
     * Create a json object from Org. object
     * @return
     */

    	public JSONObject toJson() {	
//    public  Volunteer (int id , String fName, String lName,
    		//String birthDate, String address, String email, String 
    		//password, byte[] profilePic ){

    		JSONObject iObj = new JSONObject();
    		iObj.put("volID", getId());
    		iObj.put("fname", getfName());
    		iObj.put("lname", getlName());
    		
    		iObj.put("birthDate", (getBirthDate() != null ) ? FilesUtils.getShortDateString(getBirthDate()) : "");
    		iObj.put("address", getAddress());
    		iObj.put("email", getEmail());
    		iObj.put("password", getPassword());    	
    		iObj.put("ProfilePic",  (isImageExists() ?  FilesUtils.blobToBase64String(getProfilePic()) : "" ) );
    		return iObj;
    	}
    	    	
/**
 * Checks if there is image saved in db.
 * @return true if exist false otherwise
 */
		private boolean isImageExists() {
    			if (profilePic == null || profilePic.length == 0) {
    				return false;
    			}
    			return true;
    		}
   
    /**
     * 
     * @param list
     * @return
     */
    	public static String toJson(List<Volunteer> list) {

    		JSONObject jsonObj = new JSONObject();

    		if (list == null) {
    			return null;
    		}

    		if (list.size() == 0) {
    			return null;
    		}

    		JSONArray jsonArray = new JSONArray();

    		for (Volunteer vol : list) {

    			if (vol != null) {

    				JSONObject itemObj = vol.toJson();

    				jsonArray.add(itemObj);
    			}

    		}
    		jsonObj.put("volunteers", jsonArray);

    		return jsonObj.toString(2);
    	}

}
