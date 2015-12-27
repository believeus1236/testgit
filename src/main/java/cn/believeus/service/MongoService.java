package cn.believeus.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.believeus.dao.IBaseDao;
import cn.believeus.dao.MongoDao;

@Service(value="mongoService")
public class MongoService implements IService{
	@Resource(name="mongoDao")
	private IBaseDao mongoDao;
	

	@Override
	public void saveOrUpdate(Object object) {
		((MongoDao)mongoDao).saveOrUpdate(object);
	}
	
	@Override
	public void delete(Class<?> clazz, Integer id) {
		mongoDao.delete(clazz, id);
	}

	@Override
	public void delete(Object entity) {
		mongoDao.delete(entity);
	}

	@Override
	public void delete(Class<?> clazz, List<?> ids) {
		mongoDao.delete(clazz, ids);
	}

	@Override
	public void delete(Class<?> clazz, String property, Object value) {
		mongoDao.delete(clazz, property, value);
	}

	/** 该方法不能根据id查询,根据id查询是查不到的*/
	@Override
	public Object findObject(Class<?> clazz, Object property, Object value) {
		Object object = mongoDao.findObject(clazz, property, value);
		return object;
	}

	@Override
	public Object findObject(Class<?> clazz, Integer id) {
		Object object = mongoDao.findObject(clazz,id);
		return object;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, Object property, Object value) {
		List<?> list = mongoDao.findObjectList(clazz, property, value);
		return list;
	}


	@Override
	public List<?> findObjectList(Class<?> clazz, Object property1,
			Object value1, Object property2, Object value2) {
		List<?> list = mongoDao.findObjectList(clazz, property1, value1, property2, value2);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz) {
		List<?> list = mongoDao.findObjectList(clazz);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, Integer num) {
		List<?> list = mongoDao.findObjectList(clazz,num);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, String property,
			Object value, int num) {
		List<?> list=mongoDao.findObjectList(clazz,property,value,num);
		return list;
	}

}
