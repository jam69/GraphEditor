package es.indra.sl.geditor.commands;

import javax.swing.undo.AbstractUndoableEdit;

import es.indra.sl.geditor.GShape;
import es.indra.sl.geditor.GraphEditor;

public class ClearSelectionCommand extends AbstractUndoableEdit {

	private GraphEditor editor;
	
	public ClearSelectionCommand(GraphEditor editor){
		this.editor=editor;
	}
	
	public void redo(){
	for(GShape s: editor.getSelected()){
		editor.getShapes().remove(s);
	}
	editor.getSelected().clear();
	editor.repaint();
	}
}
