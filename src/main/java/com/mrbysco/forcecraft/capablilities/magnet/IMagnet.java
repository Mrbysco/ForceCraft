package com.mrbysco.forcecraft.capablilities.magnet;

public interface IMagnet {

    boolean isActivated();

    void activate();
    void deactivate();

    void setActivation(boolean value);
}
