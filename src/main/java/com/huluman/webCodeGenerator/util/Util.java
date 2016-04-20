package com.huluman.webCodeGenerator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.alibaba.fastjson.JSON;
import com.huluman.webCodeGenerator.model.Schema;

public class Util {
	public static Schema readModelFromInput() {
		try {
			String filename = Util.class.getResource("/").getPath() + "InputBeans.json";
			String json = FileUtils.readFileToString(new File(filename));
			Schema schema = JSON.parseObject(json, Schema.class);
			return schema;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String mapType(String input) {
		switch(input.substring(0, 3).toLowerCase()) {
		case "int":
		case "tin": // tinyint
		case "sma": // smallint
			return "Integer";
		case "var": // varchar
		case "med": // mediumtext
			return "String";
		case "tim": // timestamp
			return "Timestamp";
		default:
			return null;
		}
	}
	
	public static String toCamelCase(String input) {
		String str = WordUtils.capitalizeFully(input, new char[]{'_'}).replaceAll("_", "");
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	public static void sendToOutput(StringWriter input) {
		// TODO send to file
		System.out.println(input.toString());
	}
	
	public static String getRealPathFromPackageName(String location, String packageName, String filename) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(location);
		if (!location.endsWith(File.separator)) {
			buffer.append(File.separator);
		}
		
		buffer.append(packageName.replace(".", File.separator));
		
		buffer.append(File.separator);
		
		buffer.append(filename);
		
		return buffer.toString();
	}
	
	public static void merge(Template template, VelocityContext ctx, String path) {
		/*
		StringWriter sw = new StringWriter();
		template.merge(ctx, sw);
		System.out.println(sw.toString());
		*/
		File folder = new File(path).getParentFile();
		
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path);
			template.merge(ctx, writer);
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
}
