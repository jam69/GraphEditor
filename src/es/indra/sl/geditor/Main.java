package es.indra.sl.geditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;


public class Main extends JFrame {

	private ZoomPane scroll;
	private GraphEditor ge;
	private JFileChooser fileChooser;
	private Action actionSave;
	private Action actionLoad;
	private AttribTableModel propsModel;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main m=new Main();
		m.setVisible(true);
	}
	
	Main(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ge=new GraphEditor();
		
		Palette paleta=new Palette();
		JToolBar toolBar=new JToolBar(JToolBar.VERTICAL);
		toolBar.setFloatable(true);
		for(EShape e : paleta.getShapes()){
			Action a=ge.createAction(e);
			toolBar.add(a);
		}
		toolBar.setVisible(true);
		JPanel p=new JPanel(new BorderLayout());
		setContentPane(p);
		p.add(toolBar,BorderLayout.WEST);
		
		JPanel p2=new JPanel();
		p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));
		propsModel =new AttribTableModel();
		JTable tProps=new JTable(propsModel);
		Bus.addListener("Selected",propsModel);
		
		JLabel propsTitle=new JLabel("Propiedades");
		p2.add(propsTitle);
		JScrollPane tableScroll=new JScrollPane(tProps);
		p2.add(tableScroll);
		tableScroll.setPreferredSize(new Dimension(200,400));
		p.add(p2,BorderLayout.EAST);
		
		ge.setPreferredSize(new Dimension(1000,1000));
		scroll=new ZoomPane(ge);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setSize(new Dimension(800,800));
		p.add(scroll, BorderLayout.CENTER);
		
		actionSave=new AbstractAction("SaveLayout") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s=getFileToSave();
				ge.save(s);	
			}
		};
		
		actionLoad=new AbstractAction("LoadLayout") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s=getFileToLoad();
				ge.load(s);	
			}
		};
		
		setJMenuBar(createMenuBar());
		
	}

	JMenuBar createMenuBar(){
		JMenuBar ret=new JMenuBar();
		
		JMenu menuFile=new JMenu("File");
		menuFile.add(actionLoad);
		menuFile.add(actionSave);
		menuFile.addSeparator();
		menuFile.add("exit");
		ret.add(menuFile);
		
		JMenu menuView=new JMenu("View");
		menuView.add(scroll.getZoomInAction());
		menuView.add(scroll.getZoomOutAction());
		ret.add(menuView);
		
		JMenu menuEdit=new JMenu("Edit");
		menuEdit.add(ge.getAction(GraphEditor.UNDO_ACTION));
		menuEdit.add(ge.getAction(GraphEditor.REDO_ACTION));
		menuEdit.addSeparator();
		menuEdit.add(ge.getAction(GraphEditor.CLEAR_SELECTION));
		menuEdit.add(ge.getAction(GraphEditor.REMOVE_SELECTION));
		menuEdit.addSeparator();
		menuEdit.add(ge.getAction(GraphEditor.LEFT_ALIGN));
		menuEdit.add(ge.getAction(GraphEditor.RIGHT_ALIGN));
		menuEdit.add(ge.getAction(GraphEditor.LEFT_RIGHT_ALIGN));
		menuEdit.add(ge.getAction(GraphEditor.EQUAL_SPACE_VERT));
		ret.add(menuEdit);
		
		
		
		
		JMenu menuHelp=new JMenu("Help");
		menuHelp.add("Ayuda");
		menuHelp.addSeparator();
		menuHelp.add("About");
		
		ret.add(Box.createHorizontalGlue());
		ret.add(menuHelp);
		return ret;
	}
	
	
	
	private JFileChooser createFileChooser(){
		fileChooser=new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Layout files", "json","layout");
		fileChooser.setFileFilter(filter);
		return fileChooser;
	}
	
	public String getFileToLoad(){
		if(fileChooser==null){
			fileChooser=createFileChooser();
		}
		int returnVal = fileChooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	    		   fileChooser.getSelectedFile().getName());
	    }
	    return fileChooser.getSelectedFile().getName();
	}
	
	public String getFileToSave(){
		if(fileChooser==null){
			fileChooser=createFileChooser();
		}
		int returnVal = fileChooser.showSaveDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to save this file: " +
	    		   fileChooser.getSelectedFile().getName());
	    }
	    return fileChooser.getSelectedFile().getName();
	}
	
	
		
		
		
		
	
}
