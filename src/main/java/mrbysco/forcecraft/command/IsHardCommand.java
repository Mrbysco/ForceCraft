package mrbysco.forcecraft.command;

import com.mojang.brigadier.context.CommandContext;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import mrbysco.forcecraft.registry.ForceRegistry;
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
		UpgradeBookTier bookTier = UpgradeBookTier.FINAL;
		try {
			bookTier = UpgradeBookTier.values()[Integer.parseInt(arguments.get(0))];
		}
		catch(Exception e) {
			//dont care, just use max level
		}
		
		ItemStack book = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		UpgradeBookData bd = new UpgradeBookData(book);
		bd.setTier(bookTier);
		bd.write(book);
		
		player.addItemStackToInventory(book);

		return 0;
	}

}
