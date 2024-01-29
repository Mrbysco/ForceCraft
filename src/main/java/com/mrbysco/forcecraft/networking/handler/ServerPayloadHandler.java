package com.mrbysco.forcecraft.networking.handler;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.attachment.storage.BeltStorage;
import com.mrbysco.forcecraft.attachment.storage.PackStorage;
import com.mrbysco.forcecraft.attachment.storage.StorageManager;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.ItemCardItem;
import com.mrbysco.forcecraft.menu.ForceBeltMenu;
import com.mrbysco.forcecraft.menu.ForcePackMenu;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import com.mrbysco.forcecraft.networking.message.OpenInventoryPayload;
import com.mrbysco.forcecraft.networking.message.PackChangePayload;
import com.mrbysco.forcecraft.networking.message.QuickUseBeltPayload;
import com.mrbysco.forcecraft.networking.message.RecipeToCardPayload;
import com.mrbysco.forcecraft.networking.message.SaveCardRecipePayload;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.FindingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;
import java.util.function.Predicate;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleOpen(final OpenInventoryPayload openData, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						if (openData.type() == 0) { //Belt
							Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
							if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
								ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);
								if (!beltStack.isEmpty()) {
									Optional<BeltStorage> data = StorageManager.getBelt(beltStack);
									data.ifPresent(belt ->
											player.openMenu(new SimpleMenuProvider((id, pInv, pEntity) -> new ForceBeltMenu(id, pInv, belt.getInventory()),
													beltStack.hasCustomHoverName() ? ((MutableComponent) beltStack.getHoverName()).withStyle(ChatFormatting.BLACK) : Component.translatable(Reference.MOD_ID + ".container.belt"))));
								}
							}
						} else { //Pack
							Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForcePackItem;
							if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
								ItemStack packStack = FindingUtil.findInstanceStack(player, stackPredicate);
								if (!packStack.isEmpty()) {
									Optional<PackStorage> data = StorageManager.getPack(packStack);
									data.ifPresent(pack ->
											player.openMenu(new SimpleMenuProvider((id, pInv, pEntity) -> new ForcePackMenu(id, pInv, pack.getInventory()),
													packStack.hasCustomHoverName() ? ((MutableComponent) packStack.getHoverName()).withStyle(ChatFormatting.BLACK) :
															Component.translatable(Reference.MOD_ID + ".container.pack")), buf -> buf.writeInt(pack.getInventory().getUpgrades())));
								}
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.open_inventory.failed", e.getMessage()));
					return null;
				});
	}

	public void handleQuickUse(final QuickUseBeltPayload beltData, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
						if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
							ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);

							if (!beltStack.isEmpty()) {
								Optional<BeltStorage> data = StorageManager.getBelt(beltStack);
								data.ifPresent(belt -> {
									IItemHandler handler = belt.getInventory();
									ItemStack stack = handler.getStackInSlot(beltData.slot());
									Level level = player.level();
									if (!stack.isEmpty()) {
										stack.finishUsingItem(level, player);
										level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), stack.getDrinkingSound(), player.getSoundSource(), 0.5F, player.level().random.nextFloat() * 0.1F + 0.9F);
									}
								});
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.quick_use_belt.failed", e.getMessage()));
					return null;
				});
	}

	public void handlePackChange(final PackChangePayload changeData, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						ItemStack stack = player.getItemInHand(changeData.hand());

						if (stack.getItem() == ForceRegistry.FORCE_PACK.get() || stack.getItem() == ForceRegistry.FORCE_BELT.get()) {
							CompoundTag tag = stack.getOrCreateTag();
							tag.putInt("Color", changeData.color());
							stack.setTag(tag);

							String customName = changeData.customName();
							if (customName.isEmpty()) {
								stack.resetHoverName();
							} else {
								if (!stack.getHoverName().getString().equals(customName)) {
									stack.setHoverName(Component.literal(customName).withStyle(ChatFormatting.YELLOW));
								}
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.pack_change.failed", e.getMessage()));
					return null;
				});
	}

	public void handleCard(final RecipeToCardPayload recipeData, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						// Handle tablet version
						ItemStack mainhand = ItemStack.EMPTY;
						if (player.getMainHandItem().getItem() instanceof ItemCardItem) {
							mainhand = player.getMainHandItem();
						} else if (player.getOffhandItem().getItem() instanceof ItemCardItem) {
							mainhand = player.getOffhandItem();
						}
						if (!mainhand.isEmpty() && mainhand.getItem() == ForceRegistry.ITEM_CARD.get()) {
							if (player.containerMenu instanceof ItemCardMenu itemCardContainer) {
								itemCardContainer.setMatrixContents(player, recipeData.stacks());
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.recipe_to_card.failed", e.getMessage()));
					return null;
				});
	}

	public void handleSaveCard(final SaveCardRecipePayload saveData, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent() && context.player().get() instanceof ServerPlayer player) {
						// Handle tablet version
						Level level = player.level();
						ItemStack stack = getCardStack(player);
						if (!stack.isEmpty()) {
							if (player.containerMenu instanceof ItemCardMenu itemCardContainer) {
								CraftingContainer craftMatrix = itemCardContainer.getCraftMatrix();
								ResultContainer craftResult = itemCardContainer.getCraftResult();
								Optional<RecipeHolder<CraftingRecipe>> iRecipe = player.server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftMatrix, level);
								iRecipe.ifPresent((holder) -> {
									CompoundTag tag = stack.getOrCreateTag();
									CompoundTag recipeContents = new CompoundTag();
									for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
										recipeContents.put("slot_" + i, craftMatrix.getItem(i).save(new CompoundTag()));
									}
									recipeContents.put("result", craftResult.getItem(0).save(new CompoundTag()));
									tag.put("RecipeContents", recipeContents);
									stack.setTag(tag);
								});
							}
						}
						player.sendSystemMessage(Component.literal("Recipe saved").withStyle(ChatFormatting.YELLOW));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("forcecraft.networking.save_card.failed", e.getMessage()));
					return null;
				});
	}

	private static ItemStack getCardStack(Player player) {
		if (player.getMainHandItem().getItem() instanceof ItemCardItem) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof ItemCardItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}
}
