package mrbysco.forcecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mrbysco.forcecraft.ForceCraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ForceCommands {

	public static final Set<String> SUBCOMMANDS = new HashSet<>();
	public static final List<IForceCommand> COMMANDS = new ArrayList<>();

	@SubscribeEvent
	public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
		COMMANDS.add(new IsHardCommand());
		for (IForceCommand cmd : COMMANDS) {
			SUBCOMMANDS.add(cmd.getName());
		}

		CommandDispatcher<CommandSource> r = event.getDispatcher();
		r.register(LiteralArgumentBuilder.<CommandSource>literal("force").then(Commands.argument("arguments", StringArgumentType.greedyString()).executes(this::execute)).executes(this::execute));
	}

	private int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().asPlayer();
		List<String> arguments = Arrays.asList(ctx.getInput().split("\\s+"));
		if (arguments.size() < 2) {
			badCommandMsg(player);
			return 0;
		}
		String sub = arguments.get(1);
		// loop on all registered commands
		for (IForceCommand cmd : COMMANDS) {
			if (sub.equalsIgnoreCase(cmd.getName())) {
				// do i need op
				if (cmd.needsOp()) {
					// ok check me
					boolean isOp = ctx.getSource().hasPermissionLevel(1);
					if (!isOp) {
						// player needs op but does not have it
						// player.getDisplayName()
						ForceCraft.LOGGER.info("Player [" + player.getUniqueID() + "," + player.getDisplayName() + "] attempted /force command " + sub + " but does not have the required permissions");
						sendFeedback(ctx, "commands.forcecraft.failed");
						return 1;
					}
				}
				return cmd.execute(ctx, arguments.subList(2, arguments.size()), player);
			}
		}
		badCommandMsg(player);
		return 0;
	}

	public static void sendFeedback(CommandContext<CommandSource> ctx, String string) {
		ctx.getSource().sendFeedback(new TranslationTextComponent(string), false);
	}

	private void badCommandMsg(ServerPlayerEntity player) {

		player.sendMessage(new TranslationTextComponent("commands.forcecraft.null"), player.getUniqueID());
		player.sendMessage(new StringTextComponent( String.join(", ", SUBCOMMANDS)), player.getUniqueID());
	}

}
