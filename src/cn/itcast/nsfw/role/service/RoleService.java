package cn.itcast.nsfw.role.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.itcast.core.exception.ServiceException;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.user.entity.User;

public interface RoleService {
	/**
	 * 新增
	 */
	public void save(Role role);
	
	/**
	 * 更新
	 */
	public void update(Role role);
	/**
	 * 根据id删除
	 */
	public void delete(Serializable id);
	/**
	 * 根据id查找
	 */
	public Role findObjectById(Serializable id);
	/**
	 * 查找列表
	 */
	public List<Role> findObjects() ;
	
}
