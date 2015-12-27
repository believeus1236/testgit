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
import cn.believeus.model.Tbannar;
import cn.believeus.service.IService;
import cn.believeus.service.MySQLService;

@Controller
public class AdminBannarController {
	private static final Log log=LogFactory.getLog(AdminBannarController.class);
	
	@Resource
	private MydfsTrackerServer mydfsTrackerServer;
	@Resource
	private IService mysqlService;

	@RequestMapping(value = "/admin/bannar/addView")
	public ModelAndView addView() {
		ModelAndView modelView = new ModelAndView();
		modelView.setViewName("/WEB-INF/back/bannar/add.jsp");
		return modelView;
	}

	@RequestMapping(value="/admin/bannar/save")
	public String save(Tbannar bannar,HttpServletRequest request) {
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
		bannar.setImgpath(storepath);
		mysqlService.saveOrUpdate(bannar);
		return "redirect:/admin/bannar/list.jhtml";
	}

	
	@RequestMapping(value = "/admin/bannar/list")
	public String list(HttpServletRequest request){
		String pageNumber = request.getParameter("pageNumber");
		// 如果为空，则设置为1
		if (StringUtils.isEmpty(pageNumber)) {
			pageNumber="1";
		}
		Pageable pageable=new Pageable(Integer.valueOf(pageNumber),10);
		String hql="From Tbannar";
		Page<?> page = ((MySQLService)mysqlService).findObjectList(hql, pageable);
		request.setAttribute("page",page);
		request.setAttribute("size",page.getTotal());
		// 分页
		PaginationUtil.pagination(request, page.getPageNumber(),page.getTotalPages(), 0);

		return "/WEB-INF/back/bannar/list.jsp";
	}
	
	@RequestMapping(value="/admin/bannar/edit/{id}")
	public ModelAndView edit(@PathVariable(value="id") Integer id){
		ModelAndView modelView=new ModelAndView("/WEB-INF/back/bannar/edit.jsp");
		Tbannar bannar = (Tbannar) ((MySQLService)mysqlService).findObject(Tbannar.class, id);
		modelView.addObject("bannar", bannar);
		return modelView;
	}
	/**
	 * 媒体更新
	 * @return
	 * */
	@RequestMapping(value="/admin/bannar/update")
	public String update(HttpServletRequest request,Tbannar banner){
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
				banner.setImgpath(storepath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mysqlService.saveOrUpdate(banner);
		return "redirect:/admin/bannar/list.jhtml";
	}
	
	/**
	 * 媒体删除
	 * @return
	 */
	@RequestMapping(value="/admin/bannar/delete")
	public @ResponseBody String delete(Integer[] ids){
		List<Integer> list = Arrays.asList(ids); 
		mysqlService.delete(Tbannar.class, list);
		return "{\"type\":\"success\"}";
	}
}
