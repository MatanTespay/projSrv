package com.haifa.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haifa.database.operations.ConnPool;
import com.haifa.database.operations.FoldersResProvider;
import com.haifa.objects.Folder;
import com.haifa.utils.FilesUtils;

/**
 * Servlet implementation class WebFoldersManageServlet
 */


public class WebFoldersManageServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	
	public static final int DB_RETRY_TIMES=5;

	
	
	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";
	
	private static final String FOLDER_ID = "f_id";
	private static final String FOLDER_TITLE = "f_title";
	private static final String IS_DELETE = "delete";


   

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

		PrintWriter pw = resp.getWriter();
		
		int retry = DB_RETRY_TIMES;
		Connection conn = null;
		
		
		
		String folderId = null;
		String folderTitle = null;
		
		boolean isDelete = false;
		
		String respPage = RESOURCE_FAIL_TAG;
		try {
			
			System.out.println("=======Folder Servlet =======");
	
			
			 folderId = req.getParameter(FOLDER_ID);
			 folderTitle = req.getParameter(FOLDER_TITLE);			
			 String isDeleteParam = req.getParameter(IS_DELETE);
			 isDelete = Boolean.parseBoolean(isDeleteParam);
			
			while (retry > 0) {

				try {

				
					conn = ConnPool.getInstance().getConnection();
					
					FoldersResProvider folderResProvider = new FoldersResProvider();
					Folder folder = new Folder(folderId, folderTitle);
					
					if(isDelete){
						
						
						if(folderResProvider.deleteFolder(folder, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
						}
						
					}
					else{
						if(folderResProvider.insertFolder(folder, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
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
			
			pw.write(respPage);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
