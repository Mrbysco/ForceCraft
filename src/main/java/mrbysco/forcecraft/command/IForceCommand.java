package mrbysco.forcecraft.command;

import java.util.List;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public interface IForceCommand {

	  public boolean needsOp();

	  public String getName();

	  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player);
}
