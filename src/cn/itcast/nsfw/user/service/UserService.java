package cn.itcast.nsfw.user.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.itcast.core.exception.ServiceException;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public interface UserService {
	/**
	 * 新增
	 */
	public void save(User user);
	
	/**
	 * 更新
	 */
	public void update(User user);
	/**
	 * 根据id删除
	 */
	public void delete(Serializable id);
	/**
	 * 根据id查找
	 */
	public User findObjectById(Serializable id);
	/**
	 * 查找列表
	 */
	public List<User> findObjects() throws ServiceException;
	/**
	 * 导出用户列表
	 * 
	 */
	public void exportExcel(List<User> userList,
			ServletOutputStream outputStream);

	public void importExcel(File userExcel, String userExcelFileName);

	/**
	 * 根据账号和用户id查询用户
	 * @param id 用户ID
	 * @param account 用户账号
	 * @return 用户列表
	 */
	public List<User> findUserByAccountAndId(String id, String account);
	
	//保存用户及其对应的角色
	public void saveUserAndRole(User user, String...  roleIds);
	//更新用户及其对应的角色
	public void updateUserAndRole(User user, String...  roleIds);
	//根据用户id获取该用户对应的所有用户角色
	public List<UserRole> getUserRolesByUserId(String id);
	//根据用户的账号密码查询用户列表
	public List<User> findUserByAccountAndPass(String account, String password);
}
