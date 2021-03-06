package cn.itcast.nsfw.user.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.exception.ActionException;
import cn.itcast.core.exception.ServiceException;
import cn.itcast.core.exception.SysException;
import cn.itcast.nsfw.role.service.RoleService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends BaseAction {
	@Resource
	private UserService userService;
	
	private List<User> userList;
	private User user;
	
	private File headImg;
	private String headImgContentType;
	private String headImgFileName;
	
	private File userExcel;
	private String userExcelContentType;
	private String userExcelFileName;
	@Resource
	private RoleService roleService;
	
	private String[] userRoleIds;
	//列表页面
	public String listUI() throws Exception{
		try {
			userList = userService.findObjects();
		} catch (ServiceException e) {
			throw new Exception(e.getMessage());
		}
//		return "error";
		return "listUI";
	}
	
	//跳转到新增页面
	public String addUI(){
		//加载角色列表
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
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
				userService.saveUserAndRole(user,userRoleIds);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	//跳转到编辑页面
	public String editUI(){
		//加载角色列表
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
		if (user!=null && user.getId()!=null) {
			user = userService.findObjectById(user.getId());
			//处理角色回显
			List<UserRole> list = userService.getUserRolesByUserId(user.getId());
			if (list!=null && list.size()>0) {
				userRoleIds = new String[list.size()];
				for (int i=0;i<list.size();i++) {
					userRoleIds[i] =list.get(i).getId().getRole().getRoleId();
				}
			}
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
				userService.updateUserAndRole(user,userRoleIds);
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
	
	//校验用户账号唯一
	public void verifyAccount(){
		System.out.println("##########进入判断账号唯一");
		try {
			//1获取账号
			if(user!=null && StringUtils.isNotBlank(user.getAccount())){
				//2根据账号到数据库中校验是否存在改账号对应的用户
				List<User> list = userService.findUserByAccountAndId(user.getId(),user.getAccount());
				String strResult = "true";
				if(list!=null && list.size()>0){
					//说明该账号已存在
					strResult = "false";
				}
				//输出
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(strResult.getBytes());
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

	public String[] getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(String[] userRoleIds) {
		this.userRoleIds = userRoleIds;
	}
	
	
	
	
	
	
}
