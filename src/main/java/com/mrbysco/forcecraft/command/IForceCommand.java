package com.mrbysco.forcecraft.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface IForceCommand {
	boolean needsOp();

	String getName();

	int execute(CommandContext<CommandSourceStack> ctx, List<String> arguments, Player player);

}
