package es.indra.sl.geditor;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Palette {

	private ArrayList<EShape> shapes;
	
	public Palette(){
		shapes=new ArrayList<EShape>();
		createShapes();
		savePalette("IOS_palete");
		loadPalette("IOS_palete");
	}
	
	public ArrayList<EShape> getShapes(){
		return shapes;
	}
	
	public void loadPalette(String name){
		try {
			FileReader fr=new FileReader(new File(name+".json"));
			GsonBuilder builder=new GsonBuilder();
			Gson gson=builder.create();
			JsonParser parser = new JsonParser();
		    JsonArray array = parser.parse(fr).getAsJsonArray();
		    shapes.clear();
		    for( JsonElement e :array){
		    	System.out.println(">> Leido element:"+e);
		    	EShape s=gson.fromJson(e, EShape.class);
		    	System.out.println(">> Leido shape:"+s);
		    	shapes.add(s);
		    }
			System.out.println(">> Leido");
			fr.close();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public void savePalette(String name){
		try {
			GsonBuilder builder=new GsonBuilder();
			Gson gSon=builder.setPrettyPrinting().create();
			FileWriter fw=new FileWriter(new File(name+".json"));
			String s=gSon.toJson(shapes);
			fw.write(s);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createShapes(){
		EShape e=new EShape();
		e.setColor(Color.black);
		e.setId("proto");
		e.setType("TextField");
		e.setText("Texto1");
		e.setStrImage("button.png");
		e.setRectangle(new Rectangle(0,0,60,30));
		e.addProperty("bg",GColor.toString(Color.blue));
		e.addProperty("test","un - String");
		shapes.add(e);
		
		e=new EShape();
		e.setColor(Color.magenta);
		e.setId("proto");
		e.setType("Label");
		e.setText("Texto1");
		e.setStrImage("buttonLabel.png");
		e.setRectangle(new Rectangle(0,0,154,49));
		e.addProperty("bg",GColor.toString(Color.red));
		e.addProperty("test","un - String");
		shapes.add(e);
	}
}
