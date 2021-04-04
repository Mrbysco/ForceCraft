package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import mrbysco.forcecraft.items.infuser.ForceToolData;
import mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceBowItem extends BowItem implements IForceChargingTool {
	public static final Predicate<ItemStack> FORCE_ARROWS = (stack) -> {
		return stack.getItem() instanceof ForceArrowItem;
	};

    public ForceBowItem(Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    	if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }
    
    // ShareTag for server->client capability data sync
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
    	CompoundNBT normal = super.getShareTag(stack);

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			if(normal == null) {
				normal = new CompoundNBT();
			}
			CompoundNBT newTag = ToolModStorage.serializeNBT(cap);
			normal.put(Reference.MOD_ID, newTag);
		}

        return normal;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			INBT shareTag = nbt.get(Reference.MOD_ID);
			ToolModStorage.deserializeNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModStorage.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}
	
	@Override
	public Predicate<ItemStack> getInventoryAmmoPredicate() {
		return FORCE_ARROWS;
	}
}
