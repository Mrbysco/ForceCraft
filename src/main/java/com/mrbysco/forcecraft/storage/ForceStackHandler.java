package com.mrbysco.forcecraft.storage;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.util.ForceUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class ForceStackHandler extends ItemAccessItemHandler {
	private static final int PER_GEM = 250;

	public ForceStackHandler(ItemAccess itemAccess, ItemStack parentStack) {
		super(itemAccess, ForceComponents.FORCE_SLOT.get(), 1);
	}

	public IndexModifier<ItemResource> indexModifier(ItemStack stack) {
		return (index, resource, amount) -> {
			amount = updateStack(stack, index, resource, amount);
			ItemContainerContents result = update(ItemResource.of(stack), index, resource, amount).get(this.component);

			stack.set(this.component, result);
		};
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return resource.is(ForceTags.Items.GEMS_FORCE);
	}

	private int updateStack(ItemStack stack, int index, ItemResource newResource, int amount) {
		if (newResource.isEmpty() || !newResource.is(ForceTags.Items.GEMS_FORCE)) {
			return amount;
		}

		ItemContainerContents contents = stack.getOrDefault(this.component, ItemContainerContents.EMPTY);
		ItemStack oldStack = index < contents.getSlots() ? contents.getStackInSlot(index) : ItemStack.EMPTY;
		int oldAmount = (!oldStack.isEmpty() && oldStack.getItem() == newResource.getItem()) ? oldStack.getCount() : 0;

		int added = amount - oldAmount;
		if (added <= 0) {
			return amount;
		}

		int currentStored = stack.getOrDefault(ForceComponents.FORCE_STORED, 0);
		int possibleGems = Math.max(0, (ForceUtil.FORCE_MAX - currentStored) / PER_GEM);
		int converted = Math.min(added, possibleGems);

		if (converted > 0) {
			stack.set(ForceComponents.FORCE_STORED, currentStored + converted * PER_GEM);
		}

		return amount - converted;
	}

}
