package cn.itcast.nsfw.user.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.validator.util.NewInstance;

import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	@Resource
	private UserService userService;
	
	private List<User> userList;
	private User user;
	private String[] selectedRow;
	private File headImg;
	private String headImgContentType;
	private String headImgFileName;
	
	private File userExcel;
	private String userExcelContentType;
	private String userExcelFileName;

	//列表页面
	public String listUI(){
		userList = userService.findObjects();
		return "listUI";
	}
	
	//跳转到新增页面
	public String addUI(){
		return "addUI";
	}
	
	//保存新增
	public String add(){
		try {
			if(null!=user){
				//处理头像
				if(headImg!=null){
					//1 保存头像到upload/user
					//获取保存路径的绝对地址
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName = UUID.randomUUID().toString().replaceAll("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					//复制文件
					FileUtils.copyFile(headImg, new File(filePath,fileName));
					//2 设置用户头像路径
					user.setHeadImg("user/"+fileName);//类似于user/123.jpg
				}
				userService.save(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	//跳转到编辑页面
	public String editUI(){
		if (user!=null && user.getId()!=null) {
			user = userService.findObjectById(user.getId());
		}
		return "editUI";
	}
	
	//保存编辑
	public String edit(){
		try {
			if(null!=user){
				//处理头像
				if(headImg!=null){
					//1 保存头像到upload/user
					//获取保存路径的绝对地址
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					//删除原来的头像
					System.out.println(filePath);
					File dir = new File(filePath+"/"+user.getId());
					if (dir.isDirectory()) {
						String[] deleteFileNames = dir.list();
						System.out.println(deleteFileNames.length);
						for(int i=0;i<deleteFileNames.length;i++){
							System.out.println(filePath+"/"+user.getId()+deleteFileNames[i]);
							new File(filePath+"/"+user.getId()+"/"+deleteFileNames[i]).delete();
						}
					}
					String fileName = UUID.randomUUID().toString().replaceAll("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					//复制文件
					String newHeadImgFileName = filePath+"/"+user.getId()+"/"+fileName;
					FileUtils.copyFile(headImg, new File(newHeadImgFileName));
					//2 设置用户头像路径
					user.setHeadImg("user/"+user.getId()+"/"+fileName);//类似于user/123.jpg
				}
				userService.update(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	//删除
	public String delete(){
		if (user!=null && user.getId()!=null) {
			userService.delete(user.getId());
		}
		return "list";
	}
	//批量删除
	public String deleteSelected(){
//		System.out.println("进入批量删除代码!!!!");
//		System.out.println("****************"+selectedRow);
		if(selectedRow!=null){
			for(String id :selectedRow ){
				userService.delete(id);
			}
		}
		return "list";
	}
	//导出用户列表
	public void exportExcel(){
		try {
			//1查找用户列表
			userList = userService.findObjects();
			//2导出
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/x-execl");
			response.setHeader("Content-Disposition", "attachment;filename="+new String("用户列表.xls".getBytes(),"iso-8859-1"));
			ServletOutputStream outputStream = response.getOutputStream();
			userService.exportExcel(userList,outputStream);
			if (outputStream!=null) {
				outputStream.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//导入用户列表
	public String importExcel(){
		//1获取excel文件
		if (userExcel!=null) {
			//是否是excel
			if(userExcelFileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
				//导入
				userService.importExcel(userExcel,userExcelFileName);
			}
		}
		
		return "list";
	}
	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String[] getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(String[] selectedRow) {
		this.selectedRow = selectedRow;
	}

	public File getHeadImg() {
		return headImg;
	}

	public void setHeadImg(File headImg) {
		this.headImg = headImg;
	}

	public String getHeadImgContentType() {
		return headImgContentType;
	}

	public void setHeadImgContentType(String headImgContentType) {
		this.headImgContentType = headImgContentType;
	}

	public String getHeadImgFileName() {
		return headImgFileName;
	}

	public void setHeadImgFileName(String headImgFileName) {
		this.headImgFileName = headImgFileName;
	}

	public File getUserExcel() {
		return userExcel;
	}

	public void setUserExcel(File userExcel) {
		this.userExcel = userExcel;
	}

	public String getUserExcelContentType() {
		return userExcelContentType;
	}

	public void setUserExcelContentType(String userExcelContentType) {
		this.userExcelContentType = userExcelContentType;
	}

	public String getUserExcelFileName() {
		return userExcelFileName;
	}

	public void setUserExcelFileName(String userExcelFileName) {
		this.userExcelFileName = userExcelFileName;
	}
	
	
	
	
}
