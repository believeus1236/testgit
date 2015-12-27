package cn.believeus.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**bannar 图片管理*/
@Entity
@Table
public class Tbannar extends TbaseEntity {

	private static final long serialVersionUID = 1444444234014928908L;
	/**描述*/
	private String descr;
	/**访问链接地址*/
	private String url;
	/**图片连接地址*/
	private String imgpath;


	

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	
}
