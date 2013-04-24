package es.indra.sl.geditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import es.indra.sl.geditor.actions.RedoAction;
import es.indra.sl.geditor.actions.RemoveSelectionAction;
import es.indra.sl.geditor.actions.UndoAction;

public class GraphEditor extends JPanel implements MouseMotionListener,MouseListener,Escalable {

	private Point initPosition;
	private Point oldPosition;
	private GShape moving;
	private GShape defaultGShape; // Tipo de forma que vamos a crear
	private ArrayList<GShape> shapes;
	private ArrayList<GShape> selected;
	private HashMap<String,Action> actions;
	private int snapGrid; // Distancia de ajuste de la rejilla
	private boolean drawGrid; // Si dibujamos la rejilla
	private Color backGroundColor=Color.white;
	private final Color gridColor=Color.darkGray;  // Color de la rejilla
	private final int gridSize=0;  // Tama–o de las cruzes de la rejilla
	private UndoManager undoManager;
	private UndoableEditSupport undoSupport;
	private UndoAction undoAction;
	private RedoAction redoAction;
	
	static public final String UNDO_ACTION="UNDO";
	static public final String REDO_ACTION="REDO";
	
	static public final String CLEAR_SELECTION="ClearSelection";
	static public final String REMOVE_SELECTION="RemoveSelection";
	static public final String LEFT_ALIGN="LeftAlign";
	static public final String RIGHT_ALIGN="RightAlign";
	static public final String LEFT_RIGHT_ALIGN="LeftRightAlign";
	static public final String EQUAL_SPACE_VERT="EqualSpaceVert";
	static public final int Action_SpaceHorizontal=1;
	static public final int Action_AlignTop=1;
	static public final int Action_AlignCentre=1;
	static public final int Action_AlignBottom=1;
	static public final int Action_AlignTopAndBottom=1;
	static public final int Action_SpaceVertical=1;
	static public final int Action_ToggleShowGrid=1;
	static public final int Action_ToggleUseGrid=1;
	static public final int Action_ToggleShowGuides=1;
	static public final int Action_SelectUp=1;
	static public final int Action_SelectDown=1;
	static public final int Action_toFront=1;
	static public final int Action_toBack=1;
	static public final int Action_toTop=1;
	static public final int Action_toBottom=1;
	
	
	
	public ArrayList<GShape> getShapes() {
		return shapes;
	}

	public ArrayList<GShape> getSelected() {
		return selected;
	}


	public Action getAction(String str){
		return actions.get(str);
	}
	
	public Action createAction(final EShape shape){
		String s="new_"+shape.getType();
		Action a=new GAction(s,shape.getStrImage()){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				defaultGShape=shape;
			}
		};
		return a;
	}
	

	
	private void createActions(){
		
		
		// preparamos las estructuras
		actions=new HashMap<String,Action>();
		Action a;

		undoAction=new UndoAction(undoManager);
		redoAction=new RedoAction(undoManager);
		actions.put(UNDO_ACTION,undoAction);
		actions.put(REDO_ACTION,redoAction);
		
		undoSupport.addUndoableEditListener(new UndoableEditListener() {
			
			@Override
			public void undoableEditHappened(UndoableEditEvent ev) {
				System.out.println("EditHappened:"+ev);
				undoAction.updateUndoState();
				redoAction.updateRedoState();			
			}
		});
		
		// Limpia la seleccion
		a=new GAction(CLEAR_SELECTION){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selected.clear();
				repaint();
			}

		};
		actions.put((String)a.getValue(Action.NAME),a);

		// Borra los elementos seleccionados
		a=new RemoveSelectionAction(REMOVE_SELECTION, GraphEditor.this);
		
		actions.put((String)a.getValue(Action.NAME),a);

		// Alinea a la izquierda
		a=new GAction(LEFT_ALIGN){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int v=Integer.MAX_VALUE;
				for(GShape s: selected){
					int aux=s.getRect().x;
					if(v>aux)v=aux; 
				}
				for(GShape s: selected){
					s.getRect().x=v;
				}

				repaint();
			}

		};
		actions.put((String)a.getValue(Action.NAME),a);
		
		// Alinea a la derecha
				a=new GAction(RIGHT_ALIGN){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						int v=Integer.MIN_VALUE;
						for(GShape s: selected){
							int aux=s.getRect().x+s.getRect().width;
							if(v<aux)v=aux; 
						}
						for(GShape s: selected){
							s.getRect().x=v-s.getRect().width;
						}

						repaint();
					}

				};
				actions.put((String)a.getValue(Action.NAME),a);
				
				// Alinea a la izquierda y a  la derecha
				a=new GAction(LEFT_RIGHT_ALIGN){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						int v=Integer.MAX_VALUE;
						int v2=Integer.MIN_VALUE;
						for(GShape s: selected){
							int aux=s.getRect().x;
							if(v>aux)v=aux; 
							int aux2=s.getRect().width;
							if(v2<aux2)v2=aux2; 
						}
						for(GShape s: selected){
							s.getRect().x=v;
							s.getRect().width=v2;
						}

						repaint();
					}

				};
				actions.put((String)a.getValue(Action.NAME),a);
				
				// Igual espacio verticalmente
				a=new GAction(EQUAL_SPACE_VERT){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						int H=snapGrid*1;
						int y=-1;
						for(GShape s: selected){
							if(y>=0){
								s.getRect().y=y;
							}
							y=s.getRect().y+s.getRect().height+H;
						}
						repaint();
					}

				};
				actions.put((String)a.getValue(Action.NAME),a);


	}
	
	GraphEditor(){
		shapes=new ArrayList<GShape>();
		selected=new ArrayList<GShape>();
		snapGrid=30;
		drawGrid=true;
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(backGroundColor);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		undoSupport=new UndoableEditSupport(this);
		undoManager=new UndoManager();
		undoSupport.addUndoableEditListener(undoManager);
		createActions();
	
	}
	
	public UndoManager getUndoManager(){
		return undoManager;
	}
	
	public void execute(UndoableEdit command){
		if(command.canRedo()){
			command.redo();
			undoSupport.postEdit(command);
		}
	}

	@Override
	public void mouseMoved(MouseEvent ev) {
		GShape s=find(ev.getPoint());
		if(s!=null){
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}else{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		System.out.println("MouseClicked:"+ev);
		GShape s=find(ev.getPoint());
		if(s!=null){
			
			if( selected.contains(s)){
				selected.remove(s);
			}else{
				if(! ev.isShiftDown()){
					selected.clear();
				}
				selected.add(s);
				Bus.notify("Selected", s);
			}
			
		}else {
			// New Entity
			GShape s1;
			try {
				Point p=snapPoint(ev.getPoint());
				s1 = (GShape)defaultGShape.clone();
				Rectangle r=s1.getRect();
				r.x=p.x;
				r.y=p.y;
				shapes.add(s1);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		System.out.println("MousePressed:"+ev);
		moving=find(ev.getPoint());
		initPosition=snapPoint(ev.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent ev) {
		System.out.println("MouseDragged:"+ev);
		Graphics g=getGraphics();
		Point p=snapPoint(ev.getPoint());
		if(moving!=null){
			if(oldPosition!=null){
				for(GShape s :selected){
					Rectangle r=s.getRect();
					r.x += p.x-oldPosition.x;
					r.y += p.y-oldPosition.y;
				}
			repaint();
			}
		}else{ // new-mode
			g.setXORMode(Color.blue);
			if(oldPosition!=null){
				g.drawRect(initPosition.x,initPosition.y, oldPosition.x-initPosition.x,oldPosition.y-initPosition.y);
			}

			if(initPosition.x>p.x){
				initPosition.x=p.x;
			}
			if(initPosition.y>p.y){
				initPosition.y=p.y;
			}

			g.drawRect(initPosition.x,initPosition.y, p.x-initPosition.x,p.y-initPosition.y);
		}
		oldPosition=p;
	}
	
	@Override
	public void mouseReleased(MouseEvent ev) {
		// TODO Auto-generated method stub
		if(moving!=null){
			moving=null;
		}else{
			Rectangle r=new Rectangle(initPosition);
			Point p=snapPoint(ev.getPoint());
			r.width=p.x-initPosition.x;
			r.height=p.y-initPosition.y;
			GShape s;
			try {
				s = (GShape)defaultGShape.clone();
				s.setRectangle(r);
				shapes.add(s);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		oldPosition=initPosition=null;
		repaint();
	}
	
	private Point snapPoint(Point p){
		if(snapGrid<=0) return p;  // No-snap
		float snapGrid2=snapGrid/2;
		int t=p.x%snapGrid;
		if(t>snapGrid2){
			p.x += (snapGrid-t);
			}
		else{
			p.x -= t;
		}
		t=p.y%snapGrid;
		if(t>snapGrid2){
			p.y += (snapGrid-t);
		}else{
			p.y -= t;
		}
		
		return p;
	}
	

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D gr=(Graphics2D) g;
		if(drawGrid){
			drawTheGrid(gr);
		}
		for(GShape s : shapes){
			draw(s,gr);
		}
		for(GShape s : selected){
			highlight(s,gr);
		}
	}
	
	protected void draw(GShape s,Graphics2D gr){
		System.out.println(">> draw:"+s);
		gr.setColor(s.getColor());
		Rectangle r=s.getRect();
		gr.drawRect(r.x,r.y, r.width,r.height);
		gr.fillRect(r.x,r.y, r.width,r.height);
		
		EShape e=(EShape)s;
		GImage gi=new GImage();
		gi.setName(e.getStrImage());
		gr.drawImage(gi.getImage(), r.x,r.y, r.width,r.height,null);
	}
	
	protected void draw(GShape s,Color c,Graphics2D gr){
		gr.setColor(c);
		Rectangle r=s.getRect();
		gr.drawRect(r.x,r.y, r.width,r.height);
	}
	
	protected void highlight(GShape s,Graphics2D gr){
		gr.setColor(Color.blue);
		Rectangle r=(Rectangle)s.getRect().clone();
			r.grow(2, 2);
		// amplia
//		r.x -= 2;
//		r.y -= 2;
//		r.width += 4;
//		r.height +=4;
		
		gr.drawRect(r.x,r.y, r.width,r.height);
	}
	
	protected void drawTheGrid(Graphics2D g){
		g.setColor(gridColor);
		int xg=gridSize;
		int x=snapGrid;
		while(x<getWidth()){
			int y=snapGrid;
			while(y<getHeight()){
				g.drawLine(x-xg,y,x+xg,y);
				g.drawLine(x,y-xg,x,y+xg);
				y+=snapGrid;
			}
			x+=snapGrid;
		}
	}
	
	protected GShape find(Point p){
		for (GShape s:shapes){
			Rectangle r=s.getRect();
			if(r.contains(p)) return s;
		}
		return null;
	}
	protected ArrayList<GShape> findAll(Point p){
		ArrayList<GShape> ret=new ArrayList<GShape>();
		for (GShape s:shapes){
			Rectangle r=s.getRect();
			if(r.contains(p)) {
				ret.add(s);
			}
		}
		return ret;
	}
	
	public void save(String path){
		try {
			GsonBuilder builder=new GsonBuilder();
			Gson gSon=builder.setPrettyPrinting().create();
			FileWriter fw=new FileWriter(new File(path));
			String s=gSon.toJson(shapes);
			fw.write(s);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(String path){
		try {
			FileReader fr=new FileReader(new File(path));
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
			repaint();
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
}
