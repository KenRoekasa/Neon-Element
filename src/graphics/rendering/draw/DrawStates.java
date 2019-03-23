package graphics.rendering.draw;

import engine.entities.Character;
import graphics.rendering.ISOConverter;
import graphics.rendering.Renderer;
import graphics.rendering.colourSwitch;
import graphics.rendering.textures.Sprites;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.*;

import static graphics.rendering.Renderer.textures;


/**
 * Contains methods for drawing the 'states' of a player
 * States are attacks/shield
 */
class DrawStates {

    /**
     * Start color being used when creating the shield gradient
     */
    private static Color shieldStartColour = new Color(Color.LIGHTSTEELBLUE.getRed(), Color.LIGHTSTEELBLUE.getGreen(), Color.LIGHTSTEELBLUE.getBlue(), 0.5);

    /**
     *  Stop color being used when creating the shield gradient
     */
    private static Color shieldStopColour = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 0.5);

    /**
     * The stop array used for creating the shield gradient
     */
    private static Stop[] stops1 = new Stop[]{new Stop(0.25, shieldStartColour), new Stop(1, shieldStopColour)};

    /**
     * The shield gradient
     */
    private static RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops1);


    /**
     * Draws a heavy attack to the screen
     * @param gc The GraphicsContext being drawn to
     * @param player    The player who's attacking
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param playerCenter  The relative location of the player on the screen
     */
    static void drawHeavyAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 180;
        long angle = Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle);

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));
        gc.setEffect(new MotionBlur(0, 3));

        ISOConverter.applyAngleRotation(gc, angle, playerCenter);
        gc.drawImage(textures.get(Sprites.BLADE), playerCenter.getX() - textures.get(Sprites.BLADE).getWidth(), playerCenter.getY() - textures.get(Sprites.BLADE).getHeight());


        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.drawImage(textures.get(Sprites.BLADE), playerCenter.getX() - textures.get(Sprites.BLADE).getWidth(), playerCenter.getY() - textures.get(Sprites.BLADE).getHeight());


        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.drawImage(textures.get(Sprites.BLADE), playerCenter.getX() - textures.get(Sprites.BLADE).getWidth(), playerCenter.getY() - textures.get(Sprites.BLADE).getHeight());

        ISOConverter.applyAngleRotation(gc, 90, playerCenter);
        gc.drawImage(textures.get(Sprites.BLADE), playerCenter.getX() - textures.get(Sprites.BLADE).getWidth(), playerCenter.getY() - textures.get(Sprites.BLADE).getHeight());


        gc.restore();
    }

    /**
     * Draws a heavy attack charge to the screen
     * @param gc The GraphicsContext being drawn to
     * @param player    The player who's attacking
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param playerCenter  The relative location of the player on the screen
     */
    static void drawHeavyAttackCharge(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAlphaValue = 0;
        long finishAlphaValue = 80;
        float percentCharged = (Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, finishAlphaValue, startAlphaValue)) / 100f;
        //todo get this from player class
        double attackRadius = player.getHeavyAttackHitbox().getRadius();

        gc.save();
        gc.setFill(colourSwitch.getElementColour(player.getCurrentElement()));

        //todo make better

        //inner
        gc.setGlobalAlpha(percentCharged);
        gc.fillOval(playerCenter.getX() - attackRadius / 2f, playerCenter.getY() - attackRadius / 2f, attackRadius, attackRadius);


        gc.restore();

    }

    /**
     * Draws a light attack to the screen
     * @param gc The GraphicsContext being drawn to
     * @param player    The player who's attacking
     * @param remainingAnimDuration The amount of time passed since the attack happened
     *                              Calculated by: character.getCurrentActionStart() + animationDuration - System.currentTimeMillis()
     * @param animationDuration The standard duration of a light attack
     * @param playerCenter  The relative location of the player on the screen
     */
    static void drawLightAttack(GraphicsContext gc, Character player, long remainingAnimDuration, long animationDuration, Point2D playerCenter) {
        long startAngle = 0;
        long finishAngle = 90;

        long angle = (long) (player.getPlayerAngle().getAngle() + Renderer.mapInRange(remainingAnimDuration, 0, animationDuration, startAngle, finishAngle));

        gc.save();

        gc.setEffect(new MotionBlur(0, 3));
        ISOConverter.applyAngleRotation(gc, angle - 45, playerCenter);
        gc.drawImage(textures.get(Sprites.BLADE), playerCenter.getX() - textures.get(Sprites.BLADE).getWidth(), playerCenter.getY() - textures.get(Sprites.BLADE).getHeight());
        gc.restore();

    }

    /**
     * Draws a shield to the screen
     * @param gc The GraphicsContext being drawn to
     * @param player    The player who's shielding
     * @param playerCenter  The relative location of the player on the screen
     */
    static void drawShield(GraphicsContext gc, Character player, Point2D playerCenter) {
        gc.save();
        double shieldWidth = player.getWidth() + 40;

        // using custom colours with lowered opacity on the gradient as gc.setGlobalAlpha was causing me issues making everything transparent
        gc.setFill(lg1);

        gc.fillOval(playerCenter.getX() - shieldWidth / 2, playerCenter.getY() - shieldWidth / 2, shieldWidth, shieldWidth);
        gc.restore();

     }

}
