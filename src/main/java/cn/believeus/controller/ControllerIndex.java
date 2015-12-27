package cn.believeus.controller;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.believeus.model.Tbannar;
import cn.believeus.service.IService;
import cn.believeus.service.MyBatisService;

/**
 * 首页面
 * */
@Controller
public class ControllerIndex {
	
	@Resource
	private IService mysqlService;
	@Resource
	private MyBatisService batisService;

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView modelView=new ModelAndView();
		//获取bannar图片
		List<?> bannarList = mysqlService.findObjectList(Tbannar.class);
		modelView.addObject("bannarList", bannarList);
		modelView.setViewName("/WEB-INF/front/index.jsp");
		return modelView;
	}
	@RequestMapping(value="/showuser")
	public @ResponseBody String showuser(String deviceid,String devicetype){
		 String result=deviceid+":"+devicetype;
		 return result;
	}
	@RequestMapping(value = "/goods-{id}")
	public ModelAndView list(@PathVariable String id){
		ModelAndView modelView=new ModelAndView();
		modelView.setViewName("/WEB-INF/front/list.jsp");
		return modelView;
		
	}
	@ResponseBody
	@RequestMapping(value = "/categoryTree", produces = "text/html;charset=UTF-8")
	public  String categoryTree(String parentId){
		List<Map<String,Object>> categoryTree = batisService.findMediaCategoryTree(parentId);
		JSONArray fromObject = JSONArray.fromObject(categoryTree);
		return fromObject.toString();
	}
}
