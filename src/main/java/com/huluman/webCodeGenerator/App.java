package com.huluman.webCodeGenerator;

import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.huluman.webCodeGenerator.model.Column;
import com.huluman.webCodeGenerator.model.Model;
import com.huluman.webCodeGenerator.model.Schema;
import com.huluman.webCodeGenerator.util.Util;

public class App {
	private String location;
	
	private String basePackageName;
	
	private VelocityEngine ve;
	
	public static void main(String[] args) {
		// TODO args judge
		App app = new App();
		
		app.location = "D:\\temp\\Generator\\src\\main\\java"; //args[0];
		app.basePackageName = "com.huluman.test"; // args[1]; // Package Name
		
		app.initVe();

		// Bean
		app.generateCommonBean();
		app.generateOtherBeans();
		
		// Dao
		app.generateCommonDao();
		app.generateOtherDaos();
		
		app.generateMappers();
		
		// Service
		app.generateServices();
		
		// Server.impl
		app.generateServiceImpls();
	}
	
	public void initVe() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

		ve.init();
		
		this.ve = ve;
	}
	
	public void generateCommonBean() {
		String packageName = basePackageName + ".bean";
		Template t = ve.getTemplate("CommonBean.vm");
		VelocityContext ctx = new VelocityContext();

		ctx.put("packageName", packageName);
		
		String path = Util.getRealPathFromPackageName(location, packageName, "CommonBean.java");
		Util.merge(t, ctx, path);
	}
	
	public void generateOtherBeans() {
		String packageName = basePackageName + ".bean";
		Template t = ve.getTemplate("Bean.vm");
		VelocityContext ctx = new VelocityContext();
		ctx.put("packageName", packageName);

		Schema schema = Util.readModelFromInput();
		List<Model> models = schema.getModel();
		for (Model model : models) {
			ctx.put("className", model.getName());
			List<Column> columns = model.getColumn();
			String[][] attrs = new String[columns.size()][2];
			int i = 0;
			for (Column column : columns) {
				attrs[i][0] = Util.mapType(column.getType());
				attrs[i][1] = Util.toCamelCase(column.getName());
				i++;
			}
			
			ctx.put("attrs", attrs);
			
			String path = Util.getRealPathFromPackageName(location, packageName, model.getName() + "Bean.java");
			Util.merge(t, ctx, path);
		}
	}
	
	public void generateCommonDao() {
		String packageName = basePackageName + ".dao";
		Template t = ve.getTemplate("CommonDao.vm");
		VelocityContext ctx = new VelocityContext();

		ctx.put("packageName", packageName);

		String path = Util.getRealPathFromPackageName(location, packageName, "CommonDao.java");
		Util.merge(t, ctx, path);
	}
	
	public void generateOtherDaos() {
		String packageName = basePackageName + ".dao";
		Template t = ve.getTemplate("Dao.vm");
		VelocityContext ctx = new VelocityContext();
		ctx.put("packageName", packageName);
		
		Schema schema = Util.readModelFromInput();
		List<Model> models = schema.getModel();
		for (Model model : models) {
			ctx.put("className", model.getName());
			String path = Util.getRealPathFromPackageName(location, packageName, model.getName() + "Dao.java");
			Util.merge(t, ctx, path);
		}
	}
	
	public void generateMappers() {
		String packageName = basePackageName + ".dao";
		Template t = ve.getTemplate("mapper.vm");
		VelocityContext ctx = new VelocityContext();
		ctx.put("packageName", basePackageName);

		Schema schema = Util.readModelFromInput();
		List<Model> models = schema.getModel();
		for (Model model : models) {
			ctx.put("className", model.getName());
			List<Column> columns = model.getColumn();
			String[][] attrs = new String[columns.size()][2];
			int i = 0;
			for (Column column : columns) {
				attrs[i][0] = column.getName();
				attrs[i][1] = Util.toCamelCase(column.getName());
				i++;
			}
			
			ctx.put("attrs", attrs);
			String path = Util.getRealPathFromPackageName(location, packageName, model.getName() + "Dao.xml");
			Util.merge(t, ctx, path);
		}
	}
	
	public void generateServices() {
		String packageName = basePackageName + ".service";
		Template t = ve.getTemplate("Service.vm");
		VelocityContext ctx = new VelocityContext();
		ctx.put("packageName", basePackageName);
		
		Schema schema = Util.readModelFromInput();
		List<Model> models = schema.getModel();
		for (Model model : models) {
			ctx.put("className", model.getName());
			
			String path = Util.getRealPathFromPackageName(location, packageName, model.getName() + "Service.java");
			Util.merge(t, ctx, path);
		}
	}
	
	public void generateServiceImpls() {
		String packageName = basePackageName + ".service.impl";
		Template t = ve.getTemplate("ServiceImpl.vm");
		VelocityContext ctx = new VelocityContext();
		ctx.put("packageName", basePackageName + ".service.impl");
		
		Schema schema = Util.readModelFromInput();
		List<Model> models = schema.getModel();
		for (Model model : models) {
			ctx.put("className", model.getName());
			String path = Util.getRealPathFromPackageName(location, packageName, model.getName() + "ServiceImpl.java");
			Util.merge(t, ctx, path);
		}
	}
}
