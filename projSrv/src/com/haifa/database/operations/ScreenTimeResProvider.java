package com.haifa.database.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haifa.objects.ScreenTime;
import com.haifa.objects.VolEvent;
import com.haifa.utils.FilesUtils;

public class ScreenTimeResProvider {
	private static final String update_sql = "UPDATE  screen_time  SET "
			+ " userID  = ?,  screenID  = ?,  date  = ?,  duration  = ?"
			+ " WHERE  ID  = ?; ";

	//private static final String selectByUser_sql = "SELECT * FROM  screenName WHERE screenID = ? AND userID = ?;";
	private static final String selectByID_sql = "SELECT * FROM  screen_time WHERE ID = ?;";	
	
	private static final String selectByParams = "SELECT * FROM  screen_time where userID = ? and screenID = ? and date  = ?;";
	
   // private static final String selectScreen_name = "SELECT screenName FROM screen WHERE  screenID = ? ";

	private static final String selectByUser_sql = 
	"SELECT email, st.UserID, st.screenID,  s.screenName, st.date, sum(duration) as TimeInDay"
	+ " from screen_time st inner join screen s "
	+ " on st.screenID = s.screenID inner join volunteer v on st.UserID = v.volunteerID "
	+ " where st.userID = ?"
	+ " group by st.UserID, st.screenID,  s.screenName, st.date"
	+ " order by date";

	// insert important
	private static final String insert_sql = " INSERT INTO  screen_time  ("
			+ "  userID ,  screenID ,  date ,  duration ) VALUES (?, ?, ?, ?);";

	//not sure we will need 
	private static final String delete_sql = "DELETE FROM screen_time WHERE ID = ? ;";
																			
																							
	/**
	 * select all screen time objects in db																						// folder
	 * @param conn
	 * @return
	 * @throws SQLException
	 */

	public List<ScreenTime> getUserScreenTime(int userId, Connection conn) throws SQLException {

		List<ScreenTime> results = new ArrayList<ScreenTime>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(selectByUser_sql);
			ps.setInt(1,userId);
	
			rs = ps.executeQuery();

			while (rs.next()) {
		
				// st.UserID, st.screenID,  s.screenName, st.date, sum(duration) 
				
				int userID = rs.getInt("userID");
				int screenID = rs.getInt("screenID");
				Date date = rs.getDate("date");
				String name = rs.getString("screenName");
				String email = rs.getString("email");
				long sumDuration = rs.getInt("TimeInDay");
				//long lonDuration = rs.getLong("TimeInDay");

				ScreenTime obj = new ScreenTime(0, userID, screenID, date,  sumDuration, name, email);
						results.add(obj);

			
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
	 * the method inserts screen, time and date that the user was in the screen.
	 * @param vol
	 * @param conn
	 * @return
	 */
	public boolean insertScreenTime(ScreenTime screenT, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			int id = screenT.getId();
		// the field to update or insert
			int screenId = screenT.getScreenId();
			int userId = screenT.getUserId();
			Date date = screenT.getDate();
			long duration = screenT.getDuration();
	


			
			// get the row from DB
			stt = (PreparedStatement) conn.prepareStatement(selectByParams);
			//stt.setInt(1, id);	
			stt.setInt(1, userId);
			stt.setInt(2, screenId);
			stt.setString(3, FilesUtils.getShortDateString(date));
			
			if (stt.execute()) {
				
				rs1 = stt.getResultSet();
				
				if (rs1.next()) 
				{
					// its execute update
				
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					id = rs1.getInt("ID");
					
					long oldDuration = rs1.getLong("duration");					
					long newDuration =  duration  + oldDuration;
					
					ps.setInt(5,id);
					
					ps.setInt(1, userId);
					ps.setInt(2, screenId);
					ps.setDate(3, FilesUtils. toSqlDate(date));
					ps.setLong(4,  newDuration);
				
					
					 ps.execute();
					
					result = true;
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					//ps.setInt(1,id);
					ps.setInt(1, userId);
					ps.setInt(2, screenId);
					ps.setDate(3,FilesUtils. toSqlDate(date));
					ps.setLong(4, duration);
					
					
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

	


}
