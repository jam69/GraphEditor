package es.indra.sl.geditor;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class GAction extends AbstractAction{

	public GAction(String name){
		putValue(Action.NAME, name);
	}
	
	public GAction(String name,String iconName){
		putValue(Action.NAME, name);
		GImage gi=new GImage();
		gi.setName(iconName);
		putValue(Action.SMALL_ICON,gi.getImageIcon(64, 32));
	}
	
}
