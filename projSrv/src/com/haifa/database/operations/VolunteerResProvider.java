package com.haifa.database.operations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.haifa.objects.Folder;
import com.haifa.objects.Item;
import com.haifa.objects.Volunteer;;

public class VolunteerResProvider {
	
	

	private static final String update_sql = "UPDATE volunteer SET title=?, description=?, image=?, folder_id=? WHERE volunteerID=?;";

	private static final String select_sql = "SELECT * FROM  volunteer WHERE volunteerID=?;";

	private static final String select_img_sql = "SELECT image FROM  volunteer WHERE volunteerID=?;";


	private static final String insert_sql = "INSERT INTO volunteer (volunteerID, title, description, image, folder_id) VALUES (?, ?, ?, ?, ?);";

	private static final String delete_sql = "DELETE FROM volunteer WHERE volunteerID=?;";

  //=========
	private static final String delete_all_sql = "DELETE FROM volunteer WHERE folder_id=?;"; // delete  items folder

	private static final String select_all_sql = "SELECT * FROM volunteer WHERE folder_id=?;"; // get items of folder

	public List<Volunteer> getAllVolunteers(Folder folder, Connection conn)
			throws SQLException {

		List<Volunteer> results = new ArrayList<Volunteer>();

		if (folder == null) {
			return results;
		}

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(select_all_sql);

			ps.setString(1, folder.getId());

			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString(1);
				String title = rs.getString(2);
				String description = rs.getString(3);

				java.sql.Blob imageBlob = rs.getBlob(4);
				byte[] image = null;
				if (imageBlob != null) {
					image = imageBlob.getBytes(1, (int) imageBlob.length());
				}

				

				String folderId = rs.getString(5);
				Item item = new Item(id, title, description, image,folderId);

				//results.add(item);

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

	public byte[] getImage(String itemId, Connection conn)
			throws SQLException {

		byte[] result = null;

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
		
			ps = conn.prepareStatement(select_img_sql);

			ps.setString(1, itemId);

			rs = ps.executeQuery();

			while (rs.next()) {

				java.sql.Blob imageBlob = rs.getBlob(1);
				if (imageBlob != null) {
					result = imageBlob.getBytes(1, (int) imageBlob.length());
				}
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

		return result;
	}

	public boolean insertItem(Item obj, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			String id = obj.getId();
			String title = obj.getTitle();
			String description = obj.getDescription();

			byte[] imageBytes = obj.getImage();

			String folderId = obj.getFolderId();

			if (imageBytes == null) {
				imageBytes = getImage(id, conn);
			}

			

			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			stt.setString(1, id);

			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);

					ps.setString(1, title);
					ps.setString(2, description);

					if (imageBytes != null) {
						InputStream is = new ByteArrayInputStream(imageBytes);
						ps.setBlob(3, is);

					} else {

						ps.setNull(3, Types.BLOB);
					}

					

					ps.setString(4, folderId);

					// where
					ps.setString(5, id);

					ps.execute();

					result = true;

				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					ps.setString(1, id);
					ps.setString(2, title);
					ps.setString(3, description);

					if (imageBytes != null) {
						InputStream is = new ByteArrayInputStream(imageBytes);
						ps.setBlob(4, is);

					} else {

						ps.setNull(4, Types.BLOB);
					}

					

					ps.setString(5, folderId);

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

	public boolean deleteAllItemsByFolderId(String folderId, Connection conn)
			throws SQLException {

		boolean result = false;

		PreparedStatement ps = null;

		try {

			ps = (PreparedStatement) conn.prepareStatement(delete_all_sql);

			ps.setString(1, folderId);

			ps.execute();

			result = true;

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

	public boolean deleteItem(Item obj, Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;

		try {

			if (obj != null) {

				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

				String id = obj.getId();

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
