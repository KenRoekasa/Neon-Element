package controllers;

import javafx.beans.property.StringProperty;
//create model for representing data player entities
public class PlayerModel {
    private StringProperty health;
    private StringProperty speed;

    public StringProperty healthProperty() {
        return health ;
    }

    public StringProperty speedProperty() {
        return speed ;
    }

    public String getHealth() {
        return health.get();
    }

    public void setHealth(float health) {
        this.health.set(String.valueOf(health));
    }

    public String getSpeed() {
        return speed.get();
    }

    public void setSpeed(float speed) {
        this.speed.set(String.valueOf(speed));
    }

}
