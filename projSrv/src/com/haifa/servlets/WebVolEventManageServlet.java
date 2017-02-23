package com.haifa.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.haifa.database.operations.ConnPool;
import com.haifa.database.operations.VolEventResProvider;
import com.haifa.database.operations.VolunteerResProvider;
import com.haifa.objects.VolEvent;
import com.haifa.objects.Volunteer;
import com.haifa.utils.FilesUtils;

public class WebVolEventManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

	private static final String VOLEVENT_ID = "eventID";
	private static final String VOLEVENT_VOLUNTEERID = "volunteerID";
	private static final String VOLEVENT_ORGANIZATIONID = "organizationID";
	private static final String VOLEVENTG_DATE  = "date";
	private static final String VOLEVENT_STARTTIME = "startTime";
	private static final String VOLEVENT_ENDTIME = "endTime";
	private static final String VOLEVENTG_DETAILS = "details";
	private static final String VOLEVENTG_TITLE = "title";

	private static final String IS_DELETE = "delete";
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

		// Commons file upload classes are specifically instantiated
		FileItemFactory factory = new DiskFileItemFactory();

		ServletFileUpload upload = new ServletFileUpload(factory);
		ServletOutputStream out = null;

		int retry = DB_RETRY_TIMES;
		Connection conn = null;


		String eventID = null;
		String volunteerID = null;
		String organizationID = null;
		String date = null;
		String startTime = null;
		String endTime = null;
		String details = null;
		String title = null;
			
		boolean isReqFromApp = false;
		String dataFromApp = null;
		
		boolean isDelete = false;
		//String fileName = null;
		
		String respPage = RESOURCE_FAIL_TAG;
		try {

			System.out.println("=======VOlEvent Servlet =======");
			// Parse the incoming HTTP request
			// Commons takes over incoming request at this point
			// Get an iterator for all the data that was sent
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();

			// Set a response content type
			resp.setContentType("text/html");

			// Setup the output stream for the return XML data
			out = resp.getOutputStream();

			// Iterate through the incoming request data
			while (iter.hasNext()) {
				// Get the current item in the iteration
				FileItem item = (FileItem) iter.next();

				// If the current item is an HTML form field
				if (item.isFormField()) {
					// If the current item is file data

					// If the current item is file data
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();

					System.out.println(fieldname + "=" + fieldvalue);
				
					if (fieldname.equals("data")) { // a request from app to with object
						isReqFromApp = true;	
						dataFromApp = fieldvalue;						
					}
					else if (fieldname.equals("req")) {
						isDelete = fieldvalue.equals("15"); //request code for delete from app
					}
					//request from web
					else if (fieldname.equals(VOLEVENT_ID)) {
						eventID = fieldvalue;
					} else if (fieldname.equals(VOLEVENT_VOLUNTEERID)) {
						volunteerID = fieldvalue;
					} else if (fieldname.equals(VOLEVENT_ORGANIZATIONID)) {
						organizationID = fieldvalue;
					} else if (fieldname.equals(VOLEVENTG_DATE)) {
						date = fieldvalue;
					} else if (fieldname.equals(VOLEVENT_STARTTIME)) {
						startTime = fieldvalue;
					} else if (fieldname.equals(VOLEVENT_ENDTIME)) {
						endTime = fieldvalue;					
					} else if (fieldname.equals(VOLEVENTG_DETAILS)) {
						details = fieldvalue;
					} else if (fieldname.equals(VOLEVENTG_TITLE)) {
						title = fieldvalue;
					}else if (fieldname.equals(IS_DELETE)) {

						isDelete = Boolean.valueOf(fieldvalue);

					}

				} //else {

					//fileName = item.getName();
					

				//}
			}

			while (retry > 0) {

				try {

					conn = ConnPool.getInstance().getConnection();

					VolEventResProvider volEventProvider = new VolEventResProvider();
					VolEvent volevent = new VolEvent();
					if(!isReqFromApp){
						java.util.Date eventDate = FilesUtils.getDateFromString(date);
						java.util.Date startEvent = FilesUtils.getDateTimeFromString(startTime);
						java.util.Date endEvent = FilesUtils.getDateTimeFromString(endTime);
						int vol=  Integer.parseInt(volunteerID);
						int org=  Integer.parseInt(organizationID);

							
						volevent = new VolEvent( vol, org,
								eventDate, details,   startEvent,  endEvent,  title);

						if (isDelete) {

							if(volEventProvider.deleteVolEvent(volevent, conn)){
								respPage = RESOURCE_SUCCESS_TAG;
							}

						} else {
							if (volEventProvider.insertVolEvent(volevent, conn)) {
								respPage = RESOURCE_SUCCESS_TAG;
							}

						}
					}else{
						JSONTokener jsonTokener = new JSONTokener(dataFromApp);

			            JSONObject json = (JSONObject) jsonTokener.nextValue();
			            
			            if (volevent.fromJson(json)) {
							if (isDelete) {

								if(volEventProvider.deleteVolEvent(volevent, conn)){
									respPage = RESOURCE_SUCCESS_TAG;
								}
							

							} else {
								if (volEventProvider.insertVolEvent(volevent, conn)) {
									respPage = RESOURCE_SUCCESS_TAG;
								}

							}
						
						}				
					}


					
					retry = 0;

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

			out.println(respPage);
			out.close();

		} catch (FileUploadException fue) {
			fue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
