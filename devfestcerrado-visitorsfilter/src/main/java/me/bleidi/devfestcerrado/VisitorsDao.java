package me.bleidi.devfestcerrado;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;

public class VisitorsDao {
	
	private static VisitorsDao self;
	
	private Datastore datastore;
	private KeyFactory keyFactory;
	
	private VisitorsDao() {
		datastore = DatastoreOptions.getDefaultInstance().getService();
	    keyFactory = datastore.newKeyFactory().setKind("Visitor");
	}
	
	public static VisitorsDao get() {
		if(null == self) {
			synchronized (VisitorsDao.class) {
				if(null == self) {
					self = new VisitorsDao();
				}
			}
		}
		return self;
	}

	public Long create(Visitor visitor) {
		IncompleteKey key = keyFactory.newKey();
	    FullEntity<IncompleteKey> incBookEntity = Entity.newBuilder(key)
	        .set("ip", visitor.getIp())
	        .set("userAgent", visitor.getUserAgent())
	        .build();
	    Entity bookEntity = datastore.add(incBookEntity);
	    return bookEntity.getKey().getId();
	}
	
}
