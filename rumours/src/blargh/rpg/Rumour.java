package blargh.rpg;

public interface Rumour {

	public Rumour travel(Time time, Danger danger);

	public Rumour decay(Place place);
	
	public boolean isDead();
	
	public static class Factory {
		
		protected static final Rumour DEAD_RUMOUR = new Rumour() {
			
			@Override
			public Rumour travel(Time time, Danger danger) {
				return this;
			}
			
			@Override
			public boolean isDead() {
				return true;
			}
			
			@Override
			public Rumour decay(Place place) {
				return this;
			}
		};

		public static Rumour create(final Time time) {
			
			return new Rumour() {
			
				private Time rumourTime = time;
				
				@Override
				public Rumour travel(Time time, Danger danger) {

					if(danger.avoids()) {
						return Rumour.Factory.create(rumourTime.add(time));
					}
					
					return DEAD_RUMOUR;
				}
				
				@Override
				public Rumour decay(Place place) {
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
