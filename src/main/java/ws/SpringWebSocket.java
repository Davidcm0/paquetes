package ws;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//import Lanzadora.Model.Manager;

@Component
public class SpringWebSocket extends TextWebSocketHandler {

	private static final String VEHICULO = "vehiculo";
	private static final String MATRICULA = "matricula";
	private static final String IDPEDIDO = "id_pedido";
	private static final String UBICACION = "ubicacion";


	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//Manager.get().setSession(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload().toString());
		
	}
}
