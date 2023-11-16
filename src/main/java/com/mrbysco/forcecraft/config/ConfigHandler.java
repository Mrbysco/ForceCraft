package com.mrbysco.forcecraft.config;


import com.mrbysco.forcecraft.ForceCraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigHandler {
	public static class Common {
		public final BooleanValue generateForceOre;
		public final BooleanValue generateForceTree;
		public final BooleanValue timeTorchEnabled;
		public final BooleanValue timeTorchLogging;
		public final IntValue timeTorchRate;
		public final IntValue timeTorchAmount;
		public final IntValue baconatorMaxStacks;
		public final DoubleValue sturdyDamageReduction;
		public final DoubleValue forcePunchDamage;
		public final IntValue forceInfusingTime;
		public final ConfigValue<List<? extends String>> furnaceOutputBlacklist;
		public final ConfigValue<List<? extends String>> fortuneMessages;

		public final IntValue bleedCap;
		public final IntValue healingCap;
		public final IntValue sturdyToolCap;
		public final IntValue luckCap;
		public final IntValue damageCap;
		public final IntValue forceCap;
		public final IntValue speedCap;
		public final IntValue rodSpeedCap;

		public final BooleanValue ChuChuSpawning;
		public final IntValue ChuChuWeight;
		public final IntValue ChuChuMinGroup;
		public final IntValue ChuChuMaxGroup;

		public final BooleanValue CreeperTotSpawning;
		public final IntValue CreeperTotWeight;
		public final IntValue CreeperTotMinGroup;
		public final IntValue CreeperTotMaxGroup;

		public final BooleanValue EnderTotSpawning;
		public final IntValue EnderTotWeight;
		public final IntValue EnderTotMinGroup;
		public final IntValue EnderTotMaxGroup;

		public final BooleanValue FairySpawning;
		public final IntValue FairyWeight;
		public final IntValue FairyMinGroup;
		public final IntValue FairyMaxGroup;


		Common(ForgeConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			generateForceOre = builder
					.comment("Enable Force Ore generation. [Default: true]")
					.define("generateForceOre", true);

			generateForceTree = builder
					.comment("Enable Force Tree generation. [Default: true]")
					.define("generateForceTree", true);

			timeTorchEnabled = builder
					.comment("Enable Time Torch. [Default: true]")
					.define("timeTorchEnabled", true);

			timeTorchLogging = builder
					.comment("Print in Log when Time Torch is placed and by who. [Default: true]")
					.define("timeTorchLogging", true);

			timeTorchRate = builder
					.comment("How many ticks in between ticking neighbour blocks [Default: 5]")
					.defineInRange("timeTorchRate", 5, 0, Integer.MAX_VALUE);

			timeTorchAmount = builder
					.comment("The amount of ticks the Time Torch applies to neighbour blocks [Default: 3]")
					.defineInRange("timeTorchAmount", 3, 1, Integer.MAX_VALUE);

			baconatorMaxStacks = builder
					.comment("The max amount of food stacks stored in the baconator [Default: 4]")
					.defineInRange("baconatorMaxStacks", 4, 1, Integer.MAX_VALUE);

			sturdyDamageReduction = builder
					.comment("The max amount of damage blocked when all armor pieces have the Sturdy infusion [Default: 0.75]")
					.defineInRange("sturdyDamageReduction", 0.75, 0.0, 1.0);

			forcePunchDamage = builder
					.comment("The amount of half hearts of damage given by Force Punch given by the Damage infusion on armor [Default: 0.75]")
					.defineInRange("forcePunchDamage", 2, 0.0, Double.MAX_VALUE);

			forceInfusingTime = builder
					.comment("The max amount of ticks it takes to charge a tool with Force [Default: 120 (6 seconds)]")
					.defineInRange("forceInfusingTime", 120, 20, Integer.MAX_VALUE);

			furnaceOutputBlacklist = builder
					.comment("An additional list of tile entities the Force Furnace can NOT insert into [Syntax: modid:tile_name]\n" +
							"Examples: \"minecraft:shulker_box\" would stop it inserting into shulkers")
					.defineListAllowEmpty(Collections.singletonList("furnaceOutputBlacklist"), () -> Collections.singletonList(""), o -> (o instanceof String));

			String[] fortunes = new String[]
					{
							"You aren't supposed to eat me.",
							"Beauty is in the eye of the tiger.",
							"Creeper!",
							"Which came first, the chicken or the chunk?",
							"Whatever happened to Doritos 3D?",
							"Diabetes, anyone?",
							"That wasn't a cookie!",
							"If a tree falls down in the woods, you have a special mod installed.",
							"The cake is a lie of omission.",
							"A wise man once said, \"Yes honey, it does make you look fat.\" He never said that again.",
							"Don't stare directly at the pixels.",
							"I know where you live.",
							"Your lucky numbers are 0, -7, and 437922904678137496.708162",
							"There is never enough redstone.",
							"What you seek is surrounded by blocks.",
							"Today is Tuesday, or is it Friday - I can never tell.",
							"In the event of a creeper explosion, your keyboard can double as a broken keyboard.",
							"I didn't do it.",
							"You are 5.6 grams heavier than you were 10 seconds ago.",
							"I dropped my cookie.",
							"...Saltpeter? Really?",
							"We're no strangers to love. You know the rules and so do I.",
							"Eat another cookie.",
							"I CAN believe it's not butter.",
							"Beware enderman with a short fuse...",
							"Remember to repair your tools.",
							"Every rose has its bounding box.",
							"Get out of my chest!",
							"Please recycle.",
							"Great, now you've spoiled your dinner!",
							"Welcome to Hoarders: Minecraft edition!",
							"Not all Blocks are created equal.",
							"Don't touch that!",
							"Always name your machinations.",
							"Look in your inventory, now back to me - this fortune is now diamonds!",
							"Who put that there?",
							"Winners never cheat and cheaters never win, unless the winners cheated, in which case the cheaters won.",
							"Hi Bob! What, they can't all be zingers.",
							"Have you ever thought to yourself, \"My, that's an awfully large open grave!\"",
							"Could you pick up my dry-cleaning?",
							"Never shower in a thunderstorm. It's less efficient than bathing indoors and you'll freak out your neighbors.",
							"It is said that everyone experiences hardships, but God must REALLY hate YOU.",
							"If you play a country song backwards, you waste about 4 minutes of your life listening to garbled nonsense.",
							"No, you can't make that jump.",
							"I know they're furry and cuddly, but trust me, they're evil incarnate.",
							"Do you Dew?",
							"I see the land, but where exactly are the tracts?",
							"Creepers were originally from Dr. Who.",
							"Don't bogart my nasal spray!",
							"You'll live.",
							"Don't make me come back there!",
							"I've burned everything that reminds me of you in a ritualistic bonfire.",
							"We will be an unstoppable force; to our enemies we bring only death, to our allies we always bring cake.",
							"Heavy is the head that eats the crayons.",
							"Beware skinwalkers.",
							"Don't fear the creeper.",
							"Goodness and love will always win!",
							"I told you not to eat that!",
							"Winner!",
							"Beware anyone without an eye-patch!",
							"Don't kill all the animals!",
							"It's a wonder you get anything done!",
							"You will find happiness with a new love: Minecraft's latest patch.",
							"Seriously, who is in charge of the song/cowbell ratio?",
							"It's not a bug, it's a \"feature.\"",
							"Do you really need this many cookies?",
							"NCC1701",
							"I wanted to be a newspaper.",
							"Thank you! It was really dark in there!",
							"Thank you for not eating me.",
							"Burn the door!",
							"That's the biggest flapjack I've ever seen!",
							"Please be kind, de-rind",
							"It's a secret to everybody.",
							"I AM ERROR.",
							"If all else fails use fire.",
							"Dig it! Dig it! Dig to the center of the earth!",
							"No! Not into the pit! It BURNS!",
							"Dawn of the First Day, 72 hours remain.",
							"Don't stare directly at the potato.",
							"The chicken is a double-agent.",
							"Good lord!",
							"What's all this junk?",
							"Creepers blow chunks.",
							"Equivalence is a lie.",
							"Body by Sycamore.",
							"I hear 'Innards of the Machine' is on tour.",
							"Do something else.",
							"The capital of The Ukraine is Kiev.",
							"DebugCookie4A73N82",
							"Point that somewhere else!",
							"Forking is strictly prohibited.",
							"Void where prohibited.",
							"This parrot is no more!",
							"He's dead, Jim.",
							"Leave me alone for a bit, okay?",
							"Don't you dare shift-click me!",
							"Me again.",
							"My summer home is a no-bake.",
							"We can still be friends.",
							"The night is young, but you are not.",
							"Keyboard cat has carpal tunnel.",
							"Pull lever, get key.",
							"Boats n' Hoes",
							"Never eat an entire chocolate bunny in one sitting.",
							"Push that button and die.",
							"That cookie was mostly spackle.",
							"Slime Chunkery.",
							"I hate Thaumic Slimes.",
							"Inertia is a property of mallard.",
							"I prefer cake.",
							"If you can read this, you're literate.",
							"Don't touch the sides!",
							"Crunchitize me Cap'n!",
							"Please head in an orderly fashion to the disintegration chamber.",
							"It's the deer's ears.",
							"Happy anniversary, Carol!",
							"Never startle a ninja.",
							"If the shoe fits, you probably own it.",
							"Cattle reproduce by budding.",
							"Has anyone seen my Fedora?",
							"I beg to defer.",
							"Everybody loops.",
							"Fortune is straight down",
							"Eekum Bokum.",
							"Churba wurt!",
							"Darwa jit!",
							"Success is often preceded by failure, then followed again by failure.",
							"Man with Steve skin receive no cake.",
							"It's all Melnics to me!",
							"\"Steve\" means \"lazy\" in Swedish.",
							"Don't attack the cute little endermen - you'll be sorry.",
							"I miss my sanity, but then again I never used it.",
							"So this is where germs are born!",
							"I hate mondays.",
							"My old nemesis: gravity.",
							"I'm upgrade fodder!",
							"You can do anything at Zombo.com.",
							"Remember to feed the cattle.",
							"TROGDOR was a man. I mean, he was a dragon-man... Maybe he was just a dragon.",
							"Charles in charge of our clicks left and right. Charles in charge of every block in sight.",
							"Charles was never really in charge.",
							"Remember to use every part of the chicken.",
							"I'm not responsible for this.",
							"Every 10 minutes another crappy fortune is written.",
							"How many licks does it take to get to the center of a Chuck Norris?",
							"Roundhouse-kick for the win!",
							"DO NOT feed the beast! (After midnight)",
							"Please stop doing that.",
							"I'm not a vending machine!",
							"Try Ubuntu.",
							"If you program for more than 20 hours straight, do not blink.",
							"Ceiling cat is watching you procrastinate.",
							"Get your hand off my thigh! You can have the leg.",
							"Protective eyewear is a must!",
							"It's all in the wristwatch.",
							"Endermen in the UCB!",
							"Endermen can move TNT. I'm serious.",
							"Aww, you got blood all over my new sword!",
							"The human pancreas can take only so many twinkies.",
							"Humus is something you eat when you want your innards to cry.",
							"By Mennen",
							"Put me back in!",
							"...I got nothin'.",
							"We're out of milk.",
							"Always edit-out the derps.",
							"I want a lawyer.",
							"Bring me a shrubbery!",
							"It's bigger on the inside.",
							"That's what she said.",
							"Have you heard the one about the Rabbi and the Priest? Oh I forgot, you're deaf.",
							"There are worse appendages to get caught sticking inside the cookie jar.",
							"Ever have the feeling you're being watched? That's me!",
							"He who handles his NullPointerExceptions is a wise man indeed.",
							"Taking candy from a baby often prevents choking.",
							"Texting while driving is a potent form of natural-selection.",
							"The secret to a good marriage is matching tattoos.",
							"A sucker is born every minute, however an idiot is born every two seconds.",
							"Error in Thread Main: ExceptionNotCaughtException: ForceCraft.java:32",
							"I'll tear YOUR stub!",
							"Daydreaming is free, cookies are not.",
							"PINGAS!",
							"The run is dead.",
							"You do not aim Yo-Yos at people. They should face the ground.",
							"A Yo-Yo isn't a tool for hurting people...",
							"Happy Birthday Darkosto",
							"Bye Felicia!",
					};

			builder.pop();
			builder.comment("Fortune messages")
					.push("fortunes");

			fortuneMessages = builder
					.comment("Adding lines / removing lines specifies what the fortunes can say")
					.defineList("fortuneMessages", Arrays.asList(fortunes), o -> (o instanceof String));

			builder.pop();
			builder.comment("Infusion settings")
					.push("infusion");

			bleedCap = builder
					.comment("The max level of Bleeding that can be applied [Default: 3]")
					.defineInRange("bleedCap", 3, 1, 5);

			healingCap = builder
					.comment("The max level of Healing that can be applied [Default: 3]")
					.defineInRange("healingCap", 3, 1, 5);

			sturdyToolCap = builder
					.comment("The max level of Sturdy that can be applied on force tools [Default: 3]")
					.defineInRange("sturdyToolCap", 3, 1, 10);

			luckCap = builder
					.comment("The max level of Luck that can be applied [Default: 4]")
					.defineInRange("luckCap", 4, 1, 10);

			damageCap = builder
					.comment("The max level of Damage that can be applied [Default: 5]")
					.defineInRange("damageCap", 5, 1, 10);

			forceCap = builder
					.comment("The max level of Force that can be applied [Default: 2]")
					.defineInRange("forceCap", 2, 1, 10);

			speedCap = builder
					.comment("The max level of Speed that can be applied [Default: 5]")
					.defineInRange("speedCap", 5, 1, 10);

			rodSpeedCap = builder
					.comment("The max level of Speed that can be applied to a Force Rod [Default: 3]")
					.defineInRange("rodSpeedCap", 3, 1, 5);

			builder.pop();
			builder.comment("Mob settings")
					.push("mob");

			ChuChuSpawning = builder
					.comment("Enable Chu Chu's to spawn [Default: true]")
					.define("ChuChuSpawning", true);

			ChuChuWeight = builder
					.comment("Chu Chu spawn weight [Default: 100]")
					.defineInRange("ChuChuWeight", 100, 1, Integer.MAX_VALUE);

			ChuChuMinGroup = builder
					.comment("Chu Chu Min Group size [Default: 1]")
					.defineInRange("ChuChuMinGroup", 1, 1, Integer.MAX_VALUE);

			ChuChuMaxGroup = builder
					.comment("Chu Chu Max Group size [Default: 1]")
					.defineInRange("ChuChuMaxGroup", 1, 1, Integer.MAX_VALUE);

			CreeperTotSpawning = builder
					.comment("Enable Creeper Tot's to spawn [Default: true]")
					.define("CreeperTotSpawning", true);

			CreeperTotWeight = builder
					.comment("Creeper Tot spawn weight [Default: 25]")
					.defineInRange("CreeperTotWeight", 25, 1, Integer.MAX_VALUE);

			CreeperTotMinGroup = builder
					.comment("Creeper Tot Min Group size [Default: 1]")
					.defineInRange("CreeperTotMinGroup", 1, 1, Integer.MAX_VALUE);

			CreeperTotMaxGroup = builder
					.comment("Creeper Tot Max Group size [Default: 1]")
					.defineInRange("CreeperTotMaxGroup", 2, 1, Integer.MAX_VALUE);

			EnderTotSpawning = builder
					.comment("Enable Ender Tot's to spawn [Default: true]")
					.define("CreeperTotSpawning", true);

			EnderTotWeight = builder
					.comment("Ender Tot spawn weight [Default: 5]")
					.defineInRange("CreeperTotWeight", 5, 1, Integer.MAX_VALUE);

			EnderTotMinGroup = builder
					.comment("Ender Tot Min Group size [Default: 1]")
					.defineInRange("CreeperTotMinGroup", 1, 1, Integer.MAX_VALUE);

			EnderTotMaxGroup = builder
					.comment("Ender Tot Max Group size [Default: 1]")
					.defineInRange("CreeperTotMaxGroup", 1, 1, Integer.MAX_VALUE);

			FairySpawning = builder
					.comment("Enable Fairy's to spawn [Default: true]")
					.define("FairySpawning", true);

			FairyWeight = builder
					.comment("Fairy spawn weight [Default: 4]")
					.defineInRange("FairyWeight", 4, 1, Integer.MAX_VALUE);

			FairyMinGroup = builder
					.comment("Fairy Min Group size [Default: 1]")
					.defineInRange("FairyMinGroup", 1, 1, Integer.MAX_VALUE);

			FairyMaxGroup = builder
					.comment("Fairy Max Group size [Default: 1]")
					.defineInRange("FairyMaxGroup", 2, 1, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}


	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		ForceCraft.LOGGER.debug("Loaded ForceCraft's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		ForceCraft.LOGGER.fatal("ForceCraft's config just got changed on the file system!");
	}
}


