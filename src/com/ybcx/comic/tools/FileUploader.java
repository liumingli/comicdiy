package com.ybcx.comic.tools;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;


public class FileUploader extends HttpServlet {

	private static final long serialVersionUID = -543085089916376144L;

	private String assetPath; // 文件存放目录
	private String tempPath; // 临时文件目录
	
	// 最大文件上传尺寸设置
	private int fileMaxSize = 8 * 1024 * 1024;
	
	private Logger log = Logger.getLogger(FileUploader.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploader() {
		super();
	}

	// 初始化
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
			// 从配置文件中获得初始化参数
			assetPath = config.getInitParameter("assetpath");
			tempPath = config.getInitParameter("temppath");

			ServletContext context = getServletContext();

			assetPath = context.getRealPath(assetPath);
			tempPath = context.getRealPath(tempPath);

			File fp = new File(assetPath);
			if (!fp.exists())
				fp.mkdir();

			File tp = new File(tempPath);
			if (!tp.exists())
				tp.mkdir();

			log.debug("File storage directory, the temporary file directory is ready ...");
			log.debug("assetPah: " + assetPath);
			log.debug("tempPah: " + tempPath);
		}
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			// threshold 极限、临界值，即内存缓存 空间大小
			diskFactory.setSizeThreshold(fileMaxSize);
			// repository 贮藏室，即临时文件目录
			diskFactory.setRepository(new File(tempPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			// 设置允许上传的最大文件大小 4M
			upload.setSizeMax(fileMaxSize);
			
			// 解析HTTP请求消息头
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = upload.parseRequest(request);
			Iterator<FileItem> iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) {
					log.debug("Processing the contents of the form...");
				} else {
					log.debug("Upload file handling...");
					processUploadFile(item, pw);
				}
			}// end while()

			// close write to front end
			pw.close();

		} catch (SizeLimitExceededException e) {
			
			log.debug(">>>File size exceeds the limit, can not upload!");
			pw.print(">>> File size exceeds the limit, can not upload!");
			return;
			
		} catch (Exception e) {
			log.debug("Exception occurs when using fileupload package...");
			e.printStackTrace();
		}// end try ... catch ...
	}   	  	    
	
	// 处理上传的文件
	private void processUploadFile(FileItem item, PrintWriter pw)
		throws Exception {
			// 此时的文件名包含了完整的路径，得注意加工一下
			String fileName = item.getName();
			int dotPos = fileName.lastIndexOf(".");
			//文件类型
			String fileType = fileName.substring(dotPos+1).toLowerCase();
			
			if(fileType.equals("png") || fileType.equals("jpg") || fileType.equals("jpeg") 
					|| fileType.equals("gif") || fileType.equals("swf")){
				log.debug(">>>The current file type is:"+fileType);
			}else{
				// 返回客户端信息
				pw.print("reject");
				return;			
			}

			// 如果是用IE上传就需要处理下文件名，否则是全路径了
			if (fileName != null) {
				fileName = FilenameUtils.getName(fileName);
			}

			long fileSize = item.getSize();
	//		String sizeInK = (int) fileSize / 1024 + "K";

			if ("".equals(fileName) && fileSize == 0) {
				log.debug("fileName is null ...");
				return;
			}
			
			File uploadFile = null;
			if(fileType.equals("swf")){
				fileName = System.currentTimeMillis()+fileName.substring(dotPos).toLowerCase();
				uploadFile = new File(assetPath + File.separator + fileName);
				// 生成文件
				item.write(uploadFile);
			}else{
				uploadFile = new File(assetPath + File.separator + fileName);
				// 生成文件
				item.write(uploadFile);
			}
		
			
			log.debug(fileName + " File is complete ...");
			
			// 返回客户端信息
			pw.print(uploadFile.getAbsolutePath());
		}
}
