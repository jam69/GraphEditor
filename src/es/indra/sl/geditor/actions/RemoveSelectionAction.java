package es.indra.sl.geditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.undo.UndoableEdit;

import es.indra.sl.geditor.GAction;
import es.indra.sl.geditor.GraphEditor;
import es.indra.sl.geditor.commands.RemoveSelectionCommand;

public class RemoveSelectionAction extends GAction  {

	private GraphEditor editor;
	
	public RemoveSelectionAction(String s,GraphEditor editor){
		super(s);
		this.editor=editor;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		UndoableEdit command=new RemoveSelectionCommand(editor);
		editor.execute(command);
		
	}

}
