package persistencia;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

//import Lanzadora.Model.User;
//import Lanzadora.persistencia.AgenteDB;
//import Lanzadora.Model.proyecto;
//import Lanzadora.persistencia.AgenteDB;
import model.Vehiculo;

public class VehiculoDAO {

	public static final String MATRICULA = "matricula";
	public static final String MARCA = "marca";
	public static final String MODELO = "modelo";
	public static final String COLOR = "color";
	public static final String PASSWORD = "password";
	public static final String VEHICULO = "vehiculo";
	
	
	private VehiculoDAO() {
		
	}
	
	public static void insertar(Vehiculo vehiculo) {
		Document document;
		MongoCollection<Document> coleccion;
		if (vehiculo != null) {
			coleccion = AgenteDB.get().getBd(VEHICULO);
			document = new Document("matricula", vehiculo.getMatricula());
			document.append("marca", vehiculo.getMarca());
			document.append("password", vehiculo.getPassword());
			document.append("modelo", vehiculo.getModelo());
			document.append("color", vehiculo.getColor());

			coleccion.insertOne(document);
			//System.out.println("xx");
		}
	}
	
	
	public static void actualizar_ubicacion(String matricula, String ubicacion) {
//		MongoCollection<Document> coleccion = AgenteDB.get().getBd(PROYECTO);
//		 Document findDocument = new Document("nombre", proyecto);
//		 
//		// Create the document to specify the update
//		    Document updateDocument = new Document("$set",
//		        new Document("estado", estado));
//		    coleccion.findOneAndUpdate(findDocument, updateDocument);
//		
	}
	
	public static List<Vehiculo> leer_vehiculos() {
		ArrayList<Vehiculo> vehiculos = new ArrayList<>();
		Document document;
		Vehiculo v= null;
		MongoCollection<Document> coleccion = AgenteDB.get().getBd(VEHICULO);
		MongoCursor<Document> iter = coleccion.find().iterator();
		
		while ((iter.hasNext())) {
			document = iter.next();
			v = new Vehiculo(document.getString(MATRICULA), document.getString(MARCA), document.getString(MODELO), document.getString(COLOR), document.getString(PASSWORD));
			vehiculos.add(v);		
		}

		return vehiculos;
	}
	
	public static Vehiculo leer_vehiculo(String matricula) {
		Document document;
		Vehiculo v= null;
		MongoCollection<Document> coleccion = AgenteDB.get().getBd(VEHICULO);
		MongoCursor<Document> iter = coleccion.find().iterator();
		
		while ((iter.hasNext())) {
			document = iter.next();
			if(matricula.equals(document.getString(MATRICULA))) {
				v = new Vehiculo(document.getString(MATRICULA), document.getString(MARCA), document.getString(MODELO), document.getString(COLOR), document.getString(PASSWORD));
				
			}
					
		}

		return v;
	}
	
	
}
