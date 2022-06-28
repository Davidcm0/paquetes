package com.rfranco.paquetes.ws;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import model.Manager;


@Component
public class SpringWebSocket extends TextWebSocketHandler {

	private static final String VEHICULO = "vehiculo";
	private static final String MATRICULA = "matricula";
	private static final String IDPEDIDO = "IdPedido";
	private static final String UBICACION = "ubicacion";
	private static final String TYPE = "type";


	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Manager.get().setSession(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload().toString());
		
		switch (jso.getString(TYPE)) {
		case "ready":
			session.sendMessage(new TextMessage(Manager.get().leer_pedidos(jso.getString(MATRICULA)).toString()));

			 
			break;
			
		case "asignar":
			
			  Manager.get().AsignarPedidos(jso.getString(MATRICULA), jso.get("pedidos"));
			break;
			
		case "localizar":
			session.sendMessage(new TextMessage( Manager.get().LocalizarPedido(jso.getInt(IDPEDIDO)).toString()));
			  
			break;
			
		case "ActualizarVehiculo":
			Manager.get().actualizar_vehiculo(jso.getString("matriculaVieja"),jso.getString("matriculaNueva"), jso.getString("marca"), jso.getString("modelo"), jso.getString("color"));
			break;
			
		case "eliminar":
			Manager.get().eliminar_pedidos(jso.get("pedidos"));
			break;
			
		case "actualizarUbi":
			Manager.get().actualizar_ubi(jso.get("pedidos"),jso.getString("ubicacion"));
			break;
		}
		
		
		
	}
}
