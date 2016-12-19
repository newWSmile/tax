package cn.itcast.test.service;

import java.io.Serializable;

import cn.itcast.test.entity.Person;

public interface TestService {
	
	
	//测试输出
	public void say();
	
	//保存人员
	public void save(Person person);
	
	//根据id查询人员
	public Person findPerson(Serializable id);
	
}
