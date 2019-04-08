package blargh.rpg.exception;

public class CharacterIsDeadException extends RuntimeException {

	private static final long serialVersionUID = 3158539238278603285L;

	public CharacterIsDeadException() {
		super("Character has died!");
	}
}
