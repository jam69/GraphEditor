package es.indra.sl.geditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ZoomPane extends JScrollPane {

	private float scale=1.0f;
	private JComponent escalable;
	private Action zoomInAction;
	private Action zoomOutAction;
	private Action zoomOrigAction;

	
	public ZoomPane(JComponent c){
		super(c);
		escalable=c;
		
		zoomInAction=new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				zoomIn();
			}
		};
		zoomInAction.putValue(Action.NAME,"ZoomIn");
		
		zoomOutAction=new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				zoomOut();
			}
		};
		zoomOutAction.putValue(Action.NAME,"ZoomOut");
		
	}
	
	
	
	public Action getZoomInAction(){
		return zoomInAction;
	}
	public Action getZoomOutAction(){
		return zoomOutAction;
	}
	
	public void zoomIn(){
		scale = scale * 2.f ;
		Graphics2D g=(Graphics2D)escalable.getGraphics();
		g.getTransform().scale(.5f,.5f);
		Dimension d=escalable.getPreferredSize();
		d.height=d.height*2;
		d.width=d.width*2;
		escalable.setPreferredSize(d);
		getParent().invalidate();
		System.out.println("SIZE="+escalable.getSize());
		System.out.println("PSIZE="+escalable.getPreferredSize());
 	}
	
	public void zoomOut(){
		scale = scale / 2.f ;
	}
}
