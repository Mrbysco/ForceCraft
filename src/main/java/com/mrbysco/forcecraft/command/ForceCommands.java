package com.mrbysco.forcecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ForceCommands {

	@SubscribeEvent
	public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("force");
		root.requires((source) -> source.hasPermission(2))
				.then(Commands.literal("ishard").executes(this::execute))
				.then(Commands.literal("ishard").then(Commands.argument("tier", IntegerArgumentType.integer(0, 7)).executes(this::executeSpecific)));

		dispatcher.register(root);
	}

	private int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerPlayer player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.FINAL;

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);

		player.addItem(book);

		return 0;
	}

	private int executeSpecific(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final int tier = IntegerArgumentType.getInteger(ctx, "tier");
		final ServerPlayer player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.values()[tier];

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);

		player.addItem(book);

		return 0;
	}
}
