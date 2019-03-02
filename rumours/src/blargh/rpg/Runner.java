package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Runner {

	public static void main(String... args) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(Paths.get(".").toAbsolutePath().toString());
			
			List<PlaceDto> placeList = Arrays.asList(mapper.readValue(new File("resources/places.json"), PlaceDto[].class));
			List<RouteDto> routeList = Arrays.asList(mapper.readValue(new File("resources/routes.json"), RouteDto[].class));
			
			World.Factory.create(placeList, routeList, new Random(0));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
