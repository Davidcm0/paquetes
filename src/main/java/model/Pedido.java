package model;

import java.util.List;

public class Pedido {

	private int Id;
	private List<String> ubicaciones;
	private Object ubicaciones2;
	private String vehiculo;
	
	public Pedido() {
		
	}
	
	public Pedido(int id, Object ubicaciones, String vehiculo) {
		this.Id = id;
		this.ubicaciones2 = ubicaciones;
		this.vehiculo = vehiculo;
	}
	
	public Pedido(int id, List<String> ubicaciones, String vehiculo) {
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
	public List<String> getUbicaciones() {
		return ubicaciones;
	}
	public void setUbicaciones(List<String> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}
	public String getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	
	
}
