package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Pedido {

	private int Id;
	private ArrayList<String> ubicaciones;
	private Object ubicaciones2;
	private String vehiculo;
	
	public Pedido() {
		
	}
	
	public Pedido(int id, Object ubicaciones, String vehiculo) {
		this.Id = id;
		this.ubicaciones2 = ubicaciones;
		this.vehiculo = vehiculo;
	}
	
	public Pedido(int id, ArrayList<String> ubicaciones, String vehiculo) {
		this.Id = id;
		this.ubicaciones = ubicaciones;
		this.vehiculo = vehiculo;
	}
	
	public Pedido(int id) {
		this.Id = id;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public ArrayList<String> getUbicaciones() {
		return ubicaciones;
	}
	public void setUbicaciones(ArrayList<String> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}
	public String getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	public JSONObject toJSON() {
		JSONObject jso = new JSONObject();
		jso.put("Id", this.getId());
		jso.put("Ubicaciones", this.getUbicaciones());
		jso.put("Vehiculo", this.getVehiculo());
		return jso;
	}
	
	
	
}
