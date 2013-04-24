package es.indra.sl.geditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class AttribTableModel extends AbstractTableModel implements BusListener{

	private HashMap<String,Object> properties;
	private ArrayList<String> keys;
	
	public AttribTableModel(){
		super();
		properties=new HashMap<String,Object>();
		keys=new ArrayList<String>();
	}

	public AttribTableModel(HashMap<String,Object> properties){
		super();
		setProperties(properties);
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}


	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
		keys.clear();
		if(properties!=null){
		keys.addAll(properties.keySet());
		Collections.sort(keys);
		}
		fireTableDataChanged();
		
	}
	
	public int getRowCount(){
		if(properties==null)return 0;
		return properties.size();
	}
	
	public int getColumnCount(){
		return 2;
	}
	
	public String getColumnName(int col) {
		switch(col){
		case 0: return "Nombre";
		case 1: return "Valor";
		default: return null; // nunca pasa por aqui 
		}
    }
	
	public Object getValueAt(int row, int col) {
		switch(col){
		case 0: return keys.get(row);
		case 1: return properties.get(keys.get(row));
		default: return null; // nunca pasa por aqui 
		}
    }
    
	public boolean isCellEditable(int row, int col){
    	return col==1;
    }
    
    public void setValueAt(Object value, int row, int col) {
    	assert(col==1);
        properties.put(keys.get(row),value);
        fireTableCellUpdated(row, col);
    }

	@Override
	public void notify(String key,Object value) {
		if(value==null){
			setProperties(null);
			return;
		}
		if(value instanceof EShape){
			EShape sh=(EShape)value;
			setProperties(sh.getProperties());
		}
		
	}
	
	
}
