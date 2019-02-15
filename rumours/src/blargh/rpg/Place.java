package blargh.rpg;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public interface Place {

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
			// TODO Auto-generated method stub
			
		}

		@Override
		public String name() {
			return "Nowhere";
		}
	};

	public Set<Route> routes();
	public void addRumour(Rumour rumour);
	public Rumour createLocalRumour();
	public Set<Rumour> rumours();
	public void addRoute(Route route);
	public void timePasses();
	public String name();
	
	public static class Factory{
		
		private Factory() {
		};
		
		public static Place create(String name, final Set<Route> routes, final int population, Random randomizer) {
			Place place = new Place() {
				
				private Set<Rumour> rumourSet = new HashSet<>();
				private Set<Route> routes = new HashSet<>();
				private int day = 0;
				private int eventChance = calculateEventChance();
				private Map<Integer, Set<Event>> eventHistory = new ConcurrentHashMap<>();
				private int rumourChance = calculateRumourChance();

				@Override
				public Set<Rumour> rumours() {

					Set<Rumour> newRumourSet = rumourSet.stream()
							.map(rumour -> rumour.evolve(this))
							.collect(Collectors.toSet());
					newRumourSet.remove(null);
					
					rumourSet = newRumourSet;
					
					return rumourSet;
				}
				
				private int calculateRumourChance() {
					
					int chance = 100*50/population;
					return chance > 50?50:chance;
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
					return null;
				}

				@Override
				public void timePasses() {
					
					day++;
					
					Event createdEvent = createEvent(day);
					if(createdEvent != Event.NO_EVENT) {
						if(randomizer.nextInt(20/rumourChance) == 0) {
							
						}
					}
				}
				

				private Event createEvent(int day) {
					Event newEvent = Event.NO_EVENT;
					if(randomizer.nextInt(20/eventChance) == 0) {
						Event event = Event.Factory.create(this, "" + day, new HashSet<>());
						Set<Event> eventSet = eventHistory.getOrDefault(day, new HashSet<Event>());
						eventHistory.put(day, eventSet);
						eventSet.add(event);
					}
					
					return newEvent;
				}	

				@Override
				public String name() {
					return name;
				}
			};

			routes.forEach(place::addRoute);
			
			return place;
		}
		
	}
}
