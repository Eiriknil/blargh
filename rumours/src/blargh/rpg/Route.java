package blargh.rpg;

import blargh.rpg.Route.TravelType;

public interface Route {

	public enum TravelType {
		WALKING,
		COACH,
		HORSE,
		BOAT
	}

	public Place origin();
	public Place destination();
	public int duration(TravelType travelType);
	public int distance();
	
	public static class Factory {
		
		public static Route create(Place origin, Place destination, int distance, TravelType... travelMethods) {
			return new Route() {
				
				@Override
				public Place origin() {
					return origin;
				}
				
				@Override
				public int duration(TravelType travelType) {
					return 0;
				}
				
				@Override
				public int distance() {
					return distance;
				}
				
				@Override
				public Place destination() {
					return destination;
				}
			};
		}
	}
}
