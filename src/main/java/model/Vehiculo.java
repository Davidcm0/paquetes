package model;

import org.json.JSONObject;

public class Vehiculo {
	
	private String matricula;
	private String marca;
	private String modelo;
	private String color;
	private String password;
	
	public Vehiculo() {
		
	}
	
	public Vehiculo(String matricula, String marca, String modelo, String color, String password ) {
		this.matricula = matricula;
		this.color = color;
		this.marca = marca;
		this.modelo = modelo;
		this.password = password;
	}
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public JSONObject toJSON() {
		JSONObject jso = new JSONObject();
		jso.put("Matricula", this.getMatricula());
		jso.put("Marca", this.getMarca());
		jso.put("Modelo", this.getModelo());
		jso.put("Color", this.getColor());
		return jso;
	}

}
