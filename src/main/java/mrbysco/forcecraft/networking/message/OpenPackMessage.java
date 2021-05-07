package mrbysco.forcecraft.networking.message;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForcePackContainer;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OpenPackMessage {
	public OpenPackMessage(){

	}

	public void encode(PacketBuffer buf) {

	}

	public static OpenPackMessage decode(final PacketBuffer packetBuffer) {
		return new OpenPackMessage();
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity player = ctx.getSender();
				Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForcePackItem;
				if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
					ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);
					if(!beltStack.isEmpty()) {
						player.openContainer(new INamedContainerProvider() {
							@Override
							public ITextComponent getDisplayName() {
								return beltStack.hasDisplayName() ? ((TextComponent)beltStack.getDisplayName()).mergeStyle(TextFormatting.BLACK) : new TranslationTextComponent(Reference.MOD_ID + ".container.pack");
							}

							@Nullable
							@Override
							public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
								return new ForcePackContainer(id, inventory);
							}
						});
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
