1.mybatis������
 <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE configuration
    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <settings>
	<setting name="lazyLoadingEnabled" value="false" />
	<setting name="aggressiveLazyLoading" value="false" />
	<setting name="lazyLoadingEnabled" value="false" />
	<setting name="aggressiveLazyLoading" value="true" />
	<setting name="cacheEnabled" value="false" />
  </settings>
  <typeAliases>
		<!-- ʹ�ð�ɨ��,���Լ򻯷���ֵ �������ֱ����resultType="admin" ����Ҫд��ȫ������-->
		<package name="cn.believeus.model"/>
	</typeAliases>
  <mappers>
	<mapper resource="model-mapper-xml/admin-mapper.xml"/>
  </mappers>
</configuration>

2.spring����
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="configLocation" value="classpath:mybatis-config.xml" />
</bean>
<bean  class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	<property name="sqlSessionFactory" ref="sqlSessionFactory" />
	<property name="basePackage" value="cn.believeus.service;" />
</bean>

3.��дӳ���ļ�,namespace���������ӳ��ӿڱ���һ��
<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper  
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 <!-- �˴�����ͽӿڵ�����һ�� -->
<mapper namespace="cn.believeus.mybatis.service.AdminServiceMapper">  
    <select id="findById" parameterType="int" resultType="admin">
    	select * from admin where id=#{id}
    </select>
</mapper> 

4.��дӳ��ӿ�,ӳ��ӿڱ����ӳ���ļ���namespace����һ��
package cn.believeus.service;

public interface MyBatisService {
	public Object findById(int id);
}
