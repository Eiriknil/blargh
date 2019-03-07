package blargh.rpg;

import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.Weapon.WeaponDTO;

public interface Weapon {

	public int damage(int strengthBonus, int successLevel);
	public Set<WeaponQuality> qualities();
	public Set<WeaponFlaws> flaws();
	public String toJson();

	public static class Factory {
		public static Weapon create(String name, String group, String price, String enc, String availability, String reach, String damage, String qualitiesFlaws) {
			return new Weapon() {
				
				@Override
				public Set<WeaponQuality> qualities() {
					return null;
				}
				
				@Override
				public Set<WeaponFlaws> flaws() {
					return null;
				}
				
				@Override
				public int damage(int strengthBonus, int successLevel) {
					return 0;
				}

				@Override
				public String toJson() {
					ObjectMapper mapper = new ObjectMapper();
					
					WeaponDTO weaponDTO = new WeaponDTO(name, group, price, enc, availability, reach, damage, qualitiesFlaws);
					String result = "";
					try {
						
						result  =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(weaponDTO);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					
					return result;
				}
			};
		}
	}
	
	public static class WeaponDTO {
		private String name;
		private String group;
		private String price;
		private String enc;
		private String availability;
		private String reach;
		private String damage;
		private String qualitiesFlaws;

		public WeaponDTO(String name, String group, String price, String enc, String availability, String reach,
				String damage, String qualitiesFlaws) {
					this.name = name;
					this.group = group;
					this.price = price;
					this.enc = enc;
					this.availability = availability;
					this.reach = reach;
					this.damage = damage;
					this.qualitiesFlaws = qualitiesFlaws;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getEnc() {
			return enc;
		}

		public void setEnc(String enc) {
			this.enc = enc;
		}

		public String getAvailability() {
			return availability;
		}

		public void setAvailability(String availability) {
			this.availability = availability;
		}

		public String getReach() {
			return reach;
		}

		public void setReach(String reach) {
			this.reach = reach;
		}

		public String getDamage() {
			return damage;
		}

		public void setDamage(String damage) {
			this.damage = damage;
		}

		public String getQualitiesFlaws() {
			return qualitiesFlaws;
		}

		public void setQualitiesFlaws(String qualitiesFlaws) {
			this.qualitiesFlaws = qualitiesFlaws;
		}
	}
}
