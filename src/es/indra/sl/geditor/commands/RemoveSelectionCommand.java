package es.indra.sl.geditor.commands;

import java.util.ArrayList;

import javax.swing.undo.AbstractUndoableEdit;

import es.indra.sl.geditor.GShape;
import es.indra.sl.geditor.GraphEditor;

public class RemoveSelectionCommand extends AbstractUndoableEdit {

	private GraphEditor editor;
	private ArrayList<GShape> oldShapes;

	public RemoveSelectionCommand(GraphEditor editor) {
		this.editor = editor;
	}

	public void redo() {
		oldShapes=new ArrayList<GShape>();
		for (GShape s : editor.getSelected()) {
			editor.getShapes().remove(s);
			oldShapes.add(s);
		}
		editor.getSelected().clear();
		editor.repaint();
	}

	public void undo(){
		editor.getShapes().addAll(oldShapes);
		editor.repaint();
	}
	
	public boolean canRedo(){
		return editor.getSelected().size() != 0 ;
	}
	
	public boolean canUndo(){
		return oldShapes != null && oldShapes.size() != 0 ;
	}
}
