package com.haifa.database.operations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haifa.objects.Folder;
import com.haifa.objects.Item;
import com.haifa.objects.Volunteer;
import com.haifa.utils.FilesUtils;



public class VolunteerResProvider {

	private static final String update_sql = "UPDATE volunteer SET email =?, password = ?, fName = ?, lName = ? , address =? , birthDate = ?, profilePic = ? WHERE volunteerID = ?;";

	private static final String select_sql = "SELECT * FROM  volunteer WHERE volunteerID = ?;";
	
	private static final String selectAll_sql = "SELECT * FROM  volunteer;";

	private static final String insert_sql = "INSERT INTO volunteer (email, password , fName , lName , address  , birthDate, profilePic ) VALUES (?, ?, ?, ?, ?, ?, ?);";

	private static final String delete_sql = "DELETE FROM volunteer WHERE volunteerID = ? ;";

	// =========
	//private static final String select_img_sql = "SELECT image FROM  volunteer WHERE volunteerID = ?;";
	//private static final String delete_all_sql = "DELETE FROM volunteer WHERE folder_id=?;"; // delete
																								// items
																								// folder

	//private static final String select_all_sql = "SELECT * FROM volunteer WHERE folder_id=?;"; // get
																								// items
																								// of
	/**
	 * select all volunteers in db.																							// folder
	 * @param conn
	 * @return
	 * @throws SQLException
	 */

	public List<Volunteer> getAllVolunteers(Connection conn) throws SQLException {

		List<Volunteer> results = new ArrayList<Volunteer>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(selectAll_sql);

	
			rs = ps.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("volunteerID");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String fName = rs.getString("fName");
				String lName = rs.getString("lName");
				String address = rs.getString("address");
				Date birthDate = rs.getDate("birthDate");
				
				//get the image
				java.sql.Blob imageBlob =  rs.getBlob("profilePic");
				
				byte[] profilePic = null;
				if (imageBlob != null) {
					profilePic = imageBlob.getBytes(1, (int) imageBlob.length());
				}
				
				Volunteer vol = new Volunteer(id, fName, lName, birthDate, address, email, password, profilePic);
				results.add(vol);

			
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

	/**
	 * the method gets a volunteer object and checks if it exist, if so the db will update
	 * the vol otherwise insert new vol.
	 * @param vol
	 * @param conn
	 * @return
	 */
	public boolean insertVolunteer(Volunteer vol, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			int id = vol.getId();
		// the field to update or insert
			String email = vol.getEmail();
			String password = vol.getPassword();
			String fName = vol.getfName();
			String lName = vol.getlName();
			String address = vol.getAddress();
			Date birthDate = vol.getBirthDate();
			byte[] profilePic = vol.getProfilePic();
			// get the volunteer in db that the id is vol.getID
			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			stt.setInt(1, id);			
			
			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					ps.setString(2, email);
					ps.setString(3, password);
					ps.setString(4, fName);
					ps.setString(5, lName);
					ps.setString(6, address);
					ps.setDate(7, FilesUtils. toSqlDate(birthDate));
					ps.setBytes(8, profilePic);
					
					return ps.execute();
					//result = true;
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					//ps.setInt(1,id);
					ps.setString(2, email);
					ps.setString(3, password);
					ps.setString(4, fName);
					ps.setString(5, lName);
					ps.setString(6, address);
					ps.setDate(7, FilesUtils. toSqlDate(birthDate));
					ps.setBytes(8, profilePic);
					return  ps.execute();
					//result = true;

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

	public boolean deleteVolunteer(Volunteer vol, Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;
		try {

			if (vol != null) {
				
				int id = vol.getId();
						
				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

			
				ps.setInt(1, id);
				
				return ps.execute();

				//result = true;
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
