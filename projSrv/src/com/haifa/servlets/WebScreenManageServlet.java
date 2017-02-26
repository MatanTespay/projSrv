package com.haifa.servlets;

import java.io.IOException;
import java.sql.Connection;
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
import com.haifa.database.operations.ScreenTimeResProvider;
import com.haifa.objects.ScreenTime;
import com.haifa.utils.FilesUtils;

/**
 * Servlet implementation class WebItemsManageServlet
 */

public class WebScreenManageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";


	private static final String IS_DELETE = "delete";
	public static final int DB_RETRY_TIMES = 5;

	private static final String USER_ID = "userID";
	private static final String SCREEN_ID = "screenID";
	private static final String DATE = "date";
	private static final String DURATION = "duration";
	
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


		String userID = null;
		String screenID = null;
		String date = null;
		String duration = null;
		
		String dataFromApp = null;
		
		boolean isDelete = false;
		
		String respPage = RESOURCE_FAIL_TAG;
		try {

			System.out.println("=======VOl Servlet =======");
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
					if(fieldname.equals("data")){
						dataFromApp = fieldvalue ;
					}
					if (fieldname.equals(USER_ID)) {
						userID = fieldvalue;
					} else if (fieldname.equals(SCREEN_ID)) {
						screenID = fieldvalue;
					} else if (fieldname.equals(DATE)) {
						date = fieldvalue;
					} else if (fieldname.equals(DURATION)) {
						duration = fieldvalue;
					} 

				} 
			}

			while (retry > 0) {

				try {

					conn = ConnPool.getInstance().getConnection();

					ScreenTimeResProvider screen_Provider = new ScreenTimeResProvider();
					
					/*java.util.Date the_date = FilesUtils.getDateFromString(date);
					int user_id = Integer.parseInt(userID);
					int screen_id = Integer.parseInt(screenID);
					long the_duration = Long.parseLong(duration);*/
					JSONTokener jsonTokener = new JSONTokener(dataFromApp);

		            JSONObject json = (JSONObject) jsonTokener.nextValue();
		            
					ScreenTime screen = new ScreenTime();
										
					
					if(screen.fromJson(json)){
						if (screen_Provider.insertScreenTime(screen, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
					}
					
					

					retry = 0;

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
