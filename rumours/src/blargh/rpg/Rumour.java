package blargh.rpg;

public interface Rumour {

	public Rumour travel(Time time, Danger danger, Place place);

	public Rumour evolve(Place place);
	
	public boolean isDead();
	
	public static class Factory {
		
		protected static final Rumour DEAD_RUMOUR = new Rumour() {
			
			@Override
			public Rumour travel(Time time, Danger danger, Place place) {
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
			
			return new Rumour() {
			
				private Time rumourTime = time;
				
				@Override
				public Rumour travel(Time time, Danger danger, Place place) {

					if(danger.avoids()) {
						return Rumour.Factory.create(rumourTime.add(time), Place.NOWHERE);
					}
					
					return DEAD_RUMOUR;
				}
				
				@Override
				public Rumour evolve(Place place) {
					return null;
				}

				@Override
				public boolean isDead() {
					return false;
				}
			};
		}
		
	}
}
