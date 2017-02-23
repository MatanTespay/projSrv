package com.haifa.objects;

import java.util.Date;
import java.util.List;

import com.haifa.utils.FilesUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class VolEvent {

    private int volEventID;
    private int volID;
    private int orgID;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String details;
    private String title;

    public VolEvent( int volEventID, int volID,int orgID, Date date,String details,
    		Date startTime, Date endTime, String title ) {
        this.volEventID =volEventID;
    	this.details = details;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.volID = volID;
        this.title = title;
        this.orgID=orgID;
    }
    public VolEvent(  int volID,int orgID, Date date,String details,  Date startTime, Date endTime, String title ) {
        this(0,  volID, orgID,  date, details,
        		 startTime,  endTime,  title);
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
    		iObj.put("eventID", getVolEventID());
    		iObj.put("volunteerID", getVolID());
    		iObj.put("organizationID", getOrgID());
    		iObj.put("date", (getDate() != null ) ? FilesUtils.getShortDateString(getDate()) : "");
    		iObj.put("startTime", (getStartTime() != null ) ? FilesUtils.getLongDateString(getStartTime()) : "");
    		iObj.put("endTime",  (getEndTime() != null ) ? FilesUtils.getLongDateString(getEndTime()) : "");
    		iObj.put("details", getDetails());
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
