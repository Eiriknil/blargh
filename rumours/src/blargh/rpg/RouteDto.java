package blargh.rpg;

public class RouteDto {

	private String from;
	private String to;
	private String type;
	private String danger;
	private float time;
	
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDanger() {
		return danger;
	}
	
	public void setDanger(String danger) {
		this.danger = danger;
	}
	
	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RouteDto [from=" + from + ", to=" + to + ", type=" + type + ", danger=" + danger + ", time=" + time
				+ "]";
	}
}
