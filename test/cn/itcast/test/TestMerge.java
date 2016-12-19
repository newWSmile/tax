 package cn.itcast.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.test.entity.Person;
import cn.itcast.test.service.TestService;

public class TestMerge {	
	ClassPathXmlApplicationContext cxt;
	@Before
	public void loadCtx(){
		cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	@Test
	public void testSpring(){
		TestService testService =  (TestService) cxt.getBean("testService");
		testService.say();
	}
	
	@Test
	public void testHibernate(){
		SessionFactory sessionFactory =  (SessionFactory) cxt.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		//保存人员
		session.save(new Person("人员1"));
		transaction.commit();
		session.close();
	}
	@Test
	public void testServiceAndDao(){
		TestService testService =  (TestService) cxt.getBean("testService");
		testService.save(new Person("人员2"));
		System.out.println(testService.findPerson("297e12795910a1c0015910a1c1630000").getName());
	}
	@Test
	public void testTransationReadOnly(){//测试只读事务，如果在只读事务中出现跟新操作则做回滚操作
		
		TestService testService =  (TestService) cxt.getBean("testService");
		System.out.println(testService.findPerson("297e12795910a1c0015910a1c1630000").getName());
	}
	
	@Test
	public void testTransationRollback(){//测试只读事务，如果在只读事务中出现有任何异常则回滚显现的操作
		TestService testService =  (TestService) cxt.getBean("testService");
		testService.save(new Person("人员4"));
	}
	
	
	
	
}
