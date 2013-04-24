package es.indra.sl.bus;

public interface ApplicationResource extends ApplicationResponder  {

	
	public String getSource();  // URI to request the resource
	public void setSource();
	
	public String processEvent();  // Event to perform in order to process this resource with an unknown processor

	public void getProcessEvent();
	
}
