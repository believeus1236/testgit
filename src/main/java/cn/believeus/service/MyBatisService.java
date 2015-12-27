package cn.believeus.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MyBatisService {
	public Object findById(int id);
	public List<Map<String, Object>> findMediaCategoryList(@Param("pageStart")Integer page,@Param("pageSize")Integer size);
	public List<Map<String, Object>> findMediaCategoryTree(@Param("parentId")String parentId);
}
