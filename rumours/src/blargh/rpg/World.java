package blargh.rpg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import blargh.rpg.Route.TravelType;

public interface World {

	public World tick();
	public String state();
	
	public static class Factory{

		public static World create(List<PlaceDto> placeList, List<RouteDto> routeList, Random randomizer) {
			return new Implementation(placeList, routeList, randomizer);
		}
		
	}
	
	public static class Implementation implements World {

		private Set<Place> placeSet = new HashSet<>();
		private Random randomizer;
		
		Implementation(List<PlaceDto> placeList, List<RouteDto> routeList, Random randomizer){
			this.randomizer = randomizer;

			createPlaceSet(placeList);
			createRouteSet(routeList);
		}

		private void createRouteSet(List<RouteDto> routeList) {
			Map<String, Place> placeMap = placeSet.stream().collect(Collectors.toMap(Place::name, place -> place));
			routeList.forEach(routeDto -> {
				String from = routeDto.getFrom();
				String to = routeDto.getTo();
				TravelType[] travelTypes = Arrays.stream(routeDto.getTypes())
						.map(TravelType::valueOf)
						.collect(Collectors.toList())
						.toArray(new TravelType[0]);
				placeMap.get(from).addRoute(Route.Factory.create(placeMap.get(from), placeMap.get(to), routeDto.getTime(), travelTypes));
			});
		}

		private void createPlaceSet(List<PlaceDto> placeList) {
			placeSet = placeList.stream()
					.map(placeDto -> PlaceDto.Factory.createPlace(placeDto, randomizer))
					.collect(Collectors.toSet());
		}
		
		@Override
		public World tick() {
			return null;
		}

		@Override
		public String state() {

			StringBuilder state = new StringBuilder();
			placeSet.stream().map(Place::presentation).collect(Collectors.toList()).forEach(state::append);
			
			return state.toString();
		}
		
	}
}
