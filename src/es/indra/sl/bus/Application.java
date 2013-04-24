package es.indra.sl.bus;

import java.util.ArrayList;
import java.util.HashMap;

public class Application {

	/** Constantes ficheros JSON */
	public static final String kEventAppLaunch = "EventAppLaunch";
	public static final String kEventAppWillResignActive = "EventAppResignActive";
	public static final String kEventAppEnterBackgroud = "EventAppEnterBackground";
	public static final String kEventAppEnterForeground = "EventAppEnterForeground";
	public static final String kEventAppBecomeActive = "EventAppBecomeActive";
	public static final String kEventAppWillTerminate = "EventAppWillTerminate";

	/*
	 * 
	 * Sample JSON configuration (using Keys as reference)
	 */ 
//	 { 
//		 kIAppClass = "IApplication", 
//	     kIAppURISchema = "myapp://",
//	     kIAppLaunchEvents = ["EventAppLaunch", "EventSendPendingData"],
//	     kIAppListeners = { 
//				 "EventAppLaunch" = [ "IClass1", "IClass2" ],
//	             "EventAppFinish" = [ "IClass3", "IClass1", "IClass4" ] 
//	             } 
//		 kIAppHandlers = { 
//				 "EventAppLaunch" = [ "IClassHandler1" ] 
//				 } 
//		 kIAppResources = {
//				 "Resource1" = "http://adomain.com/resource", 
//				 "Resource2" = "file://afile.ext", 
//				 "Resource3" = "myapp://getResource/1234" 
//				 } 
//		 kIAppURIs = { 
//				 "DoSomething" = "EventDoSomething" 
//				} 
//		 kIAppNavigationScheme = [ kAppNavigationGroup, kAppNavigationStack ] 
//	 }
//	  
//	  
//	  Names of classes should be cross-referenced to the code using another
//	  JSON configuration such as:
//	  
//	  { "IClass1" = "IInternalObjCClass1", ... }
//
	
	/** Instancia Singleton */
	private Application sharedInstance;

	/** estructuras datos */
	private HashMap<String, ArrayList<ApplicationHandler>> handlers;
	private HashMap<String, ArrayList<ApplicationListener>> listeners;
	private HashMap<String, ArrayList<ApplicationResource>> resources;

	private Application() {

	}

	public Application sharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new Application();
		}
		return sharedInstance;
	}

	void dispatchEvent(String event, Object userInfo, Object sender) {
		boolean eventProcessed = false;

		for (ApplicationHandler handler : handlers.get(event)) {

			if (!handler.continueAfterEvent(event, userInfo, sender))
				break;

		}
		// Segun la implementación de Objective-C se hace esto, y si algun
		// handler ha devuelto false ya no
		// se comprueba ningún listener ¿¿??
		if (! eventProcessed) {
			for (ApplicationListener listener : listeners.get(event)) {
				if (!listener.continueAfterEvent(event, userInfo, sender))
					break;
			}
		}

		if (! eventProcessed) {
			// Segun ObjectiveC,.... Application lo procesa:  "processEVENT(info,sender)"
			
			// En pseudo-código
			// String methodName="process"+event;
			// Object[] args=new [info,sender];
			// Reflection.invoke(this,methodName,args)
			
			// Rápido walk-around sin introspección
			processEvent(event,userInfo,sender);
		}

	}
	
	protected void processEvent(String event,Object userInfo, Object sender){
		// do nothing :   must be override by the implementations 
		// También se puede definir como abstract
	}

	// ---------- Listeners
	public void addListener(ApplicationListener newListener, String event) {
		ArrayList<ApplicationListener> aux = listeners.get(event);
		if (aux == null) {
			aux = new ArrayList<ApplicationListener>();
			listeners.put(event, aux);
		}
		aux.add(newListener);
	}

	public void removeListener(ApplicationListener oldListener) {
		for (ArrayList<ApplicationListener> aux : listeners.values()) {
			aux.remove(oldListener);
		}
	}

	public ArrayList<ApplicationListener> listenersForEvent(String event) {
		return listeners.get(event);
		// return Collections.unmodifiableList(listeners.get(event));
	}

	// --------- Handlers
	public void addHandler(ApplicationHandler newHandler, String event) {
		ArrayList<ApplicationHandler> aux = handlers.get(event);
		if (aux == null) {
			aux = new ArrayList<ApplicationHandler>();
			handlers.put(event, aux);
		}
		aux.add(newHandler);
	}

	public void removeHandler(ApplicationHandler oldHandler) {
		for (ArrayList<ApplicationHandler> aux : handlers.values()) {
			aux.remove(oldHandler);
		}
	}

	public ArrayList<ApplicationHandler> handlersForEvent(String event) {
		return handlers.get(event);
		// return Collections.unmodifiableList(handlers.get(event));
	}

	// --------- Resources
	public void addResource(ApplicationResource newResource, String event) {
		ArrayList<ApplicationResource> aux = resources.get(event);
		if (aux == null) {
			aux = new ArrayList<ApplicationResource>();
			resources.put(event, aux);
		}
		aux.add(newResource);
	}

	public void removeResource(ApplicationResource oldResource) {
		for (ArrayList<ApplicationResource> aux : resources.values()) {
			aux.remove(oldResource);
		}
	}

	public ArrayList<ApplicationResource> resourcesForEvent(String event) {
		return resources.get(event);
		// return Collections.unmodifiableList(resources.get(event));
	}

	public ApplicationResource aResourceForKey(String event) {
		ArrayList<ApplicationResource> aux = resources.get(event);
		if (aux != null && aux.size() > 0) {
			return aux.get(0);
		}
		return null;
	}

}
