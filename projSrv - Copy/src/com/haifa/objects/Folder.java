package com.haifa.objects;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Folder {
	
	private String id;
	private String title;
	
	private List<Item> items = null;
	

	public Folder(String id) {
		this.id = id;
	}
	

	public Folder(String id, String title) {
		this.id = id; 
		this.title = title;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public void setItems(List<Item> items) {
		if(items!=null && items.size()>0){
			this.items = items;
		}
	}
	
	
	private List<Item> getItems() {
		return items;
	}
	
	
	
	public static String toJson(List<Folder> list) {

		JSONObject jsonObj = new JSONObject();

		if (list == null) {
			return null;
		}

		if (list.size() == 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		for (Folder f : list) {

			if (f != null) {

				JSONObject fObj = new JSONObject();

				fObj.put("id", f.getId());
				fObj.put("title", f.getTitle());

				List<Item> items = f.getItems();

				if (items != null && items.size() > 0) {

					JSONArray itemsJArray = new JSONArray();
					for (Item item : items) {
						JSONObject itemJobj = item.toJson();
						itemsJArray.add(itemJobj);
					}

					fObj.put("items", itemsJArray);
				}

				jsonArray.add(fObj);
			}

		}

		jsonObj.put("folders", jsonArray);

		return jsonObj.toString(2);
	}

}
