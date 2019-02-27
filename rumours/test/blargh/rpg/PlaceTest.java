package blargh.rpg;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import blargh.rpg.Route.TravelType;

class PlaceTest {

	@Test
	void testNowhere() {
		
		assertThat(Place.NOWHERE.name(), is("Nowhere"));
		Place.NOWHERE.addRoute(Route.Factory.create(Place.NOWHERE, Place.NOWHERE, 0, TravelType.WALKING));
		assertThat(Place.NOWHERE.routes().size(), is(0));
		Place.NOWHERE.addRumour(Rumour.Factory.create(Time.Factory.create(1), Place.NOWHERE));
		assertThat(Place.NOWHERE.rumours().size(), is(0));
		assertThrows(RuntimeException.class, Place.NOWHERE::createLocalRumour);
	}

}
