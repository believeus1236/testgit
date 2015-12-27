package cn.believeus.dao;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class MongoDao implements IBaseDao {
	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public void saveOrUpdate(Object object) {
		mongoTemplate.save(object);
	}


	@Override
	public void delete(Class<?> clazz, List<?> idList) {
		for (Object id : idList) {
			Criteria criteria=Criteria.where((String)id).is(id);
			Query query=Query.query(criteria);
			mongoTemplate.remove(query, clazz);
		}
	}

	@Override
	public void delete(Class<?> clazz, Integer id) {
		Criteria criteria=Criteria.where("id").is(id);
		Query query=Query.query(criteria);
		mongoTemplate.remove(query, clazz);
	}

	@Override
	public void delete(Object entity) {
		mongoTemplate.remove(entity);
	}

	@Override
	public void delete(Class<?> clazz, String property, Object value) {
		Criteria criteria=Criteria.where((String)property).is(value);
		Query query=Query.query(criteria);
		mongoTemplate.remove(query, clazz);
	}

	/** 该方法不能根据id查询,根据id查询是查不到的*/
	@Override
	public Object findObject(Class<?> clazz, Object property, Object value) {
		Criteria criteria=Criteria.where((String)property).is(value);
		Query query=new Query(criteria);
		Object object = mongoTemplate.findOne(query, clazz);
		return object;
	}

	@Override
	public Object findObject(Class<?> clazz, Integer id) {
		 Object object=mongoTemplate.findById(id, clazz);
		 return object;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, Object property, Object value) {
		Criteria criteria = Criteria.where((String) property).is(value);
		Query query = Query.query(criteria);
		return mongoTemplate.find(query, clazz);
	}


	@Override
	public List<?> findObjectList(Class<?> clazz, Object property1,
			Object value1, Object property2, Object value2) {
		Criteria criteria = Criteria.where((String) property1).is(value1).and((String)property2).is(value2);
		Query query = Query.query(criteria);
		List<?> list = mongoTemplate.find(query, clazz);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz) {
		List<?> list = mongoTemplate.findAll(clazz);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, Integer num) {
		Query query=new Query().limit(num);
		List<?> list = mongoTemplate.find(query, clazz);
		return list;
	}

	@Override
	public List<?> findObjectList(Class<?> clazz, String property,
			Object value, int num) {
		Criteria criteria = Criteria.where((String) property).is(value);
		Query query = Query.query(criteria).limit(num);
		List<?> list = mongoTemplate.find(query, clazz);
		return list;
	}
}
