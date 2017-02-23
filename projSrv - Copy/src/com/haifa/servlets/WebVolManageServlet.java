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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.haifa.database.operations.ConnPool;
import com.haifa.database.operations.ItemsResProvider;
import com.haifa.database.operations.VolunteerResProvider;
import com.haifa.objects.Item;
import com.haifa.objects.Volunteer;
import com.haifa.utils.FilesUtils;

/**
 * Servlet implementation class WebItemsManageServlet
 */

public class WebVolManageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

	private static final String VOL_ID = "volunteerID";
	private static final String VOL_EMAIL = "email";
	private static final String VOL_PASSWORD = "password";
	private static final String VOL_FNAME = "fName";
	private static final String VOL_LNAME = "lName";
	private static final String VOL_ADDRESS = "address";
	private static final String VOL_BIRTHDATE = "birthDate";
	private static final String VOL_PROFILEPIC = "profilePic";

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

		/**
		 * private static final String VOL_ID = "volunteerID"; private static
		 * final String VOL_EMAIL = "email"; private static final String
		 * VOL_PASSWORD = "password"; private static final String VOL_FNAME =
		 * "fName"; private static final String VOL_LNAME = "lName"; private
		 * static final String VOL_ADDRESS = "address"; private static final
		 * String VOL_BIRTHDATE = "birthDate"; private static final String
		 * VOL_PROFILEPIC = "profilePic";
		 */
		String volID = null;
		String email = null;
		String fName = null;
		String lName = null;
		String address = null;
		String birthDate = null;
		
		String password = null;		
		boolean isDelete = false;
		String fileName = null;
		byte[] image = null;
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
					
					if (fieldname.equals(VOL_ID)) {
						volID = fieldvalue;
					} else if (fieldname.equals(VOL_EMAIL)) {
						email = fieldvalue;
					} else if (fieldname.equals(VOL_FNAME)) {
						fName = fieldvalue;
					} else if (fieldname.equals(VOL_LNAME)) {
						lName = fieldvalue;
					} else if (fieldname.equals(VOL_ADDRESS)) {
						address = fieldvalue;
					} else if (fieldname.equals(VOL_PASSWORD)) {
						password = fieldvalue;					
					} else if (fieldname.equals(VOL_BIRTHDATE)) {
						birthDate = fieldvalue;
					
					}else if (fieldname.equals(IS_DELETE)) {

						isDelete = Boolean.valueOf(fieldvalue);

					}

				} else {

					fileName = item.getName();
					image = item.get();

				}
			}

			while (retry > 0) {

				try {

					conn = ConnPool.getInstance().getConnection();

					VolunteerResProvider volProvider = new VolunteerResProvider();
					
					java.util.Date date = FilesUtils.getDateFromString(birthDate);
										
					Volunteer vol = new Volunteer(fName, lName, date, address, email, password, image);
					
					
					if (isDelete) {

						if(volProvider.deleteVolunteer(vol, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
						}
					

					} else {
			
						
						if (volProvider.insertVolunteer(vol, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}

					}

					/*if (image != null && image.length > 0) {

						FilesUtils.writeLocalCopy(fileName, image, false);
					}*/

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
