package com.haifa.servlets;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haifa.database.operations.ConnPool;
import com.haifa.database.operations.FoldersResProvider;
import com.haifa.database.operations.ItemsResProvider;
import com.haifa.objects.Folder;
import com.haifa.objects.Item;
import com.haifa.utils.FilesUtils;

/**
 * Servlet implementation class ProjectResourceServlet
 */
public class ProjectResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// ========
	private static final int GET_ALL_FOLDERS_JSON_REQ = 0;
	private static final int INSERT_FOLDER_REQ = 1;
	private static final int DELETE_FOLDER_REQ = 2;
	private static final int INSERT_ITEM_REQ = 3;
	private static final int DELETE_ITEM_REQ = 4;
	private static final int GET_ITEM_IMAGE_REQ = 5;
	private static final int GET_ITEMS_OF_FOLDER_JSON_REQ = 6;
	private static final int GET_FILE_FROM_FILESYSTEM_REQ = 7;
	// requserts numbers
	
	
	// VOLUNEER
	private static final int GET_VOLUNTEER_REQ = 8;
	private static final String VOL_ID = "volunteerID";
	private static final String VOL_EMAIL = "it_title";

	

	private static final String FOLDER_ID = "f_id";
	private static final String FOLDER_TITLE = "f_title";

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

	private static final String ITEM_ID = "it_id";
	private static final String ITEM_TITLE = "it_title";
	private static final String ITEM_DESCRIPTION = "it_desc";
	private static final String ITEM_FOLDER_ID = "it_fid";
	private static final String FILE_NAME ="name";

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

					// == folder apis
					case GET_ALL_FOLDERS_JSON_REQ: {

						conn = ConnPool.getInstance().getConnection();
						FoldersResProvider foldersResProvider = new FoldersResProvider();
						List<Folder> foldersList = foldersResProvider
								.getAllFolders(conn);
						String resultJson = Folder.toJson(foldersList);

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

					case INSERT_FOLDER_REQ: {
						String id = req.getParameter(FOLDER_ID);
						String title = req.getParameter(FOLDER_TITLE);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						FoldersResProvider foldersResProvider = new FoldersResProvider();

						Folder folder = new Folder(id, title);
						if (foldersResProvider.insertFolder(folder, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					case DELETE_FOLDER_REQ: {
						String id = req.getParameter(FOLDER_ID);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						FoldersResProvider foldersResProvider = new FoldersResProvider();

						Folder folder = new Folder(id);
						if (foldersResProvider.deleteFolder(folder, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}
					// == end folder apis

					// == item apis
					case INSERT_ITEM_REQ: {
						String id = req.getParameter(ITEM_ID);

						String title = req.getParameter(ITEM_TITLE);

						String descrpition = req.getParameter(ITEM_DESCRIPTION);

						String folderId = req.getParameter(ITEM_FOLDER_ID);

						ServletInputStream isServ = req.getInputStream();

						DataInputStream is = new DataInputStream(isServ);

						ByteArrayOutputStream bin = new ByteArrayOutputStream(
								2048);
						int data;
						while ((data = is.read()) != -1) {
							bin.write((byte) data);
						}

						byte[] imageBlob = bin.toByteArray();

						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						ItemsResProvider itemsResProvider = new ItemsResProvider();

						Item item = new Item(id, title, descrpition, imageBlob,
								folderId);
						if (itemsResProvider.insertItem(item, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					case DELETE_ITEM_REQ: {
						String id = req.getParameter(ITEM_ID);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						ItemsResProvider itemsResProvider = new ItemsResProvider();
						Item item = new Item(id);
						if (itemsResProvider.deleteItem(item, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					case GET_ITEMS_OF_FOLDER_JSON_REQ: {

						String id = req.getParameter(FOLDER_ID);
						conn = ConnPool.getInstance().getConnection();
						ItemsResProvider itemsResProvider = new ItemsResProvider();
						Folder folder = new Folder(id);
						List<Item> itemsList = itemsResProvider.getAllItems(
								folder, conn);
						String resultJson = Item.toJson(itemsList);

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

					case GET_ITEM_IMAGE_REQ: {
						String id = req.getParameter(ITEM_ID);
						respPage = RESOURCE_FAIL_TAG;

						conn = ConnPool.getInstance().getConnection();
						ItemsResProvider itemsResProvider = new ItemsResProvider();

						byte[] imgBlob = itemsResProvider.getImage(id, conn);

						if (imgBlob != null && imgBlob.length > 0) {
							ServletOutputStream os = resp.getOutputStream();
							os.write(imgBlob);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}
					
					case GET_FILE_FROM_FILESYSTEM_REQ: {
						String fileName = req.getParameter(FILE_NAME);
						
						File file = new File(FilesUtils.appDirName,fileName);
						
						byte[] blob = FilesUtils.getLocalCopy(file);

						if (blob != null && blob.length > 0) {
							ServletOutputStream os = resp.getOutputStream();
							os.write(blob);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}

					// == end items apis

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
