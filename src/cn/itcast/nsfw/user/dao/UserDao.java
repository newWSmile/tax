package cn.itcast.nsfw.user.dao;

import java.util.List;

import cn.itcast.core.dao.BaseDao;
import cn.itcast.nsfw.user.entity.User;

public interface UserDao extends BaseDao<User>{
	/**
	 * 根据账号和用户id查询用户
	 * @param id 用户ID
	 * @param account 用户账号
	 * @return 用户列表
	 */
	List<User> findUserByAccountAndId(String id, String account);

}
