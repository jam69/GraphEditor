package es.indra.sl.geditor;

import java.util.HashMap;

public class EShape extends GShape implements Cloneable{

	private String type;
	private String id;
	private String text;
	private String strImage;
	private boolean resizable;
	private HashMap<String,Object> properties;
	

	public EShape(){
		properties=new HashMap<String,Object>();
		resizable=false;
	}

	protected Object clone() throws CloneNotSupportedException {

		EShape clone=(EShape)super.clone();
		clone.type=type;
		clone.id=id;
		clone.text=text;
		clone.strImage=strImage;
		clone.resizable=resizable;
		clone.properties=(HashMap<String, Object>) properties.clone();
		return clone;
		
	}
	
	public String getStrImage() {
		return strImage;
	}

	public void setStrImage(String strImage) {
		this.strImage = strImage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setResizable(boolean v){
		resizable=v;
	}
	
	public boolean getResizable(){
		return resizable;
	}
	
	public void addProperty(String name, Object value){
		properties.put(name,value);
	}
	
	public Object getProperty(String name){
		return properties.get(name);
	}
	
	public HashMap<String,Object> getProperties(){
		return properties;
	}
	
}
