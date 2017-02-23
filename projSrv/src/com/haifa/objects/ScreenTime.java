package com.haifa.objects;

import java.util.Date;
import java.util.List;

import com.haifa.utils.FilesUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ScreenTime {

	private int Id;
	private int userId;	
	private int screenId;	
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private Date date;	
	private int duration;
	private String name;
	
	public ScreenTime() {
		super();
	}
	public ScreenTime(int userId, int screenId, Date date, int duration ,String name) {
		this(0,  userId,  screenId,  date, duration , name,"");
	}
	//ScreenTime(userID, screenID,  Name,  sumDuration)
	public ScreenTime(int userId, int screenId, Date date, int duration) {
		this(0,  userId,  screenId,  date, duration , "","");
	}
	public ScreenTime(int id, int userId, int screenId, Date date,
			int duration, String name, String email) {
		super();
		Id = id;
		this.userId = userId;
		this.screenId = screenId;
		this.date = date;
		this.name = name;
		this.duration = duration;
		this.email =email;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getScreenId() {
		return screenId;
	}
	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * 
	 * @param list
	 * @return
	 */
		public static String toJson(List<ScreenTime> list) {

			JSONObject jsonObj = new JSONObject();

			if (list == null) {
				return null;
			}

			if (list.size() == 0) {
				return null;
			}

			JSONArray jsonArray = new JSONArray();

			for (ScreenTime screen : list) {

				if (screen != null) {

					JSONObject itemObj = screen.toJson();

					jsonArray.add(itemObj);
				}

			}

			jsonObj.put("screenTime", jsonArray);

			return jsonObj.toString(2);
		}
		/**
		 * Create a json object from Org. object
		 * @return
		 */
			public JSONObject toJson() {
				//(int id , String name, String address, 
				//String email, String password, byte[] profilePic)

				JSONObject iObj = new JSONObject();
				iObj.put("ID", getId());
				iObj.put("email", getEmail());
				iObj.put("UserId", getUserId());
				iObj.put("ScreenId", getScreenId());
				iObj.put("Date", (getDate() != null ) ? FilesUtils.getShortDateString(getDate()) : "");
				iObj.put("Duration", getDuration() );
				iObj.put("Name", getName() );
				return iObj;
			}

	
}
