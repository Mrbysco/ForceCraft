package com.mrbysco.forcecraft.attachment.magnet;

public interface IMagnet {

	boolean isActivated();

	void activate();

	void deactivate();

	void setActivation(boolean value);
}
