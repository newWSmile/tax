package cn.itcast.test.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.test.dao.TestDAO;
import cn.itcast.test.entity.Person;
import cn.itcast.test.service.TestService;
@Service("testService")
public class TestServiceImpl implements TestService{
	@Resource
	private TestDAO testDAO;

	@Override
	public void say() {
		System.out.println("service层输出测试");
	}

	@Override
	public void save(Person person) {
		testDAO.save(person);
		int i = 1/0;
	}

	@Override
	public Person findPerson(Serializable id) {
		save(new Person("test"));
		return testDAO.findPerson(id);
	}

}
