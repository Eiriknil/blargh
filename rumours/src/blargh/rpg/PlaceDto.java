package blargh.rpg;

import java.util.HashSet;
import java.util.Random;

public class PlaceDto {

	private String name;
	private int population;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	@Override
	public String toString() {
		return "PlaceDto [name=" + name + ", population=" + population + "]";
	}
	
	public static class Factory {
		public static Place createPlace(PlaceDto placeDto, Random randomizer) {
			return Place.Factory.create(placeDto.getName(), new HashSet<>(), placeDto.getPopulation(), randomizer);
		}
	}
}
