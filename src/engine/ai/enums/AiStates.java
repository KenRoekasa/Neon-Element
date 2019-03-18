package engine.ai.enums;

public enum AiStates {
	//used by basic and advanced enemies
	IDLE, ATTACK, AGGRESSIVE_ATTACK, ESCAPE, FIND_HEALTH, FIND_DAMAGE, FIND_SPEED, WANDER,
	
	//used by advanced enemies only
	ATTACK_WINNER, 
	
	//used in Hill game type
	GO_TO_HILL, WANDER_ON_HILL, ESCAPE_ON_HILL
}
