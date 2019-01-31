package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Runner {

	public static void main(String... args) {
		System.out.println("starting...");
		ObjectMapper mapper = new ObjectMapper();
		try {
			 System.out.println(Paths.get(".").toAbsolutePath().toString());
			
			List<Object> list = mapper.readValue(new File("resources/places.json"), LinkedList.class);
			list.forEach(value -> System.out.println(value));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
