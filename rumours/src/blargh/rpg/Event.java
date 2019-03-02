package blargh.rpg;

import java.util.HashSet;
import java.util.Set;

public interface Event {

	Event NO_EVENT = new Event() {
		
		private Set<String> participants = new HashSet<String>();

		@Override
		public String time() {
			return "No";
		}
		
		@Override
		public Set<String> participants() {
			return new HashSet<>(participants);
		}
		
		@Override
		public String description() {
			return "No event";
		}
	};

	public String description();
	public Set<String> participants();
	public String time();
	
	public static class Factory {
		
		private Factory() {}
		
		public static Event create(Place place, String time, Set<String> participants) {
			return new Event() {
				
				@Override
				public String time() {
					return time;
				}
				
				@Override
				public Set<String> participants() {
					return new HashSet<>(participants);
				}
				
				@Override
				public String description() {
					return place.name() + " " + time;
				}
			};
		}
	}
}
