package cn.believeus.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import mydfs.storage.server.MydfsTrackerServer;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.believeus.PaginationUtil.PaginationUtil;
import cn.believeus.model.MediaCategory;
import cn.believeus.service.IService;
import cn.believeus.service.MyBatisService;
import cn.believeus.service.MySQLService;

@Controller
@RequestMapping("/admin/mcategory")
public class AdminMediaCategoryController {
	//private static final Log log=LogFactory.getLog(AdminMediaCategoryController.class);
	
	@Resource
	private MydfsTrackerServer mydfsTrackerServer;
	@Resource
	private IService mysqlService;
	@Resource
	private MyBatisService batisService;

	@RequestMapping(value = "/add")
	public ModelAndView addView() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("/WEB-INF/back/mediaCategory/add.jsp");
		return modelView;
	}

	@RequestMapping(value="/save")
	public String save(MediaCategory mCategory) {
		mysqlService.saveOrUpdate(mCategory);
		return "redirect:/admin/mcategory/list.jhtml";
	}

	
	@RequestMapping(value = "/list")
	public ModelAndView list(HttpServletRequest request){
		ModelAndView modelView=new ModelAndView();
		// 如果为空，则设置为1
		String pageNumber = request.getParameter("pageNumber");
		Integer pageStart=1; 
		if (!StringUtils.isEmpty(pageNumber)) {
			pageStart=Integer.valueOf(pageNumber);
		}
		Integer pageSize=10;
		Integer pstart=(Integer.valueOf(pageStart)-1)*pageSize;
		List<Map<String,Object>> categoryList = batisService.findMediaCategoryList(pstart, pageSize);
		long totalSize = batisService.findMediaCategoryList(null, null).size();
		modelView.addObject("content",categoryList);
		modelView.addObject("size",totalSize);
		// 分页
		Integer totalPages=(int) Math.ceil((double)totalSize / (double)pageSize);
		if (totalPages < pstart) {
			pageStart=totalPages;
		}
		PaginationUtil.pagination(request, pageStart,totalPages, 0);
        modelView.setViewName("/WEB-INF/back/mediaCategory/list.jsp");
		return modelView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/categoryTree", produces = "text/html;charset=UTF-8")
	public  String categoryTree(String parentId){
		List<Map<String,Object>> categoryTree = batisService.findMediaCategoryTree(parentId);
		JSONArray fromObject = JSONArray.fromObject(categoryTree);
		return fromObject.toString();
	}
	
	@RequestMapping(value="/edit/{id}")
	public ModelAndView edit(@PathVariable(value="id") Integer id){
		ModelAndView modelView=new ModelAndView("/WEB-INF/back/mediaCategory/edit.jsp");
		MediaCategory mCategory = (MediaCategory) ((MySQLService)mysqlService).findObject(MediaCategory.class, id);
		modelView.addObject("mCategory", mCategory);
		return modelView;
	}
	/**
	 * 媒体更新
	 * @return
	 * */
	@RequestMapping(value="/update")
	public String update(HttpServletRequest request,MediaCategory mCategory){
		MediaCategory category = (MediaCategory) ((MySQLService)mysqlService).findObject(MediaCategory.class, mCategory.getId());
		category.setParentId(mCategory.getParentId());
		category.setCategoryName(mCategory.getCategoryName());
		mysqlService.saveOrUpdate(category);
		return "redirect:/admin/mcategory/list.jhtml";
	}
	
	/**
	 * 媒体删除
	 * @return
	 */
	@RequestMapping(value="/delete")
	public @ResponseBody String delete(Integer[] ids){
		List<Integer> list = Arrays.asList(ids); 
		mysqlService.delete(MediaCategory.class, list);
		return "{\"type\":\"success\"}";
	}
}
