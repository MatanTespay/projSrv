package com.haifa.objects;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class VolEvent {

    private int volEventID;
    private int volID;
    private int orgID;
    private String details;
    private String title;
    private Date date;
    private Date startTime;
    private Date endTime;

    public VolEvent( int orgID,int volID, String details, Date date, Date startTime, Date endTime, String title ) {
        this.details = details;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.volID = volID;
        this.title = title;
        this.orgID=orgID;
    }

    public VolEvent() {

    }

    public int getVolEventID() {
        return volEventID;
    }

    public void setVolEventID(int volEventID) {
        this.volEventID = volEventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getVolID() {
        return volID;
    }


    public void setVolID(int volID){
        this.volID=volID;
    }

    public int getOrgID() {
        return orgID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }
    /**
     * Create a json object from Org. object
     * @return
     */
  
    	public JSONObject toJson() {
    		//(int id , String name, String address, 
    		//String email, String password, byte[] profilePic)

    		JSONObject iObj = new JSONObject();
    		iObj.put("orgID", getOrgID());
    		iObj.put("volID", getVolID());
    		iObj.put("details", getDetails());
    		iObj.put("date", getDate());
    		iObj.put("startTime", getStartTime());
    		iObj.put("endTime", getEndTime());
    		iObj.put("title", getTitle());
    		return iObj;
    	}
   
    /**
     * 
     * @param list
     * @return
     */
    	public static String toJson(List<VolEvent> list) {

    		JSONObject jsonObj = new JSONObject();

    		if (list == null) {
    			return null;
    		}

    		if (list.size() == 0) {
    			return null;
    		}

    		JSONArray jsonArray = new JSONArray();

    		for (VolEvent event : list) {

    			if (event != null) {

    				JSONObject itemObj = event.toJson();

    				jsonArray.add(itemObj);
    			}

    		}

    		jsonObj.put("VolEvents", jsonArray);

    		return jsonObj.toString(2);
    	}
}