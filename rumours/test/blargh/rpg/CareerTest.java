package blargh.rpg;

import static blargh.rpg.Characteristics.T;
import static blargh.rpg.Skills.TRADE_APOTHECARY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

class CareerTest {

	@Test
	void testcareer_readFromFile_correctCareer() {
		
		Career career = Career.Factory.create("testcareers");
		assertThat(career.skillList(1).get(0), is(TRADE_APOTHECARY));
		assertThat(career.allSkills(1).get(0), is(TRADE_APOTHECARY));
		assertThat(career.allSkills(2).size(), is(12));
		assertThat(career.allStats(4).size(), is(6));
		assertThat(career.statList(1).get(0), is(T));
		assertThat(career.allSkills(1).get(0), is(TRADE_APOTHECARY));
		assertThat(career.allSkills(1).get(0), is(TRADE_APOTHECARY));
	}

}
