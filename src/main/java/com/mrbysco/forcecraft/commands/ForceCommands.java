package com.mrbysco.forcecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@SuppressWarnings("SameReturnValue")
@EventBusSubscriber
public class ForceCommands {
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("forcecraft");
		root.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("ishard").executes(ForceCommands::execute)
						.then(Commands.argument("tier", IntegerArgumentType.integer(1, 7))
								.executes(ForceCommands::executeSpecific)
						)
				);

		dispatcher.register(root);
	}


	private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final ServerPlayer player = ctx.getSource().getPlayerOrException();

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bookData = UpgradeBookData.DEFAULT.withTier(UpgradeBookTier.SEVEN);
		book.set(ForceComponents.UPGRADE_BOOK, bookData);

		player.addItem(book);

		return 0;
	}

	private static int executeSpecific(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final int tier = IntegerArgumentType.getInteger(ctx, "tier");
		final ServerPlayer player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.values()[tier - 1];

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bookData = UpgradeBookData.DEFAULT.withTier(bookTier);
		book.set(ForceComponents.UPGRADE_BOOK, bookData);

		player.addItem(book);

		return 0;
	}
}
