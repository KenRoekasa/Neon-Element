package engine.ai.enums;

public enum AiType {
	EASY, NORMAL, HARD;

	public static AiType getType(String type) {
		switch(type.toLowerCase().trim()) {
			default:
			case "easy":
				return AiType.EASY;
			case "normal":
				return AiType.NORMAL;
			case "hard":
				return AiType.HARD;

		}
	}
}
