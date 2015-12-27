package cn.believeus.service.test;

import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.believeus.model.Admin;
import cn.believeus.service.IService;
import cn.believeus.service.MongoService;

/**mongoDB 增删改查单元测试*/
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/applicationContext.xml")  
public class MongoServiceTest {
	@Resource(name="mongoService")
	private IService mongoService;
	/**mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testSave */
	@Test
	public void testSave(){
		for (int i = 0; i < 14; i++) {
			Admin admin=new Admin();
			admin.setId(i);
			admin.setPassword("admin_"+i);
			admin.setUsername("admin_"+i);
			admin.setDescription("administartor_"+i);
			mongoService.saveOrUpdate(admin);
		}
	}
	/*查询单个对象*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testFindObject*/
	@Test
	public void testFindObject(){
		Admin admin = (Admin) mongoService.findObject(Admin.class, 1);
		System.out.println("admin:username:"+admin.getUsername());
	}
	/*根据条件查询对象*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testFindByProperty*/
	@Test
	public void testFindByProperty(){
		Admin admin = (Admin)mongoService.findObject(Admin.class, "username", "admin_1");
		System.out.println("admin.username="+admin.getUsername());
	}
	/*根据条件查询对象*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testFindByMultiProperty*/
	@Test
	public void testFindByMultiProperty(){
		@SuppressWarnings("unchecked")
		List<Admin> admins = (List<Admin>)mongoService.findObjectList(Admin.class, "username", "admin_2","password","admin_2");
		for (Admin admin : admins) {
			System.out.println("admin.username:"+admin.getUsername());
		}
	}
	/*查询制定个数*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testLimit*/
	@Test
	public void testLimit(){
		@SuppressWarnings("unchecked")
		List<Admin> list = (List<Admin>) mongoService.findObjectList(Admin.class, 5);
		for (Admin admin : list) {
			System.out.println("admin.username="+admin.getUsername()+",admin.password:"+admin.getPassword());
		}
	}
	/*查询所有*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testFindAll*/
	@SuppressWarnings("unchecked")
	@Test
	public void testFindAll(){
		List<Admin> list = (List<Admin>) mongoService.findObjectList(Admin.class);
		for (Admin admin : list) {
			System.out.println("admin.username="+admin.getUsername()+",admin.password:"+admin.getPassword());
		}
	}
	
	/*删除对象*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testDeleteById*/
	@Test
	public void testDeleteById(){
		mongoService.delete(Admin.class, 1);
	}
	/*根据单个条件删除对象*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testDeleteByProperty*/
	@Test
	public void testDeleteByProperty() {
		mongoService.delete(Admin.class, "username", "admin_0");
	}
	/*更新操作*/
	/*mvn clean compile test -Dtest=cn.believeus.service.test.MongoServiceTest#testSaveOrUpdate*/
	@Test
	public void testSaveOrUpdate(){
		
		Admin admin = (Admin)((MongoService)mongoService).findObject(Admin.class, 5);
		admin.setUsername("root");
		mongoService.saveOrUpdate(admin);
		
	}
}
