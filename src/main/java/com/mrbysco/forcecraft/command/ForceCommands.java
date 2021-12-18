package com.mrbysco.forcecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForceCommands {

	@SubscribeEvent
	public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

		final LiteralArgumentBuilder<CommandSource> root = Commands.literal("force");
		root.requires((source) -> source.hasPermission(2))
				.then(Commands.literal("ishard").executes(this::execute))
				.then(Commands.literal("ishard").then(Commands.argument("tier", IntegerArgumentType.integer(0, 7)).executes(this::executeSpecific)));

		dispatcher.register(root);
	}

	private int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.FINAL;

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);

		player.addItem(book);

		return 0;
	}

	private int executeSpecific(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final int tier = IntegerArgumentType.getInteger(ctx, "tier");
		final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.values()[tier];

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);

		player.addItem(book);

		return 0;
	}
}
