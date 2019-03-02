package blargh.rpg;

import java.util.HashSet;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import blargh.rpg.Route.TravelType;

class RouteTest {

	@Test
	void test() {
		Place origin = Place.Factory.create("origin", new HashSet<>(), 100, new Random(0));
		Place destination = Place.Factory.create("destination", new HashSet<>(), 100, new Random(0));
		int distance = 4;
		TravelType travelMethods = TravelType.HORSE;
		Route route = Route.Factory.create(origin, destination, distance, travelMethods);
		assertThat(route.destination().name(), is("destination"));
		assertThat(route.origin().name(), is("origin"));
		assertThat(route.distance(), is(distance));
	}

}
