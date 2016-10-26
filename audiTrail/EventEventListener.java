package org.iqvis.nvolv3.audiTrail;

import org.iqvis.nvolv3.domain.Event;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

import com.mongodb.DBObject;

public class EventEventListener extends AbstractMongoEventListener<Event> {

	// @Autowired
	// MongoTemplate mongoTemplate;

	@Override
	public void onApplicationEvent(MongoMappingEvent<?> event) {
		// TODO Auto-generated method stub
		super.onApplicationEvent(event);

	}

	@Override
	public void onBeforeConvert(Event source) {
		// TODO Auto-generated method stub
		super.onBeforeConvert(source);
		// System.out.println("onBeforeConvert Event update");
		//
		// Javers javers = JaversBuilder.javers().build();
		//
		// Person tommyOld = new Person("tommy", "Tommy Smart");
		//
		// Person tommyNew = new Person("tommy", "Tommy C. Smart");
		//
		// Diff diff = javers.compare(tommyOld, tommyNew);
		//
		// then there should be one change of type {@link ValueChange}
		//
		// ValueChange change = diff.getChangesByType(ValueChange.class).get(0);
		//
		// System.out.println(((Person)change.getAffectedCdo()).getLogin());

	}

	@Override
	public void onBeforeSave(Event source, DBObject dbo) {
		// TODO Auto-generated method stub
		super.onBeforeSave(source, dbo);

		// System.out.println("onBeforeSave Event update");
	}

	@Override
	public void onAfterSave(Event source, DBObject dbo) {
		// TODO Auto-generated method stub
		super.onAfterSave(source, dbo);

		// System.out.println("onAfterSave Event update");

	}

	@Override
	public void onAfterLoad(DBObject dbo) {
		// TODO Auto-generated method stub
		super.onAfterLoad(dbo);

		// System.out.println("onAfterLoad Event update");

	}

	@Override
	public void onAfterConvert(DBObject dbo, Event source) {
		// TODO Auto-generated method stub
		super.onAfterConvert(dbo, source);

		// System.out.println("onAfterConvert Event update");
	}

	@Override
	public void onAfterDelete(DBObject dbo) {
		// TODO Auto-generated method stub
		super.onAfterDelete(dbo);

		// System.out.println("onAfterDelete Event update");

	}

	@Override
	public void onBeforeDelete(DBObject dbo) {
		// TODO Auto-generated method stub
		super.onBeforeDelete(dbo);

		// System.out.println("onBeforeDelete Event update");

	}

}
