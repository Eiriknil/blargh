package blargh.rpg;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface Place {

	public Set<Route> routes();
	public void addRumour(Rumour rumour);
	public Set<Rumour> rumours();
	
	public static class Factory{
		
		public static Place create() {
			return new Place() {
				
				private Set<Rumour> rumourSet = new HashSet<>();

				@Override
				public Set<Rumour> rumours() {

					Set<Rumour> newRumourSet = rumourSet.stream()
							.map(rumour -> rumour.decay(this))
							.collect(Collectors.toSet());
					newRumourSet.remove(null);
					
					rumourSet = newRumourSet;
					
					return rumourSet;
				}
				
				@Override
				public Set<Route> routes() {

					return null;
				}

				@Override
				public void addRumour(Rumour rumour) {
					rumourSet.add(rumour);
				}
			};
		}
		
	}
}
