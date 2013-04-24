package es.indra.sl.geditor;

import java.awt.Color;
import java.awt.Rectangle;

/** 
 * Una de las formas para dibujar
 * 
 * @author geoespacial02
 *
 */
public class GShape implements Cloneable{
	
	private Rectangle r;
	private String strColor;
	
	
	public GShape(){
		
	}
	
	public GShape(Rectangle r){
		this.r = r;
	}
	
	protected Object clone() throws CloneNotSupportedException {

		GShape clone=(GShape)super.clone();
		if(r!=null) clone.r=new Rectangle(r);
		clone.strColor=strColor;
		return clone;
	}
	
	public Rectangle getRect(){
		return r;
	}
	
	public void setRectangle(Rectangle r){
		this.r = r;
	}
	
	public void setStrColor(String color){
		this.strColor=color;
	}
	
	public void setColor(Color color){
		strColor=GColor.toString(color);
	}
	
	public Color getColor(){
		return GColor.toColor(strColor);
	}
	
	public String getStrColor(){
		return strColor;
	}
}
