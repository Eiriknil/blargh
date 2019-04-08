package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.Character.Skill;

public interface Career {

	public List<Skill> skillList(int level);
	public List<Skill> allSkills(int level);
	public List<Talents> talentList(int level);
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
				careerDef = mapper.readValue(new File(String.format("resources/careers/%s.json", careerName)), CareerDto.class);
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
			public List<Talents> talentList(int level) {

				List<String> talentList = careerDef.getLevel().get(level - 1).get("talents");
				return talentList.stream().map(talentName -> Talents.valueOf(talentName.toUpperCase()
						.replaceAll("[ /-]", "_")
						.replace("(", "_")
						.replace("!", "")
						.replace(")", ""))).collect(Collectors.toList());
			}

			@Override
			public List<Characteristics> statList(int level) {
				List<String> talentList = careerDef.getLevel().get(level - 1).get("stats");
				return talentList.stream().map(statName -> Characteristics.valueOf(statName.toUpperCase().replace(" ", "_"))).collect(Collectors.toList());
			}

			@Override
			public List<Skill> skillList(int level) {
				List<String> skillNameList = careerDef.getLevel().get(level - 1).get("skills");
				return skillNameList.stream().map(skillName -> skillName.toUpperCase()
						.replaceAll(" ", "_")
						.replace("-", "_"))
				.map(skillName -> new Skill(skillName))
				.collect(Collectors.toList());
			}

			@Override
			public String name() {
				return careerDef.getCareer();
			}

			@Override
			public List<Skill> allSkills(int level) {

				List<Skill> allSkills = new CopyOnWriteArrayList<>();
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
