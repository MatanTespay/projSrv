package com.haifa.database.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.haifa.objects.Folder;
import com.haifa.objects.Item;

public class FoldersResProvider {

	private static final String update_sql = "UPDATE folders SET title=? WHERE id=?;";

	private static final String select_sql = "SELECT * FROM  folders WHERE id=?;";

	private static final String insert_sql = "INSERT INTO folders (id, title) VALUES (?,?);";

	private static final String delete_sql = "DELETE FROM folders WHERE id=?;";

	private static final String select_all_sql = "SELECT * FROM folders;";

	public List<Folder> getAllFolders(Connection conn) throws SQLException {

		List<Folder> results = new ArrayList<Folder>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(select_all_sql);

	
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString(1);
				String title = rs.getString(2);
				
				Folder f = new Folder(id, title);
				
				results.add(f);

			}
			
			// get items if exist
			ItemsResProvider itemResProvider = new ItemsResProvider();
			for(Folder folder:results){
				List<Item> items = itemResProvider.getAllItems(folder, conn);
				folder.setItems(items);
			}

		} catch (SQLException e) {
			throw e;

		} catch (Throwable e) {
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return results;
	}

	public boolean insertFolder(Folder obj, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			String id = obj.getId();
			String title = obj.getTitle();

			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			stt.setString(1, id);
			

			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					ps.setString(1, title);
					// where
					ps.setString(2, id);
					ps.execute();
					result = true;
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					ps.setString(1,id);
					ps.setString(2, title);
					ps.execute();
					result = true;

				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (stt != null) {
				try {
					stt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}

	public boolean deleteFolder(Folder obj,
			Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;


		try {

			if (obj != null) {
				
				String id = obj.getId();
				
				ItemsResProvider itemResProvider = new ItemsResProvider();
				itemResProvider.deleteAllItemsByFolderId(id, conn);				

				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

			
				ps.setString(1, id);
				
				ps.execute();

				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return result;
	}



}
