package blargh.rpg;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;
import org.junit.jupiter.api.Test;

class TimeTest {

	@Test
	void test() {
		Time time = Time.Factory.create(1);
		assertThat(time.time(), is(1));
		time = time.add(Time.Factory.create(4));
		assertThat(time.time(), is(5));
	}

}
