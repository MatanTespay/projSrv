package com.haifa.objects;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Item {

	private String id = null;
	private String title;
	private String description;
	private byte[] image = null;
	private String folderId = null;

	public Item(String id) {
		this.id = id;

	}

	public Item(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public Item(String id, String title, String description, byte[] image,
			String folderId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.image = image;
		this.folderId = folderId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;

	}

	public String getFolderId() {
		return folderId;
	}

	public JSONObject toJson() {

		JSONObject iObj = new JSONObject();
		iObj.put("id", getId());
		iObj.put("title", getTitle());
		iObj.put("description", getDescription());
		iObj.put("img", isImageExists());

		return iObj;
	}

	private boolean isImageExists() {
		if (image == null || image.length == 0) {
			return false;
		}
		return true;
	}

	public static String toJson(List<Item> list) {

		JSONObject jsonObj = new JSONObject();

		if (list == null) {
			return null;
		}

		if (list.size() == 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		for (Item item : list) {

			if (item != null) {

				JSONObject itemObj = item.toJson();

				jsonArray.add(itemObj);
			}

		}

		jsonObj.put("items", jsonArray);

		return jsonObj.toString(2);
	}

}
