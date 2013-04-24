package es.indra.sl.geditor;

import java.util.ArrayList;
import java.util.HashMap;


public class Bus {

	private HashMap<String,ArrayList<BusListener>> listeners=new HashMap<String,ArrayList<BusListener>>();
	
	private static Bus instance;
	
	static {
		instance = new Bus();
	}
	
	private Bus(){
		
	}
	
	public static void addListener(String key,BusListener listener){
		instance._addListener(key, listener);
	}
	
	public static void removeListener(String key,BusListener listener){
		instance._removeListener(key, listener);
	}
	
	public static void notify(String key,Object value){
		instance._notify(key,value);
	}
	
	public void _addListener(String key,BusListener listener){
		ArrayList<BusListener> aux=listeners.get(key);
		if(aux==null){
			aux=new ArrayList<BusListener>();
			listeners.put(key, aux);
		}
		aux.add(listener);
	}
	
	public void _removeListener(String key,BusListener listener){
		ArrayList<BusListener> aux=listeners.get(key);
		if(aux!=null){
			aux.remove(listener);
		}
	}
	
	public void _notify(String key,Object value){
		ArrayList<BusListener> aux=listeners.get(key);
		if(aux==null)return;
		for(BusListener listener:aux){
			listener.notify(key,value);
		}
	}
}
