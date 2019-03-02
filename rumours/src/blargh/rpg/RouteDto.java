package blargh.rpg;

import java.util.Arrays;

public class RouteDto {

	private String from;
	private String to;
	private String[] types;
	private int time;
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String[] getTypes() {
		return types;
	}
	
	public void setTypes(String[] types) {
		this.types = types;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RouteDto [from=" + from + ", to=" + to + ", types=" + Arrays.toString(types) + ", time=" + time + "]";
	}
}
