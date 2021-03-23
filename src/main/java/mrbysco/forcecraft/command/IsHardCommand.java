package mrbysco.forcecraft.command;

import java.util.List;

import com.mojang.brigadier.context.CommandContext;

import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

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
		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(UpgradeBookTier.FINAL);
		bd.write(book);
		
		player.dropItem(book, true);

		return 0;
	}

}
