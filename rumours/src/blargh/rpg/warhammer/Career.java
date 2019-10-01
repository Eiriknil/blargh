package blargh.rpg.warhammer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.Character.Skill;
import blargh.rpg.warhammer.Character.Talent;

public interface Career {

	public List<Skill> skillList(int level);
	public List<Skill> allSkills(int level);
	public List<Talent> talentList(int level);
	public List<String> trappingList(int level);
	public List<Characteristics> statList(int level);
	public List<Characteristics> allStats(int level);
	public String name(int level);
	public CareerDto toCareerDto();

	public static class Factory {

		private Factory() {
		}

		private static CareerDto readCareerDefinition(String careerName) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				final CareerDto careerDef = mapper.readValue(new File(String.format("resources/careers/%s.json", careerName)), CareerDto.class);
				final CareerDto careerTrappingsDef = mapper.readValue(new File(String.format("resources/careers/%s_trapping.json", careerName)), CareerDto.class);
				AtomicInteger index = new AtomicInteger(0);
				careerTrappingsDef.getLevel().forEach(trappings -> careerDef.getLevel().get(index.getAndIncrement()).putAll(trappings));
				return careerDef;
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
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
			public List<Talent> talentList(int level) {

				List<String> talentList = careerDef.getLevel().get(level - 1).get("talents");
				return talentList.stream().map(talentName -> new Talent(talentName.toUpperCase()
						.replaceAll("[ /-]", "_")
						.replace("!", ""))).collect(Collectors.toList());
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
			public String name(int level) {
				return String.format("%s (%s)", careerDef.getLevel().get(level - 1).get("name").get(0), careerDef.getCareer());
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

			@Override
			public List<String> trappingList(int level) {
				
				return careerDef.getLevel().subList(0, level).stream()
						.map(attribute -> attribute.get("trappings"))
						.map(list -> list.get(0))
						.collect(Collectors.toList());
			}

			@Override
			public CareerDto toCareerDto() {
				return careerDef;
			}
		}
	}
}
