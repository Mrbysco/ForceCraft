package mrbysco.forcecraft.capablilities.toolmodifier;

public interface IToolModifier {
    /**
     * Modifier: Speed
     * Item: Sugar
     * Levels: 5 on tools, 1 on armor, 3 on Force Rod
     * Effect: Gives Player Haste [Level] when holding the tool
     */
    int getSpeedLevel();
    void incrementSpeed();
    void setSpeed(int newSpeed);

    /**
     * Modifier Heat
     * Item: Golden Power Source
     * Levels: 1
     * Effect: Auto-Smelt Item drops
     */
    boolean hasHeat();
    void setHeat(boolean val);

    /**
     * Modifier: Force
     * Item: Force Nugget
     * Levels: 2
     * Effect: Gives the Sword Knockback
     */
    int getForceLevel();
    boolean hasForce();
    void incrementForce();
    void setForce(int newForce);

    /**
     * Modifier Silk
     * Item: Web
     * Levels: 1
     * Effect: Give Pick/Shovel/Axe Silk Touch
     */
    boolean hasSilk();
    void setSilk(boolean val);

    /**
     * Modifier: Sharpness
     * Item: Claw
     * Levels: 5
     * Effect: Adds Sharpness to Force Sword
     */
    int getSharpLevel();
    boolean hasSharp();
    void incrementSharp();
    void setSharp(int newSharp);

    /**
     * Modifier: Luck
     * Item: Fortune
     * Levels: 4
     * Effect: Adds Fortune to a tool or Looting to a sword
     */
    int getLuckLevel();
    boolean hasLuck();
    void incrementLuck();
    void setLuck(int newLuck);

    /**
     * Modifier: Sturdy
     * Item: Bricks/Obsidian
     * Levels: 3 on tools, 1 per armor
     * Effect: Adds 1 Level of Unbreaking to tool up to 3
     */
    int getSturdyLevel();
    boolean hasSturdy();
    void incrementSturdy();
    void setSturdy(int newSturdy);

    /**
     * Modifier: Rainbow
     * Items: Lapis Lazuli
     * Levels: 1
     * Effect: Makes sheep drop a random amount of colored wool
     */
    boolean hasRainbow();
    void setRainbow(boolean val);

    /**
     * Modifier: Lumberjack
     * Items: Force Log
     * Levels: 1
     * Effect: Allows an axe to chop an entire tree down
     */
    boolean hasLumberjack();
    void setLumberjack(boolean val);

    /**
     * Modifier: Bleeding
     * Items: Arrow
     * Levels: 3
     * Effect: Applies Bleeding Potion Effect
     */

    int getBleedLevel();
    boolean hasBleed();
    void incrementBleed();
    void setBleed(int newBleed);

    /**
     * Modifier: Bane
     * Items: Spider Eye
     * Levels: 1
     * Effect: Applies Bane Potion Effect
     */

    int getBaneLevel();
    boolean hasBane();
    void incrementBane();
    void setBane(int newBane);

    /**
     * Modifier: Wing
     * Items: Feathers
     * Levels: 1
     * Effect: If full armor set is equipped, player can fly
     */

    boolean hasWing();
    void setWing(boolean val);

    /**
     * Modifier: Camo
     * Items: Invisibility Potion
     * Levels: 1
     * Effect: Gives Invisibility to wearer/user
     */

    boolean hasCamo();
    void setCamo(boolean val);

    /**
     * Modifier: Sight
     * Items: Night Vision Potion
     * Levels: 1
     * Effect: Gives Night Vision
     */

    boolean hasSight();
    void setSight(boolean val);

    /**
     * Modifier: Light
     * Items: Glowstone Dust
     * Levels: 1
     * Effect: Shows mobs through walls
     */

    boolean hasLight();
    void setLight(boolean val);

    /**
     * Modifier: Ender
     * Items: Ender Pearl / Eye of Ender
     * Levels: 1
     * Effect: Teleports target to random location
     */

    boolean hasEnder();
    void setEnder(boolean val);

    /**
     * Modifier: Freezing
     * Items: Snow Cookie
     * Levels: 1
     * Effect: Gives Slowness to enemy
     */

    boolean hasFreezing();
    void setFreezing(boolean val);


    /**
     * Modifier: Treasure
     * Items: Treasure Core
     * Levels: 1
     * Effect: Allows treasure cards to drop upon killing mobs
     */

    boolean hasTreasure();
    void setTreasure(boolean val);
}
