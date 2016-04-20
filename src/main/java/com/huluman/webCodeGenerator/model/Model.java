package com.huluman.webCodeGenerator.model;

import java.util.List;

public class Model {

	private String name;
	
	private List<Column> column;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumn() {
		return column;
	}

	public void setColumn(List<Column> column) {
		this.column = column;
	}


}
