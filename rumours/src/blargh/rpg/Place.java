package blargh.rpg;

import java.util.HashSet;
import java.util.Set;
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
	};

	public Set<Route> routes();
	public void addRumour(Rumour rumour);
	public Set<Rumour> rumours();
	public void addRoute(Route route);
	
	public static class Factory{
		
		private Factory() {
		};
		
		public static Place create(Set<Route> routes) {
			Place place = new Place() {
				
				private Set<Rumour> rumourSet = new HashSet<>();
				private Set<Route> routes = new HashSet<>();

				@Override
				public Set<Rumour> rumours() {

					Set<Rumour> newRumourSet = rumourSet.stream()
							.map(rumour -> rumour.evolve(this))
							.collect(Collectors.toSet());
					newRumourSet.remove(null);
					
					rumourSet = newRumourSet;
					
					return rumourSet;
				}
				
				@Override
				public Set<Route> routes() {

					return new HashSet<Route>(routes);
				}

				@Override
				public void addRumour(Rumour rumour) {
					rumourSet.add(rumour);
				}

				@Override
				public void addRoute(Route route) {
					routes.add(route);
				}
			};

			routes.forEach(place::addRoute);
			
			return place;
		}
		
	}
}
