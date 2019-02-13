package blargh.rpg;

import java.util.List;

public interface World {

	public World tick();
	public String state();
	
	public static class Factory{

		public static void create(List<PlaceDto> placeList, List<RouteDto> routeList) {
			
		}
		
	}
	
	public static class Implementation implements World {

		
		
		@Override
		public World tick() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String state() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
