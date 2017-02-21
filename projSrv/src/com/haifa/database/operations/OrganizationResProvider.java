package com.haifa.database.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haifa.objects.Organization;
import com.haifa.objects.Volunteer;

public class OrganizationResProvider {

	// update organization query
	private static final String update_sql = "UPDATE  organization  SET  organizationID  = ?,  organizationName  = ?,  address  = ?,  email  = ?,  password  ?,  orgPic  = ? WHERE  organizationID  = ?;";
	// select organization by chosen org query 
	private static final String select_sql = "SELECT * FROM  organization WHERE organizationID = ?;";
	// select  all organizations query
	private static final String selectAll_sql = "SELECT * FROM  organization;";
	// insert org query
	private static final String insert_sql = "INSERT INTO  organization  ( organizationID ,  organizationName , "
			+ " address ,  email ,  password ,  orgPic ) VALUES (?, ?, ?,?, ?,?); ";
	// delete org query
	private static final String delete_sql = "DELETE FROM organization WHERE organizationID = ? ;";

	// =========
	//private static final String select_img_sql = "SELECT image FROM  volunteer WHERE volunteerID = ?;";
	//private static final String delete_all_sql = "DELETE FROM volunteer WHERE folder_id=?;"; // delete
																								// items
																								// folder

	//private static final String select_all_sql = "SELECT * FROM volunteer WHERE folder_id=?;"; // get
																								// items
																								// of
	/**
	 * select all Organizations in db.																							// folder
	 * @param conn
	 * @return
	 * @throws SQLException
	 */

	public List<Organization> getAllOrganizations(Connection conn) throws SQLException {

		List<Organization> results = new ArrayList<Organization>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(selectAll_sql);

	
			rs = ps.executeQuery();

			while (rs.next()) {	
		
				// columns
				int id = rs.getInt("organizationID");
				String name = rs.getString("organizationName");
				String address = rs.getString("address");
				String email = rs.getString("email");
				String password = rs.getString("password");
				byte[]  orgPic = rs.getBytes("orgPic");
				
				Organization org = new Organization(id, name, address, email, password, orgPic);
		
					
				results.add(org);

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
	 * the method gets a Organization object and checks if it exist, if so the db will update
	 * the Organization otherwise insert new Organization.
	 * @param vol
	 * @param conn
	 * @return
	 */
	public boolean insertVolunteer(Organization org, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			int id = org.getId();
		// the field to update or insert
			String email = org.getEmail();
			String password = org.getPassword();
			String name = org.getName();
			String address = org.getAddress();
			byte[] profilePic = org.getProfilePic();
			// get the Organization in db that the id is org.getID
			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			//where
			stt.setInt(1, id);
			
			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					ps.setInt(1, id);
					ps.setString(2, name);
					ps.setString(3, address);
					ps.setString(4, email);
					ps.setString(5, password);
					ps.setBytes(6, profilePic);
					
					
					result = ps.execute();
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					ps.setInt(1, id);
					ps.setString(2, name);
					ps.setString(3, address);
					ps.setString(4, email);
					ps.setString(5, password);
					ps.setBytes(6, profilePic);
					result = ps.execute();
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

	public boolean deleteVolunteer(Organization org, Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;
		try {

			if (org != null) {
				
				int id = org.getId();
						

				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

			//delete the recored
				ps.setInt(1, id);
				
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
