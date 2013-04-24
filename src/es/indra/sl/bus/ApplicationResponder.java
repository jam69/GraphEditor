package es.indra.sl.bus;

public interface ApplicationResponder {

	public boolean continueAfterEvent(String event,Object info,Object sender);

}

