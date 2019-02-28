package blargh.rpg;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public interface Place {

	public String PRESENTATION = "Name: %s Population: %d\nRoutes:\n";
	public Place NOWHERE = new Place() {

		private final Set<Route> routes = new HashSet<>();
		private final Set<Rumour> rumours = new HashSet<>();

		@Override
		public Set<Rumour> rumours() {
			return rumours;
		}

		@Override
		public Set<Route> routes() {
			return routes;
		}

		@Override
		public void addRumour(Rumour rumour) {
			// No rumours nowhere
		}

		@Override
		public void addRoute(Route route) {
			// No routes nowhere
		}

		@Override
		public Rumour createLocalRumour() {
			throw new RuntimeException("Rumours need to be created somewhere!");
		}

		@Override
		public void timePasses() {
			// Time is endless...
		}

		@Override
		public String name() {
			return "Nowhere";
		}

		@Override
		public String presentation() {
			return String.format(PRESENTATION, "Nowhere", 0);
		}
	};

	public Set<Route> routes();
	public void addRumour(Rumour rumour);
	public Rumour createLocalRumour();
	public Set<Rumour> rumours();
	public void addRoute(Route route);
	public void timePasses();
	public String name();
	public String presentation();

	public static class Factory{

		private Factory() {
		}

		public static Place create(String name, final Set<Route> routes, final int population, Random randomizer) {
			Place place = new Implementation(name, routes, population, randomizer);

			routes.forEach(place::addRoute);

			return place;
		}

		static class Implementation implements Place {

			private Set<Rumour> rumourSet = new HashSet<>();
			private Set<Route> routes = new HashSet<>();
			private int day = 0;
			private int eventChance = 1;
			private Map<Integer, Set<Event>> eventHistory = new ConcurrentHashMap<>();
			private String name;
			private int population;
			private Random randomizer;

			public Implementation(String name, Set<Route> routes, int population, Random randomizer) {
				this.name = name;
				this.routes = routes;
				this.population = population;
				this.randomizer = randomizer;
				eventChance = calculateEventChance();
			}

			@Override
			public Set<Rumour> rumours() {

				Set<Rumour> newRumourSet = rumourSet.stream()
						.map(rumour -> rumour.evolve(this))
						.collect(Collectors.toSet());
				newRumourSet.remove(null);

				rumourSet = newRumourSet;

				return rumourSet;
			}

			private int calculateEventChance() {

				int chance = 20*50/population;
				return chance > 10?10:chance;
			}

			@Override
			public Set<Route> routes() {

				return new HashSet<>(routes);
			}

			@Override
			public void addRumour(Rumour rumour) {
				rumourSet.add(rumour);
			}

			@Override
			public void addRoute(Route route) {
				routes.add(route);
			}

			@Override
			public Rumour createLocalRumour() {
				return Rumour.Factory.create(Time.Factory.create(day), this);
			}

			@Override
			public void timePasses() {

				day++;

				Event createdEvent = createEvent(day);
				if(createdEvent != Event.NO_EVENT) {
					Rumour localRumour = createLocalRumour();
					addRumour(localRumour);
				}
			}


			private Event createEvent(int day) {
				Event newEvent = Event.NO_EVENT;
				int chance = 20/eventChance;
				int roll = randomizer.nextInt(chance);
				if(roll == 0) {
					newEvent = Event.Factory.create(this, "" + day, new HashSet<>());
					Set<Event> eventSet = eventHistory.getOrDefault(day, new HashSet<Event>());
					eventHistory.put(day, eventSet);
					eventSet.add(newEvent);
				}

				return newEvent;
			}	

			@Override
			public String name() {
				return name;
			}

			@Override
			public String presentation() {

				StringBuilder presentation = new StringBuilder(String.format(PRESENTATION, name, population));
				routes.stream()
				.map(Route::presentation)
				.forEach(presentation::append);

				return presentation.toString();
			}

			@Override
			public String toString() {
				return "Implementation [routes=" + routes + ", name=" + name + ", population=" + population + "]";
			}
		}
	}
}
