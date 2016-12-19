package cn.itcast.test.dao.impl;

import java.io.Serializable;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.itcast.test.dao.TestDAO;
import cn.itcast.test.entity.Person;

/*继承HibernateDaoSupport直接用它的方法，很方便也应该这样做,
  但我们要注意在spring中为这个类配置一个sessionfactory的property
*/
public class TestDAOImpl extends HibernateDaoSupport implements TestDAO{
	
	/**
	 * 保存人员
	 */
	@Override
	public void save(Person person) {
		getHibernateTemplate().save(person);
	}
	/**
	 * 根据id查询人员
	 */
	@Override
	public Person findPerson(Serializable id) {
		return getHibernateTemplate().get(Person.class, id);
	}

}
