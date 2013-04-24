package es.indra.sl.geditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import es.indra.sl.geditor.GAction;

public class UndoAction extends GAction {

	private UndoManager undoManager;

	public UndoAction(UndoManager undoManager) {
		super("Deshacer");
		this.undoManager = undoManager;
		updateUndoState();
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			undoManager.undo();
		} catch (CannotUndoException ex) {
			System.out.println("Unable to undo: " + ex);
			ex.printStackTrace();
		}
	}

	public void updateUndoState() {
			setEnabled(undoManager.canUndo());
			putValue(Action.SHORT_DESCRIPTION,undoManager.getRedoPresentationName());
	}
}
