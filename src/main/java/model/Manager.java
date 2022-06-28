package model;

import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


//import excepciones.CredencialesInvalidasException;
import persistencia.PaqueteDAO;
import persistencia.VehiculoDAO;


import java.io.*;
import javax.swing.*;

public class Manager {
    Vehiculo vehiculo = new Vehiculo();
    Pedido pedido = new Pedido();
	private WebSocketSession session;
	public static final String USUARIOS = "usuarios";

	public Manager() {
		// Metodo constructor vacio (no hay atributos)
	}

	private static class ManagerHolder {
		private static Manager singleton = new Manager();
	}

	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public void login(String matricula, String password) throws Exception {

		boolean login = false;

		ArrayList<Vehiculo> vehiculos = (ArrayList<Vehiculo>) VehiculoDAO.leer_vehiculos();
		for (Vehiculo v : vehiculos) {
			login = checkCredenciales(v, matricula, password);
			if (login)
				break;
		}
		if (!login) {
			//throw new CredencialesInvalidasException();
		}

	}

	public boolean checkCredenciales(Vehiculo v, String matricula, String password) throws Exception {
		boolean aux = false;
		String pwdEncrypted, pwdUser;
		if (v.getMatricula().equals(matricula)) {
				pwdEncrypted = v.getPassword();
				pwdUser = encriptarMD5(password);
				if (!(pwdEncrypted.equals(pwdUser))) {

					//throw new CredencialesInvalidasException();

				} else {
					System.out.println("Sucessful login");
					aux = true;
				}
		}
		

		return aux;

	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	private static String encriptarMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public void register(String matricula, String password, String marca, String modelo, String color) {
		Boolean validado = false;
		VehiculoDAO.insertar(new Vehiculo(matricula, marca, modelo, color, encriptarMD5(password)));

	}

	public Object leer() {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONObject leer_pedidos(String matricula) {
		JSONArray jsa = new JSONArray();
		JSONObject jso = new JSONObject();
		List<Pedido> pedidos = PaqueteDAO.leer_pedidos();
		Vehiculo v = VehiculoDAO.leer_vehiculo(matricula);
        jsa.put(v.toJSON());
		for (Pedido pedido : pedidos) {

			jsa.put(pedido.toJSON());
		}
		
		jso.put("pedidos", jsa);

		return jso;
	}

	public void AsignarPedidos(String matricula, Object pedidos) {
		ArrayList<Integer> listdata = new ArrayList<Integer>();
		JSONArray jArray = (JSONArray) pedidos;
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				listdata.add(jArray.getInt(i));
			}
		}
		for (Integer Id: listdata) {
			PaqueteDAO.asignar_vehiculo(Id,matricula);
		}
		
		
	}

	public JSONObject LocalizarPedido(int idpedido) {
		JSONObject jso = new JSONObject();
		JSONArray jsa = new JSONArray();
		Pedido p = PaqueteDAO.leer_pedido(idpedido);
		Vehiculo v = VehiculoDAO.leer_vehiculo(p.getVehiculo());
		jsa.put(p.toJSON());
		jsa.put(v.toJSON());
		jso.put("pedido", jsa);
		return jso;
		// TODO Auto-generated method stub
		
	}

	public void actualizar_vehiculo(String matricula_vieja, String matricula_nueva, String marca, String modelo, String color) {
		VehiculoDAO.actualizar_vehiculo(matricula_vieja, matricula_nueva, marca, modelo, color);
		
	}

	public void eliminar_pedidos(Object pedidos) {
		ArrayList<Integer> listdata = new ArrayList<Integer>();
		JSONArray jArray = (JSONArray) pedidos;
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				listdata.add(jArray.getInt(i));
			}
		}
		for (Integer Id: listdata) {
			PaqueteDAO.eliminar_pedido(Id);
		}
		
	}

	public void actualizar_ubi(Object pedidos, String ubi) {
		ArrayList<Integer> listdata = new ArrayList<Integer>();
		JSONArray jArray = (JSONArray) pedidos;
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				listdata.add(jArray.getInt(i));
			}
		}
		for (Integer Id: listdata) {
			PaqueteDAO.actualizar_ubicacion(Id, ubi);
		}

		
	}

}
