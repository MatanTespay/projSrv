package com.haifa.database.operations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haifa.objects.VolEvent;
import com.haifa.objects.Volunteer;
import com.haifa.utils.FilesUtils;

public class VolEventResProvider {

	private static final String update_sql = "UPDATE  volevent  SET    volunteerID  =? "
			+ " ,  organizationID  = ?,  date  = ?,  startTime  = ?,  endTime  = ?,  details  = ?,"
			+ "  title  = ? WHERE  eventID  = ?; ";

	private static final String selectByVolAndOrg_sql = "SELECT * FROM  volevent WHERE volunteerID = ? AND organizationID = ?;";
	
	private static final String selectAll_sql = "SELECT * FROM  volevent;";

	private static final String insert_sql = " INSERT INTO  volevent  (   volunteerID ,  organizationID ,  date ,  startTime ,  endTime ,  details ,  title ) VALUES (?, ?, ?, ?, ?, ?, ?);";

	//private static final String select_img_sql = "SELECT image FROM  volunteer WHERE volunteerID = ?;";
	
	private static final String delete_sql = "DELETE FROM volevent WHERE eventID = ? ;";

	// =========

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

	public List<VolEvent> getAllVolEvent(Connection conn) throws SQLException {

		List<VolEvent> results = new ArrayList<VolEvent>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(selectAll_sql);

	
			rs = ps.executeQuery();

			while (rs.next()) {
		
				int id = rs.getInt("eventID");
				int volunteerID = rs.getInt("volunteerID");
				int organizationID = rs.getInt("organizationID");
				Date date = rs.getDate("date");
				Date startTime = rs.getTimestamp("startTime");
				Date endTime = rs.getTimestamp("endTime");
				String details = rs.getString("details");
				String title = rs.getString("title");
				
				VolEvent event = new VolEvent(  id,  volunteerID, organizationID, 
						 date,  details,   startTime,  endTime,  title );
						results.add(event);

			
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
	public boolean insertVolEvent(VolEvent event, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			int id = event.getVolEventID();
		// the field to update or insert
			int volId = event.getVolID();
			int orgId = event.getOrgID();
			Date date = event.getDate();
			Date startTime = event.getStartTime();
			Date endTime = event.getEndTime();
			String details = event.getDetails();
			String title = event.getTitle();


			
			// get the volunteer in db that the id is vol.getID
			stt = (PreparedStatement) conn.prepareStatement(selectByVolAndOrg_sql);
			stt.setInt(1, volId);			
			stt.setInt(2, orgId);
			
			if (stt.execute()) {
				rs1 = stt.getResultSet();
				
				if (rs1.next()) 
				{
					// its execute update

				
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					id = rs1.getInt("eventID");
					ps.setInt(8,id);
					
					ps.setInt(1, volId);
					ps.setInt(2, orgId);
					ps.setDate(3, FilesUtils. toSqlDate(date));
					ps.setString(4,  FilesUtils. getLongDateString(startTime));
					ps.setString(5, FilesUtils. getLongDateString(endTime));
					ps.setString(6, details);
					ps.setString(7, title);
					
					 ps.execute();
					
					result = true;
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					//ps.setInt(1,id);
					ps.setInt(1, volId);
					ps.setInt(2, orgId);
					//			Date startTime = rs.getTimestamp("startTime");
					//Date endTime = rs.getTimestamp("endTime");
					ps.setDate(3,FilesUtils. toSqlDate(date));
					ps.setString(4,  FilesUtils. getLongDateString(startTime));
					ps.setString(5, FilesUtils. getLongDateString(endTime));
					ps.setString(6, details);
					ps.setString(7, title);
					
					
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

	

	public boolean deleteVolEvent(VolEvent event, Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;
		PreparedStatement stt = null;
		ResultSet rs1 = null;
		try {

			stt = (PreparedStatement) conn.prepareStatement(selectByVolAndOrg_sql);
			stt.setInt(1, event.getVolID());			
			stt.setInt(2, event.getOrgID());	
			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					// its execute update
					
		
					ps = (PreparedStatement) conn.prepareStatement(delete_sql);
					int eventId =  rs1.getInt("eventID");
					ps.setInt(1,eventId);
					 ps.execute();
					 result = true;
				}
			
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
