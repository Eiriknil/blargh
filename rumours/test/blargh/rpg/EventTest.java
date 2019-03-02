package blargh.rpg;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import org.junit.jupiter.api.Test;

class EventTest {

	private static final String TIME = "2019-02-27";
	private static final String PLACE_DESCRIPTION = "SomeWhere";

	@Test
	void createEvent_eventData_correctEventDataInEvent() {
		String participants1 = "Some people";
		String participants2 = "Some other people";
		HashSet<String> participants = new HashSet<String>(Arrays.asList(participants1, participants2));
		Event event = Event.Factory.create(Place.Factory.create(PLACE_DESCRIPTION, new HashSet<>(), 100, new Random(0)), TIME, participants);
		assertThat(event.description(), is(PLACE_DESCRIPTION + " " + TIME));
		assertThat(event.time(), is(TIME));
		assertThat(event.participants(), hasItem(participants1));
		assertThat(event.participants(), hasItem(participants2));
		
		assertThat(Event.NO_EVENT.description(), is("No event"));
		assertThat(Event.NO_EVENT.time(), is("No"));
		assertThat(Event.NO_EVENT.participants().isEmpty(), is(true));
	}

}
