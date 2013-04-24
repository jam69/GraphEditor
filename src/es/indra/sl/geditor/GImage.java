package es.indra.sl.geditor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GImage {

	private static final String imgPath="./resources/IOS.GUI/Icons/";
	
	public String name;
	
	public GImage(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage(){
		try {
			Image im=ImageIO.read(new File(imgPath+name));
			return im;
		} catch (IOException e) {
			System.err.println("No encontrada imagen:"+name);
			return null;
		}
	}
	
	public ImageIcon getImageIcon(int w,int h){
		try {
			Image im=ImageIO.read(new File(imgPath+name));
			BufferedImage icon=new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g=icon.getGraphics();
			g.drawImage(im,0,0,w,h,null);
			return new ImageIcon(icon);
		} catch (IOException e) {
			System.err.println("No encontrada imagen:"+name);
			return null;
		}
	}
	
}
