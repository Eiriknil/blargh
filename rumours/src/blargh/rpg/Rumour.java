package blargh.rpg;

public interface Rumour {

	public Rumour travel(Time time, Place place);

	public Rumour evolve(Place place);
	
	public boolean isDead();
	
	public static class Factory {
		
		private Factory() {}
		
		protected static final Rumour DEAD_RUMOUR = new Rumour() {
			
			@Override
			public Rumour travel(Time time, Place place) {
				return this;
			}
			
			@Override
			public boolean isDead() {
				return true;
			}
			
			@Override
			public Rumour evolve(Place place) {
				return this;
			}
		};

		public static Rumour create(final Time time, final Place place) {
			
			Rumour newRumour = new Implementation(time);
			
			place.addRumour(newRumour);
			
			return newRumour;
		}
		
	}
	
	public static class Implementation implements Rumour {

		private Time rumourTime;
		
		@SuppressWarnings("unused")
		private Implementation() {}
		
		public Implementation(Time time) {
			this.rumourTime = time;
		}

		@Override
		public Rumour travel(Time time, Place place) {

			return new Implementation(this.rumourTime.add(time));
		}
		
		@Override
		public Rumour evolve(Place place) {
			return null;
		}

		@Override
		public boolean isDead() {
			return false;
		}
	}
}
