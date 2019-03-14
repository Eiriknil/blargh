package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Career {

	public List<Skills> skillList(int level);
	public List<Skills> allSkills(int level);
	public Set<Talents> talentSet(int level);
	public List<Characteristics> statList(int level);
	public List<Characteristics> allStats(int level);
	public String name();

	public static class Factory {

		private Factory() {
		}

		private static CareerDto readCareerDefinition(String careerName) {

			ObjectMapper mapper = new ObjectMapper();
			CareerDto careerDef = null;
			try {
				careerDef = mapper.readValue(new File(String.format("resources/%s.json", careerName)), CareerDto.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return careerDef;
		}

		public static Career create(String name) {

			CareerDto careerDef = readCareerDefinition(name);

			return new CareerImpl(careerDef);

		}
		private static class CareerImpl implements Career {

			private CareerDto careerDef;

			public CareerImpl(CareerDto careerDef) {
				this.careerDef = careerDef;
			}

			@Override
			public Set<Talents> talentSet(int level) {

				List<String> talentList = careerDef.getLevel().get(level - 1).get("talents");
				return talentList.stream().map(talentName -> Talents.valueOf(talentName.toUpperCase().replace(" ", "_"))).collect(Collectors.toSet());
			}

			@Override
			public List<Characteristics> statList(int level) {
				List<String> talentList = careerDef.getLevel().get(level - 1).get("stats");
				return talentList.stream().map(statName -> Characteristics.valueOf(statName.toUpperCase().replace(" ", "_"))).collect(Collectors.toList());
			}

			@Override
			public List<Skills> skillList(int level) {
				List<String> skillList = careerDef.getLevel().get(level - 1).get("skills");
				return skillList.stream().map(skillName -> Skills.valueOf(skillName.toUpperCase().replace(" ", "_"))).collect(Collectors.toList());
			}

			@Override
			public String name() {
				return careerDef.getCareer();
			}

			@Override
			public List<Skills> allSkills(int level) {

				List<Skills> allSkills = new CopyOnWriteArrayList<Skills>();
				for(int index = 1; index <= level; index++) {
					allSkills.addAll(skillList(index));
				}

				return allSkills;
			}

			@Override
			public List<Characteristics> allStats(int level) {

				List<Characteristics> allStats = new CopyOnWriteArrayList<Characteristics>();
				for(int index = 1; index <= level; index++) {
					allStats.addAll(statList(index));
				}

				return allStats;
			}

			@Override
			public String toString() {
				return String.format("Career [careerDef=%s]", careerDef);
			}
		}
	}
}