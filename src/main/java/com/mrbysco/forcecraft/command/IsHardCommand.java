package com.mrbysco.forcecraft.command;

import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IsHardCommand implements IForceCommand {

	@Override
	public boolean needsOp() {
		return true;
	}

	@Override
	public String getName() {
		return "ishard";
	}

	@Override
	public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
		UpgradeBookTier bookTier;
		try {
			String arg = arguments.get(0);
			bookTier = UpgradeBookTier.values()[Integer.parseInt(arg)];
		} catch (Exception e) {
			bookTier = UpgradeBookTier.FINAL;
			//dont care, just use max level
		}

		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);

		player.addItem(book);

		return 0;
	}

}
