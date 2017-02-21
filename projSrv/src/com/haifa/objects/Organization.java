package com.haifa.objects;

import java.util.List;

import com.haifa.utils.FilesUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Organization {

    private int id;
    private String name;
    private String address;
    private String email;
    private String password;
    private byte[] profilePic;

    public Organization(String name, String address,byte[] profilePic){
        this(0,  name, address,  "",  "",  profilePic);
    }

    public Organization(String name, String address, String email , String password,byte[] profilePic){
        this(0,  name, address,  email,  password,  profilePic);
    }

    public Organization(int id , String name, String address, 
    		String email, String password, byte[] profilePic){
        this.id =id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePic =profilePic;
    }

    public Organization() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }
/**
 * Create a json object from Org. object
 * @return
 */
	public JSONObject toJson() {
		//(int id , String name, String address, 
		//String email, String password, byte[] profilePic)

		JSONObject iObj = new JSONObject();
		iObj.put("id", getId());
		iObj.put("title", getName());
		iObj.put("Address", getAddress());
		iObj.put("Email", getEmail());
		iObj.put("Password", getPassword());
		iObj.put("ProfilePic",  (isImageExists() ?  FilesUtils.blobToBase64String(getProfilePic()) : null ) );
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
	public static String toJson(List<Organization> list) {

		JSONObject jsonObj = new JSONObject();

		if (list == null) {
			return null;
		}

		if (list.size() == 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		for (Organization org : list) {

			if (org != null) {

				JSONObject itemObj = org.toJson();

				jsonArray.add(itemObj);
			}

		}

		jsonObj.put("organizations", jsonArray);

		return jsonObj.toString(2);
	}

}
