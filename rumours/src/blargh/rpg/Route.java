package blargh.rpg;

import java.util.Arrays;

public interface Route {

	public enum TravelType {
		WALKING(4),
		COACH(8),
		HORSE(8),
		BOAT(6);

		private int speed;

		private TravelType(int speed) {
			this.speed = speed;
		}
		
		public int speed() {
			return speed;
		}
	}

	public Place origin();
	public Place destination();
	public int duration(TravelType travelType);
	public int distance();
	public String presentation();

	public static class Factory {

		protected static final String PRESENTATION = "From %s to %s, distance: %d\n";

		public static Route create(Place origin, Place destination, int distance, TravelType... travelMethods) {
			return new Implementation(origin, destination, distance, travelMethods); 
		}

		static class Implementation implements Route {

			private Place origin;
			private Place destination;
			private int distance;
			private TravelType[] travelMethods;

			public Implementation(Place origin, Place destination, int distance, TravelType[] travelMethods) {
				if(origin == null) {
					origin = Place.NOWHERE;
				}
				this.origin = origin;
				if(destination == null) {
					destination = Place.NOWHERE;
				}
				this.destination = destination;
				this.distance = distance;
				this.travelMethods = travelMethods;
			}

			@Override
			public Place origin() {
				return origin;
			}

			@Override
			public Place destination() {
				return destination;
			}

			@Override
			public int duration(TravelType travelType) {
				return distance/travelType.speed();
			}

			@Override
			public int distance() {
				return distance;
			}

			@Override
			public String presentation() {
				return String.format(PRESENTATION, origin.name(), destination.name(), distance);
			}

			@Override
			public String toString() {
				return "Route [ origin = " + origin.name() + ", destination = " + destination.name() + ", distance = " + distance + ", travelMethods = " + Arrays.toString(travelMethods) + "]";
			}
		}
	}
}
