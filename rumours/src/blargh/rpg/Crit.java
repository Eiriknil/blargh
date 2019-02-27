package blargh.rpg;

import blargh.rpg.Character.HitLocation;

public interface Crit {

	public int level();
	public Character.HitLocation location();
	
	public static class Factory {
		
		public static Crit create(final int critLevel) {
			return new Crit() {

				HitLocation location = Character.randomHitLocation();
				
				@Override
				public int level() {
					return 0;
				}

				@Override
				public HitLocation location() {
					return null;
				}
				
			};
		}
	}
}
