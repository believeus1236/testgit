package cn.believeus.admin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import mydfs.storage.server.MydfsTrackerServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Assert;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.believeus.PaginationUtil.Page;
import cn.believeus.PaginationUtil.Pageable;
import cn.believeus.PaginationUtil.PaginationUtil;
import cn.believeus.model.MediaCategory;
import cn.believeus.model.Tmedia;
import cn.believeus.service.IService;
import cn.believeus.service.MySQLService;

@Controller
public class AdminMediaController {
	
	private static final Log log=LogFactory.getLog(AdminMediaController.class);
	
	@Resource
	private MydfsTrackerServer mydfsTrackerServer;
	
	@Resource
	private IService mysqlService;

	/**
	 * 媒体列表
	 * @return
	 */
	@RequiresPermissions("mediaDinamic:view")
	@RequestMapping(value="/admin/media/list")
	public String list(HttpServletRequest request){
		String pageNumber = request.getParameter("pageNumber");
		// 如果为空，则设置为1
		if (StringUtils.isEmpty(pageNumber)) {
			pageNumber="1";
		}
		Pageable pageable=new Pageable(Integer.valueOf(pageNumber),10);
		String hql="SELECT new Tmedia(t.id,t.imgpath,t.title,t.editTime,t.coin,CONCAT(c2.categoryName,' --> ',c1.categoryName) as type) From Tmedia t,MediaCategory c1,MediaCategory c2 where t.type=c1.id and c1.parentId=c2.id";
		//String hql="From Tmedia";
		Page<?> page = ((MySQLService)mysqlService).findObjectList(hql, pageable);
		request.setAttribute("page",page);
		request.setAttribute("size",page.getTotal());
		// 分页
		PaginationUtil.pagination(request, page.getPageNumber(),page.getTotalPages(), 0);

		return "/WEB-INF/back/media/list.jsp";
	}
	
	/**
	 * 媒体添加
	 * @return
	 */
	@RequiresPermissions("mediaDinamic:create")
	@RequestMapping(value="/admin/media/add")
	public String add(){
		return "/WEB-INF/back/media/add.jsp";
	}
	
	/**
	 * 媒体保存
	 * @return
	 * */
	@RequiresPermissions("mediaDinamic:create")
	@RequestMapping(value="/admin/media/save")
	public String save(HttpServletRequest request,Tmedia media){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String storepath = "";
		Map<String, MultipartFile> files = multipartRequest.getFileMap();
		for (MultipartFile file : files.values()) {
			InputStream inputStream;
			try {
				inputStream = file.getInputStream();
				if(inputStream.available()==0){
					break;
				}
				Assert.assertNotNull("upload file InputStream is null", inputStream);
				String originName=file.getOriginalFilename();
				String extention = originName.substring(originName.lastIndexOf(".") + 1);
				log.debug("upload file stuffix:"+extention);
				storepath += mydfsTrackerServer.upload(inputStream, extention);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		media.setImgpath(storepath);
		mysqlService.saveOrUpdate(media);
		return "redirect:/admin/media/list.jhtml";
	}
	
	/**
	 * /admin/media/edit/1.jhtml
	 * @return
	 */
	@RequiresPermissions("mediaDinamic:update")
	@RequestMapping(value="/admin/media/edit/{id}")
	public ModelAndView edit(@PathVariable(value="id") Integer id){
		ModelAndView modelView=new ModelAndView("/WEB-INF/back/media/edit.jsp");
		Tmedia media = (Tmedia) ((MySQLService)mysqlService).findObject(Tmedia.class, id);
		MediaCategory mCategory = (MediaCategory) ((MySQLService)mysqlService).findObject(MediaCategory.class, Integer.valueOf(media.getType()));
		modelView.addObject("media", media);
		modelView.addObject("mediaCategoryParentId", mCategory.getParentId());
		return modelView;
	}
	/**
	 * 媒体更新
	 * @return
	 * */
	@RequiresPermissions("mediaDinamic:create")
	@RequestMapping(value="/admin/media/update")
	public String update(HttpServletRequest request,Tmedia media){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = multipartRequest.getFileMap();
		for (MultipartFile file : files.values()) {
			InputStream inputStream;
			try {
				inputStream = file.getInputStream();
				if(inputStream.available()==0){
					break;
				}
				Assert.assertNotNull("upload file InputStream is null", inputStream);
				String originName=file.getOriginalFilename();
				String extention = originName.substring(originName.lastIndexOf(".") + 1);
				log.debug("upload file stuffix:"+extention);
				String storepath = mydfsTrackerServer.upload(inputStream, extention);
				media.setImgpath(storepath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mysqlService.saveOrUpdate(media);
		return "redirect:/admin/media/list.jhtml";
	}
	
	/**
	 * 媒体删除
	 * @return
	 */
	@RequiresPermissions("mediaDinamic:delete")
	@RequestMapping(value="/admin/media/delete")
	public @ResponseBody String delete(Integer[] ids){
		List<Integer> list = Arrays.asList(ids); 
		mysqlService.delete(Tmedia.class, list);
		return "{\"type\":\"success\"}";
	}
	
	/**
	 * 媒体置顶
	 * @return
	 */
	@RequiresPermissions("mediaDinamic:update")
	@RequestMapping(value="/admin/media/top")
	public String mediaTop(Integer myNewId, HttpServletRequest request){
		return "redirect:/admin/media/list.jhtml";
	}

}