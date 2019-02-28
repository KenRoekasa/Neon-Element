package engine.ai;

import java.util.Random;

import engine.calculations.AiCalculations;
import engine.entities.Player;
import engine.enums.PowerUpType;
//high level actions, based on ai states 
public class AiStateActions {
	
	Player aiPlayer;
	Player player;
	AiController aiCon;
	AiCalculations calc;
	AiActions actions;
	
	public AiStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {

		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.player = aiCon.getPlayer();
		this.calc = calc;
		this.actions = actions;
		
	}

	public void wander() {
		if(!aiCon.isWandering()) {
			aiCon.setWandering(true);
			Random r = new Random();
			actions.wanderingDirection = r.nextInt(8);
		}
		actions.startWandering();
	}
	
	public void findSpeed() {
		int index = calc.getNearestPowerUp(PowerUpType.SPEED);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	public void findDamage() {
		int index = calc.getNearestPowerUp(PowerUpType.DAMAGE);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	public void findHealth() {
		int index = calc.getNearestPowerUp(PowerUpType.HEAL);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	public void aggressiveAttack() {
		Player player = calc.getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			aiPlayer.lightAttack();
		}
	}
	
	public void escape() {
		Player player = calc.getNearestPlayer();
		actions.moveAway(player);
	}	
	
	public void attack() {
		Player player = calc.getNearestPlayer();
		actions.moveTo(player);

		if (calc.inAttackDistance(player) && player.getHealth()>0) {
			aiPlayer.lightAttack();
		}
	}
}
