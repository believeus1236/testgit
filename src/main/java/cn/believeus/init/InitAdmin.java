package cn.believeus.init;

import javax.annotation.Resource;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import cn.believeus.model.Admin;
import cn.believeus.model.Authority;
import cn.believeus.model.Role;
import cn.believeus.service.IService;
import cn.believeus.variables.Variables;

@Component
public class InitAdmin implements ApplicationListener<ApplicationEvent>{
	
	@Resource
	private IService mysqlService;
	
	// tomcat第一次启动会调用这个方法
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// tomcat启动完毕调用该方法
		if(event instanceof ContextRefreshedEvent){
			Admin admin = (Admin)mysqlService.findObject(Admin.class, Variables.USER_NAME, "admin");
			if(admin == null){
				 admin = new Admin();
				 admin.setUsername("admin");
				 admin.setPassword("admin");
				 admin.setDescription("该管理员拥有所有权限");
				 mysqlService.saveOrUpdate(admin);
				 Role role=new Role();
				 role.setDescription("该角色拥有所有权限");
				 role.setRoleName("root");
				 role.setAdmin(admin);
				 mysqlService.saveOrUpdate(role);
				 Authority authority=new Authority();
				 authority.setPermission("*");
				 authority.setRole(role);
				 mysqlService.saveOrUpdate(authority);
			}
		}
	}
}
