package cn.itcast.nsfw.role.dao;

import cn.itcast.core.dao.BaseDao;
import cn.itcast.nsfw.role.entity.Role;

public interface RoleDao extends BaseDao<Role> {
	//1删除改角色对应的所有权限
	public void deleteRolePrivilegeByRoleId(String roleId);

}
