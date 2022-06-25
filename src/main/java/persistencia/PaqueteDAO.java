package persistencia;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

//import Lanzadora.Model.proyecto;
//import Lanzadora.persistencia.AgenteDB;
import model.Pedido;
import model.Vehiculo;

public class PaqueteDAO {
	
	public static final String ID = "matricula";
	public static final String UBICACIONES = "ubicaciones";
	public static final String VEHICULO = "vehiculo";
	public static final String PEDIDO = "pedido";
	public static final String MATRICULA = "matricula";
	
	public static void insertar(Pedido pedido) {
		Document document;
		MongoCollection<Document> coleccion;
		if (pedido != null) {
			coleccion = AgenteDB.get().getBd(PEDIDO);
			document = new Document(ID, pedido.getId());
			document.append(UBICACIONES, pedido.getUbicaciones());//si da error quiza cambiar list por otro formato
			document.append(VEHICULO, pedido.getVehiculo());

			coleccion.insertOne(document);
			//System.out.println("xx");
		}
	}
	
	public static List<Pedido> leer_pedidos() {

		ArrayList<Pedido> pedidos = new ArrayList<>();
		Document document;
		Pedido p = null;
		MongoCollection<Document> coleccion = AgenteDB.get().getBd(PEDIDO);
		MongoCursor<Document> iter = coleccion.find().iterator();
		
		while ((iter.hasNext())) {
			document = iter.next();
			p = new Pedido(document.getInteger(ID), document.get(UBICACIONES), document.getString(VEHICULO));
			pedidos.add(p);
			
		}

		return pedidos;
	}
	
	public static Pedido leer_pedido(int id) {

		Document document;
		Pedido p = null;
		MongoCollection<Document> coleccion = AgenteDB.get().getBd(PEDIDO);
		MongoCursor<Document> iter = coleccion.find().iterator();
		
		while ((iter.hasNext())) {
			document = iter.next();
			if(id == document.getInteger(ID)) {
				p = new Pedido(document.getInteger(ID), document.get(UBICACIONES), document.getString(VEHICULO));

			}
			
		}

		return p;
	}
	
	public static void asignar_vehiculo(Pedido pedido, String matricula) {

		MongoCollection<Document> coleccion = AgenteDB.get().getBd(PEDIDO);
		 Document findDocument = new Document(ID, pedido.getId());
		 
		// Create the document to specify the update
		    Document updateDocument = new Document("$set",
		        new Document(MATRICULA, matricula));
		    coleccion.findOneAndUpdate(findDocument, updateDocument);
		
		

	}

}


