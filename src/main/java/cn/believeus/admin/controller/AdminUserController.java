package cn.believeus.admin.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import mydfs.storage.server.MydfsTrackerServer;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.believeus.PaginationUtil.Page;
import cn.believeus.PaginationUtil.Pageable;
import cn.believeus.PaginationUtil.PaginationUtil;
import cn.believeus.model.MediaCategory;
import cn.believeus.model.Tuser;
import cn.believeus.service.IService;
import cn.believeus.service.MySQLService;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
	//private static final Log log=LogFactory.getLog(AdminMediaCategoryController.class);
	
	@Resource
	private MydfsTrackerServer mydfsTrackerServer;
	@Resource
	private IService mysqlService;

	@RequestMapping(value = "/list")
	public ModelAndView list(HttpServletRequest request){
		ModelAndView modelView=new ModelAndView();
		// 如果为空，则设置为1
		String pageNumber=request.getParameter("pageNumber");
		if (StringUtils.isEmpty(pageNumber)) {
			pageNumber="1";
		}
		Pageable pageable=new Pageable(Integer.valueOf(pageNumber),10);
		String hql="From Tuser";
		Page<?> page = ((MySQLService)mysqlService).findObjectList(hql, pageable);
		modelView.addObject("page",page);
		modelView.addObject("size",page.getTotal());
		// 分页
		PaginationUtil.pagination(request, page.getPageNumber(),page.getTotalPages(), 0);
        modelView.setViewName("/WEB-INF/back/user/list.jsp");
		return modelView;
	}
	
	@RequestMapping(value="/view/{id}")
	public ModelAndView edit(@PathVariable(value="id") Integer id){
		ModelAndView modelView=new ModelAndView("/WEB-INF/back/user/edit.jsp");
		Tuser user = (Tuser) ((MySQLService)mysqlService).findObject(Tuser.class, id);
		modelView.addObject("user", user);
		return modelView;
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
