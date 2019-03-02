package blargh.rpg;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

class RouteDtoTest {

	@Test
	void test() {
		RouteDto dto = new RouteDto();
		dto.setFrom("from");
		dto.setTo("to");
		dto.setTime(1);
		dto.setTypes(new String[]{"WALKING"});
		
		assertThat(dto.getFrom(), is("from"));
		assertThat(dto.getTo(), is("to"));
		assertThat(dto.getTime(), is(1));
		assertThat(dto.getTypes(), is(new String[] {"WALKING"}));
		
		assertThat(dto.toString(), is("RouteDto [from=from, to=to, types=[WALKING], time=1]"));
	}

}
     
