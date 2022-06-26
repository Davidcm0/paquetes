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

	public void sendProyecto(String nombre) {
		boolean enviado = false;
		// boolean proyecto_enviado = true;

		DataInputStream input;
		BufferedInputStream bis;
		BufferedOutputStream bos;
		int in;
		byte[] byteArray;
		// Fichero a transferir

		System.out.println(archivo.getRuta());
		final String filename = archivo.getRuta();

		try {
			final File localFile = new File(filename);
			Socket client = new Socket("localhost", 5000);
			bis = new BufferedInputStream(new FileInputStream(localFile));
			bos = new BufferedOutputStream(client.getOutputStream());
			// Enviamos el nombre del fichero
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());

			dos.writeUTF(localFile.getName());

			// Enviamos el fichero
			byteArray = new byte[8192];
			while ((in = bis.read(byteArray)) != -1) {

				bos.write(byteArray, 0, in);
				
			}
			enviado = true;
			bis.close();
			bos.close();

		} catch (Exception e) {
			System.err.println(e);
		}
		if (enviado)
			proyectoDAO.proyecto_enviado(nombre, true);
	}

	public void explorador() {
		Scanner entrada = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(fileChooser);
		try {
			String ruta = fileChooser.getSelectedFile().getAbsolutePath();

			archivo.setRuta(ruta);
			System.out.println(ruta);
			File f = new File(ruta);
			entrada = new Scanner(f);

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("No se ha seleccionado ningún fichero");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (entrada != null) {
				entrada.close();
			}
		}

	}

	public void crearProyecto(String nombre, int repeticiones, String lenguaje, String descripcion, int dut,
			String usuario) {
		User usuarioDef = new User();
		List<User> usuarios = UserDAO.leerUsers();
		for(User user : usuarios) {
			if(user.getName().equals(usuario)) {
				
				usuarioDef.setEmail(user.getEmail());
						
			}
			
		}
		java.util.Date fecha = new Date();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		System.out.println(dtf);
		System.out.println("yyyy/MM/dd HH:mm:ss-> " + dtf.format(LocalDateTime.now()));
		// fecha = dtf.format(LocalDateTime.now());
		System.out.println(fecha);
		String estado = "En preparación";
		Boolean enviado = false;
		proyectoDAO.insertar(
				new proyecto(fecha, nombre, descripcion, dut, repeticiones, lenguaje, usuario, usuarioDef.getEmail(), estado, enviado, "",""));

	}

	public JSONObject leer_proyectos(String nombre) {
		JSONArray jsa = new JSONArray();
		JSONObject jso = new JSONObject();
		List<proyecto> proyectos = proyectoDAO.leer_proyectos(nombre);

		for (proyecto proyecto : proyectos) {

			jsa.put(proyecto.toJSON());
		}
		jso.put("proyectos", jsa);

		return jso;

	}

	public void register(String matricula, String password, String marca, String modelo, String color) {
		Boolean validado = false;
		VehiculoDAO.insertar(new Vehiculo(matricula, marca, modelo, color, encriptarMD5(password)));

	}

	public void actualizar_estado(String proyecto, String estado, String url, String password) {
       
		
		if(estado.equals("En preparacion")){
        	estado = "En preparación";
        }
		if(!estado.equals("Elige...") ){
			proyectoDAO.actualizar_estado(proyecto, estado);
		}
		
		if(!url.equals("")) {
			proyectoDAO.actualizar_url(proyecto, url);
		}
		if(!password.equals("")) {
			proyectoDAO.actualizar_password(proyecto, password);
		}
		

	}

	public JSONObject resultados(String proyecto, String usuario) {

		resultados.setNombre(proyecto + "_" + usuario);
		Double Time[] = new Double[14];
		Double HDD[] = new Double[14];
		Double Graphs[] = new Double[14];
		Double Procesador[] = new Double[14];
		Double Monitor[] = new Double[14];
		Double DUT[] = new Double[14];
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		int j = 4;
		try {

			AgenteMariaDB maria = AgenteMariaDB.getMariaDB();

			ResultSet rs = AgenteMariaDB.hacer_consulta(resultados.getNombre());

			while (rs.next()) {
				switch (rs.getString(3)) {
				case "time":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						Time[i] = rs.getDouble(j);
						Time[i] = Math.round(Time[i]*100.0)/100.0;
						j++;
					}
					resultados.setTime(Time);

					break;

				case "hdd":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						HDD[i] = rs.getDouble(j);
						HDD[i] = Math.round(HDD[i]*100.0)/100.0;
						j++;
					}

					resultados.setHDD(HDD);
					break;

				case "graphicscard":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						Graphs[i] = rs.getDouble(j);
						Graphs[i] = Math.round(Graphs[i]*100.0)/100.0;
						j++;
					}
					resultados.setGraphs(Graphs);

					break;

				case "processor":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						Procesador[i] = rs.getDouble(j);
						Procesador[i] = Math.round(Procesador[i]*100.0)/100.0;
						j++;
					}
					resultados.setProcesador(Procesador);

					break;

				case "monitor":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						Monitor[i] = rs.getDouble(j);
						Monitor[i] = Math.round(Monitor[i]*100.0)/100.0;
						j++;
					}
					resultados.setMonitror(Monitor);

					break;

				case "dut":
					j = 4;
					for (int i = 0; i < Time.length; i++) {
						DUT[i] = rs.getDouble(j);
						DUT[i] = Math.round(DUT[i]*100.0)/100.0;
						j++;
					}
					resultados.setDUT(DUT);

					break;

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray jsa = new JSONArray();
		JSONObject jso = new JSONObject();

		jsa.put(resultados.toJSON());

		jso.put("resultados", jsa);

		return jso;

	}

	public Object leer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void modificarUsuario(String nombre, String new_nombre, String descripcion, int dut, int repeticiones,
			String lenguaje) {
		proyectoDAO.modificar(nombre, new_nombre, descripcion, dut, repeticiones, lenguaje);

	}

	public JSONObject leer_pedidos() {
		JSONArray jsa = new JSONArray();
		JSONObject jso = new JSONObject();
		List<Pedido> pedidos = PaqueteDAO.leer_pedidos();

		for (Pedido pedido : pedidos) {

			jsa.put(pedido.toJSON());
		}
		jso.put("pedidos", jsa);

		return jso;
	}

	public void validar_user(String usuario) {
		UserDAO.validar(usuario);

	}

	public void eliminar_users() {
		UserDAO.eliminarAll();

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

}
