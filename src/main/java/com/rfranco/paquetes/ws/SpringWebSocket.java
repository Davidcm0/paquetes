package com.rfranco.paquetes.ws;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import model.Manager;

//import Lanzadora.Model.Manager;

//import Lanzadora.Model.Manager;

@Component
public class SpringWebSocket extends TextWebSocketHandler {

	private static final String VEHICULO = "vehiculo";
	private static final String MATRICULA = "matricula";
	private static final String IDPEDIDO = "id_pedido";
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
			session.sendMessage(new TextMessage(Manager.get().leer_pedidos().toString()));

			 //Manager.get().leer_proyectos(jso.getString(NOMBRE));
			break;
		case "asignar":
			
			  Manager.get().AsignarPedidos(jso.getString(MATRICULA), jso.get("pedidos"));
			break;
		}
		
		
		
	}
}
