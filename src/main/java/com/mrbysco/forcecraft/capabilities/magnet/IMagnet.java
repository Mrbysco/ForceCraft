package com.mrbysco.forcecraft.capabilities.magnet;

public interface IMagnet {

    boolean isActivated();

    void activate();
    void deactivate();

    void setActivation(boolean value);
}
