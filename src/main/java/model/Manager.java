package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.FileOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


//import excepciones.CredencialesInvalidasException;
import persistencia.PaqueteDAO;
import persistencia.VehiculoDAO;

import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	public JSONObject comparar(Object proyectos, String usuario) {

		ArrayList<String> listdata = new ArrayList<String>();
		JSONArray jArray = (JSONArray) proyectos;
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				listdata.add(jArray.getString(i));
			}
		}
		
		JSONObject jso = new JSONObject();
		JSONObject jso2 = new JSONObject();
		JSONArray jsa = new JSONArray();
		
		for (int i = 0; i < listdata.size(); i++) {

			 jso2 = resultados(listdata.get(i), usuario);
			 Iterator x = jso2.keys();
			 while (x.hasNext()){
				    String key = (String) x.next();
				    jsa.put(jso2.get(key));
				}
			//jsa.put(resultados.toJSON());
			//jsa.put(jso2);
		}
		
		jso.put("graficas", jsa);
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
