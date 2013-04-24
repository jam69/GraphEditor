package es.indra.sl.geditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import es.indra.sl.geditor.GAction;

public class RedoAction extends GAction {

	private UndoManager undoManager;

	public RedoAction(UndoManager undoManager) {
		super("Rehacer");
		this.undoManager = undoManager;
		updateRedoState();
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
	        undoManager.redo();
	    } catch (CannotRedoException ex) {
	        System.out.println("Unable to redo: " + ex);
	        ex.printStackTrace();
	    }
	}

	public void updateRedoState() {
		setEnabled(undoManager.canRedo());
		putValue(Action.SHORT_DESCRIPTION,undoManager.getUndoPresentationName());
	}
}
