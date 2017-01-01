package cn.itcast.core.permission.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.itcast.core.permission.PermissionCheck;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.role.entity.RolePrivilege;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.service.UserService;

public class PermissionCheckImpl implements PermissionCheck {
	
	@Resource
	private UserService userService;
	
	
	@Override
	public boolean isAccessiable(User user, String code) {
		//1 获取用户的所有角色
		List<UserRole> list = user.getUserRoles();
		if (list==null) {
			list = userService.getUserRolesByUserId(user.getId());
		}
		//2根据每个角色对应的所有权限进行对比
		if (list!=null && list.size()>0) {
			for (UserRole ur :list) {
				Role role = ur.getId().getRole();
				for (RolePrivilege rp :role.getRolePrivileges()) {
					//对比是否有code对应的权限
					if (code.equals(rp.getId().getCode())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
