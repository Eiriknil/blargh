package blargh.rpg;

public interface Time {

	Time add(Time time);
	int time();
	
	public static class Factory {
		public static Time create(int startTime) {
			return new Time() {
				
				int time = startTime;
				
				@Override
				public Time add(Time time) {

					return Time.Factory.create(time.time() + this.time());
				}

				@Override
				public int time() {
					return time;
				}
			};
		}
	}

}
