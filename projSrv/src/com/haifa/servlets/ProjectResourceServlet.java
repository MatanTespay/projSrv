package com.haifa.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

import com.haifa.database.operations.ConnPool;
import com.haifa.database.operations.OrganizationResProvider;
import com.haifa.database.operations.ScreenTimeResProvider;
import com.haifa.database.operations.VolEventResProvider;
import com.haifa.database.operations.VolunteerResProvider;
import com.haifa.objects.Organization;
import com.haifa.objects.ScreenTime;
import com.haifa.objects.VolEvent;
import com.haifa.objects.Volunteer;
import com.haifa.utils.FilesUtils;

/**
 * Servlet implementation class ProjectResourceServlet
 */
public class ProjectResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// ========

	// VOLUNEER
	private static final int GET_VOLUNTEERS_REQ = 8;
	// columns
	private static final String VOL_ID = "volunteerID";
	private static final String VOL_EMAIL = "email";
	private static final String VOL_PASSWORD = "password";
	private static final String VOL_FNAME = "fName";
	private static final String VOL_LNAME = "lName";
	private static final String VOL_ADDRESS = "address";
	private static final String VOL_BIRTHDATE = "birthDate";
	private static final String VOL_PROFILEPIC = "profilePic";

	// ORGANIZATION
	private static final int GET_ORGANIZATIONS_REQ = 9;
	// columns
	private static final String ORG_ID = "organizationID";
	private static final String ORG_NAME = "organizationName";
	private static final String ORG_ADDRESS = "address";
	private static final String ORG_EMAIL = "email";
	private static final String ORG_PASSWORD = "password";
	private static final String ORG_ORGPIC = "orgPic";
	private static final int UPDATE_ORG_REQ = 12;

	// VOLEVENT
	private static final int GET_VOLEVENTS_REQ = 10;

	// columns
	private static final String VOLEVENT_ID = "eventID";
	private static final String VOLEVENT_VOLUNTEERID = "volunteerID";
	private static final String VOLEVENT_ORGANIZATIONID = "organizationID";
	private static final String VOLEVENTG_DATE = "date";
	private static final String VOLEVENT_STARTTIME = "startTime";
	private static final String VOLEVENT_ENDTIME = "endTime";
	private static final String VOLEVENTG_DETAILS = "details";
	private static final String VOLEVENTG_TITLE = "title";

	private static final String FOLDER_ID = "f_id";
	private static final String FOLDER_TITLE = "f_title";

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

	private static final String ITEM_ID = "it_id";
	private static final String ITEM_TITLE = "it_title";
	private static final String ITEM_DESCRIPTION = "it_desc";
	private static final String ITEM_FOLDER_ID = "it_fid";
	private static final String FILE_NAME = "name";

	/**
	 * STATISTICS, screen object
	 */
	private static final int GET_SCREENTIME_REQ = 11;
	private static final String SCREEN_TIME_ID = "ID";
	private static final String SCREEN_USERID = "userID";
	private static final String SCREEN_ID = "screenID";
	private static final String SCREEN_DATE = "date";
	private static final String SCREEN_DURATION = "duration";

	private static final int UPDATE_SCREEN_REQ = 16;

	private static final String REQ = "req";

	public static final int DB_RETRY_TIMES = 5;

	public void init(ServletConfig config) throws ServletException {

		super.init();

		String tmp = config.getServletContext().getInitParameter("localAppDir");
		if (tmp != null) {
			FilesUtils.appDirName = config.getServletContext().getRealPath(tmp);
			System.out.println(FilesUtils.appDirName);

		}

	}

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String respPage = null;
		String userReq = req.getParameter(REQ);
		Connection conn = null;
		int retry = DB_RETRY_TIMES;

		if (userReq != null) {

			int reqNo = Integer.valueOf(userReq);
			System.out.println("ProjectResourceServlet:: req code ==>" + reqNo);
			while (retry > 0) {

				try {

					switch (reqNo) {

					case GET_VOLUNTEERS_REQ: {

						conn = ConnPool.getInstance().getConnection();
						VolunteerResProvider v_provider = new VolunteerResProvider();
						List<Volunteer> v_list = v_provider
								.getAllVolunteers(conn);
						String resultJson = Volunteer.toJson(v_list);

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}

					case GET_VOLEVENTS_REQ: {

						conn = ConnPool.getInstance().getConnection();
						VolEventResProvider v_provider = new VolEventResProvider();
						List<VolEvent> v_list = v_provider.getAllVolEvent(conn);
						String resultJson = VolEvent.toJson(v_list);

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}
					case GET_SCREENTIME_REQ: {
						String userID = req.getParameter("userId");

						conn = ConnPool.getInstance().getConnection();
						ScreenTimeResProvider screenProvider = new ScreenTimeResProvider();
						int user = Integer.parseInt(userID);
						List<ScreenTime> v_list = screenProvider
								.getUserScreenTime(user, conn);
						String resultJson = ScreenTime.toJson(v_list);

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}
					case GET_ORGANIZATIONS_REQ: {

						conn = ConnPool.getInstance().getConnection();
						OrganizationResProvider v_provider = new OrganizationResProvider();
						List<Organization> v_list = v_provider
								.getAllOrganizations(conn);
						String resultJson = Organization.toJson(v_list);

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}
					case UPDATE_ORG_REQ: {

						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");

						conn = ConnPool.getInstance().getConnection();

						Organization org = new Organization();

						OrganizationResProvider orgProvider = new OrganizationResProvider();

						String data = req.getParameter("data");

						JSONTokener jsonTokener = new JSONTokener(data);

						JSONObject json = (JSONObject) jsonTokener.nextValue();

						if (org.fromJson(json)) {

							if (orgProvider.insertOrganization(org, conn)) {
								respPage = RESOURCE_SUCCESS_TAG;
							}
						}

						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;

					}

					default:
						retry = 0;
					}

				} catch (SQLException e) {
					e.printStackTrace();
					retry--;
				} catch (Throwable t) {
					t.printStackTrace();
					retry = 0;
				} finally {
					if (conn != null) {
						ConnPool.getInstance().returnConnection(conn);
					}
				}
			}
		}

	}

}
