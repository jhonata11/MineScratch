package br.ufsc.ine.minetest;

public class Parameter {
	private Object type;
	private Object parameter;
	
	public Parameter(Object klass, Object parameter) {
		this.type = klass;
		this.parameter = parameter;
	}
	
	public Object getParameter() {
		return parameter;
	}

	public Object getType() {
		return type;
	}
}
