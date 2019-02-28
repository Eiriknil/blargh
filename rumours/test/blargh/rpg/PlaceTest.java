package blargh.rpg;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

	@Test
	void createPlace_givenDataForPlace_correctPlace() {
		
		Random randomizer = new Random(0);
		Set<Route> routes = new HashSet<>();
		String name = "Place";
		Place place = Place.Factory.create(name, routes, 1000, randomizer);
		assertThat(place.name(), is(name));
		assertThat(place.routes().size(), is(0));
		Route route = Route.Factory.create(place, Place.NOWHERE, 0, TravelType.WALKING);
		place.addRoute(route);
		assertThat(place.routes().size(), is(1));
		assertThat(place.routes(), hasItem(route));
		
		place.timePasses();
		assertThat(place.rumours().size(), is(1));
		place.timePasses();
		place.timePasses();
	}
}
