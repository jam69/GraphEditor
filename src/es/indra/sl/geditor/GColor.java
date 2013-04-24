package es.indra.sl.geditor;

import java.awt.Color;

public class GColor {

	static String toString(Color color){
		return color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha();
	}
	
	static Color toColor(String str){
		String[] s=str.split(",");
		return new Color(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]));
		
	}
}
