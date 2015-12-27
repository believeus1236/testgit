package cn.believeus.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class MediaCategory extends TbaseEntity {
	
	private static final long serialVersionUID = 4649312197323888319L;
	private String categoryName;  // 分类名称
	private String parentId;      // 父类ID
	
	public MediaCategory(){
		super();
	}
	
	public MediaCategory(Integer id,String categoryName,String parentId){
		super();
		this.id=id;
		this.categoryName=categoryName;
		this.parentId=parentId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}