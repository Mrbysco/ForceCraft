package com.mrbysco.forcecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@EventBusSubscriber
public class ForceCommands {
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("forcecraft");
		root.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("ishard").executes((ctx) -> ForceCommands.isHard(ctx, 7))
						.then(Commands.argument("tier", IntegerArgumentType.integer(1, 7))
								.executes((ctx) -> ForceCommands.isHard(ctx, IntegerArgumentType.getInteger(ctx, "tier")))
						)
				);

		dispatcher.register(root);
	}


	private static int isHard(CommandContext<CommandSourceStack> ctx, int tier) throws CommandSyntaxException {
		final ServerPlayer player = ctx.getSource().getPlayerOrException();

		UpgradeBookTier bookTier = UpgradeBookTier.values()[tier - 1];

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bookData = completeBook(ctx.getSource(), bookTier);
		book.set(ForceComponents.UPGRADE_BOOK, bookData);

		player.addItem(book);

		return 0;
	}

	private static UpgradeBookData completeBook(CommandSourceStack source, UpgradeBookTier tier) {
		List<Identifier> used = new ArrayList<>();
		Collection<RecipeHolder<InfuseRecipe>> infuseRecipes = source.getServer().getRecipeManager().recipeMap().byType(ForceRecipes.INFUSE.get());
		for (RecipeHolder<InfuseRecipe> holder : infuseRecipes) {
			InfuseRecipe recipe = holder.value();
			if (recipe.getTier() != tier) continue;

			Identifier id = holder.id().identifier();
			String group = recipe.group();
			Identifier usedId = group.isEmpty() ? id : id.withPath(group);
			if (!used.contains(usedId)) {
				used.add(usedId);
			}
		}
		return new UpgradeBookData(tier, used, 0);
	}

}
