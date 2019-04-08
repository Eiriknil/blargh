package blargh.rpg;

import static blargh.rpg.Characteristics.T;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

class CareerTest {

	@Test
	void testcareer_readFromFile_correctCareer() {
		
		Career career = Career.Factory.create("apothecary");
		assertThat(career.allSkills(2).size(), is(14));
		assertThat(career.allStats(4).size(), is(6));
		assertThat(career.statList(1).get(0), is(T));
	}

}
