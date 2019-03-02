package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

class WorldTest {

	private static final StringBuilder WORLD_STATE = new StringBuilder("");

	@Test
	void test() {

		ObjectMapper mapper = new ObjectMapper();
		
		try {

			List<PlaceDto> placeList = Arrays.asList(mapper.readValue(new File("resources/places.json"), PlaceDto[].class));
			List<RouteDto> routeList = Arrays.asList(mapper.readValue(new File("resources/routes.json"), RouteDto[].class));

			World world = World.Factory.create(placeList, routeList, new Random(0));

			List<String> expectedWorldState = Files.readAllLines(Paths.get("resources/world_state.txt"));
			Collections.sort(expectedWorldState);
			List<String> stateSplitList = Arrays.asList(world.state().split("\\\n"));
			Collections.sort(stateSplitList);
			
			assertThat(stateSplitList, is(expectedWorldState));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
