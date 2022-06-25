package model;

import java.util.List;

public class Pedido {

	private int Id;
	private List<String> ubicaciones;
	private Vehiculo vehiculo;
	
	public Pedido() {
		
	}
	
	public Pedido(int id, List<String> ubicaciones, Vehiculo vehiculo) {
		this.Id = id;
		this.ubicaciones = ubicaciones;
		this.vehiculo = vehiculo;
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
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	
	
}
