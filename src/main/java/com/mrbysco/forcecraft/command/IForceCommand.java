package com.mrbysco.forcecraft.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface IForceCommand {

	  public boolean needsOp();

	  public String getName();

	  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player);
}
