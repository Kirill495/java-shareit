package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {

	public static void main(String[] args)   {

//		Tomcat tomcat = new Tomcat();
//		tomcat.setSilent(true);
//		tomcat.getConnector().setPort(8080);
//
//		Context tomcatContext = tomcat.addContext("", null);
//
//		AnnotationConfigWebApplicationContext applicationContext =
//						new AnnotationConfigWebApplicationContext();
//		applicationContext.scan("ru.practicum");
//		applicationContext.setServletContext(tomcatContext.getServletContext());
//		applicationContext.refresh();
//
//		// добавляем диспетчер запросов
//		DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
//		Wrapper dispatcherWrapper =
//						Tomcat.addServlet(tomcatContext, "dispatcher", dispatcherServlet);
//		dispatcherWrapper.addMapping("/");
//		dispatcherWrapper.setLoadOnStartup(1);
//
//		tomcat.start();
		SpringApplication.run(ShareItApp.class, args);
	}

}
