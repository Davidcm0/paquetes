package persistencia;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class AgenteDB {

	private MongoClientURI uri;
	private MongoClient mongoClient;
	private MongoDatabase database;
	

	public AgenteDB() {

		uri = new MongoClientURI(
				"mongodb://david:david@ac-bfbavpy-shard-00-00.zuobnhu.mongodb.net:27017,ac-bfbavpy-shard-00-01.zuobnhu.mongodb.net:27017,ac-bfbavpy-shard-00-02.zuobnhu.mongodb.net:27017/?ssl=true&replicaSet=atlas-gd5ufy-shard-0&authSource=admin&retryWrites=true&w=majority");
		mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase("Rfranco");

	}

	private static class BrokerHolder {
		private static AgenteDB singleton = new AgenteDB();
	}

	public static AgenteDB get() {
		return BrokerHolder.singleton;

	}

	public MongoCollection<Document> getBd(String collection) {
		if (this.database == null) {
			AgenteDB.get();
			return getBd(collection);
		} else {
			return this.database.getCollection(collection);
		}
	}

}
