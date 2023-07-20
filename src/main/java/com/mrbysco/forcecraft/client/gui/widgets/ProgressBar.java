package com.mrbysco.forcecraft.client.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ProgressBar {

	private ResourceLocation texture;
	private ProgressBarDirection direction;
	private int positionX, positionY;
	private int width, height;
	private int textureX, textureY;

	private float min, max = 0;


	/**
	 * @param texture   Texture of the Bar
	 * @param direction Direction for the Bar to move
	 * @param width     Width of Progress Bar
	 * @param height    Height of Progress Bar
	 * @param posX      X Position
	 * @param posY      Y Position
	 * @param textureX  X Position of Texture
	 * @param textureY  Y Position of Texture
	 */
	public ProgressBar(ResourceLocation texture, ProgressBarDirection direction, int width, int height, int posX, int posY, int textureX, int textureY) {
		this.texture = texture;
		this.direction = direction;
		this.width = width;
		this.height = height;
		this.positionX = posX;
		this.positionY = posY;
		this.textureX = textureX;
		this.textureY = textureY;
	}

	/**
	 * @param min Minimum value (Default: 0)
	 * @return Edited instance
	 */
	public ProgressBar setMin(int min) {
		this.min = min;
		return this;
	}

	public ProgressBar setMax(int max) {
		this.max = max;
		return this;
	}

	private int getAdjustedWidth() {
		return (int) (min != 0 && max != 0 ? min / max * width : 0);
	}

	private int getAdjustedHeight() {
		return (int) (min != 0 && max != 0 ? min / max * height : 0);
	}

	public void draw(GuiGraphics guiGraphics) {
		switch (direction) {
			case DIAGONAL_UP_LEFT -> {
				guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height);
				guiGraphics.blit(texture, positionX, positionY, positionX, positionY, width - getAdjustedWidth(), height - getAdjustedHeight());
			}
			case DIAGONAL_UP_RIGHT -> {
				guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height);
				guiGraphics.blit(texture, positionX + getAdjustedWidth(), positionY, positionX + getAdjustedWidth(), positionY, width - getAdjustedWidth(), height - getAdjustedHeight());
			}
			case DIAGONAL_DOWN_LEFT -> {
				guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height);
				guiGraphics.blit(texture, positionX, positionY + getAdjustedHeight(), positionX, positionY + getAdjustedHeight(), width - getAdjustedWidth(), height - getAdjustedHeight());
			}
			case DIAGONAL_DOWN_RIGHT ->
					guiGraphics.blit(texture, positionX, positionY, textureX, textureY, getAdjustedWidth(), getAdjustedHeight());
			case DOWN_TO_UP -> //the only one that gets used
					guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height - getAdjustedHeight());
			case LEFT_TO_RIGHT ->
					guiGraphics.blit(texture, positionX, positionY, textureX, textureY, getAdjustedWidth(), height);
			case RIGHT_TO_LEFT -> {
				guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height);
				guiGraphics.blit(texture, positionX, positionY, positionX, positionY, width - getAdjustedWidth(), height);
			}
			case UP_TO_DOWN ->
					guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, getAdjustedHeight());
			default -> guiGraphics.blit(texture, positionX, positionY, textureX, textureY, width, height);
		}
	}


	public enum ProgressBarDirection {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT, UP_TO_DOWN, DOWN_TO_UP, DIAGONAL_UP_RIGHT, DIAGONAL_UP_LEFT, DIAGONAL_DOWN_RIGHT, DIAGONAL_DOWN_LEFT;
	}
}
