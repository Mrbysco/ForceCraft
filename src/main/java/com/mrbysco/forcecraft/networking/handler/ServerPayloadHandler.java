package com.mrbysco.forcecraft.networking.handler;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.card.RecipeContentsData;
import com.mrbysco.forcecraft.components.storage.BeltStorage;
import com.mrbysco.forcecraft.components.storage.PackStorage;
import com.mrbysco.forcecraft.components.storage.StorageManager;
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
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
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
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Optional;
import java.util.function.Predicate;

public class ServerPayloadHandler {
	public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

	public static ServerPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleOpen(final OpenInventoryPayload openData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						if (openData.inventoryType() == 0) { //Belt
							Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
							if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
								ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);
								if (!beltStack.isEmpty()) {
									Optional<BeltStorage> data = StorageManager.getBelt(beltStack);
									data.ifPresent(belt ->
											player.openMenu(new SimpleMenuProvider((id, pInv, pEntity) -> new ForceBeltMenu(id, pInv, belt.getInventory()),
													beltStack.has(DataComponents.CUSTOM_NAME) ? ((MutableComponent) beltStack.getHoverName()).withStyle(ChatFormatting.BLACK) : Component.translatable(Reference.MOD_ID + ".container.belt"))));
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
													packStack.has(DataComponents.CUSTOM_NAME) ? ((MutableComponent) packStack.getHoverName()).withStyle(ChatFormatting.BLACK) :
															Component.translatable(Reference.MOD_ID + ".container.pack")), buf -> buf.writeInt(pack.getInventory().getUpgrades())));
								}
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.open_inventory.failed", e.getMessage()));
					return null;
				});
	}

	public void handleQuickUse(final QuickUseBeltPayload beltData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						Predicate<ItemStack> stackPredicate = (stack) -> stack.getItem() instanceof ForceBeltItem;
						if (FindingUtil.hasSingleStackInHotbar(player, stackPredicate)) {
							ItemStack beltStack = FindingUtil.findInstanceStack(player, stackPredicate);

							if (!beltStack.isEmpty()) {
								Optional<BeltStorage> data = StorageManager.getBelt(beltStack);
								data.ifPresent(belt -> {
									ResourceHandler<ItemResource> handler = belt.getInventory();
									ItemResource resource = handler.getResource(beltData.slot());
									Level level = player.level();
									if (!resource.isEmpty()) {
										try (Transaction tx = Transaction.openRoot()) {
											if (handler.extract(resource, 1, tx) != 1) {
												ItemStack stack = resource.toStack();

												stack.finishUsingItem(level, player);
												if (stack.has(DataComponents.CONSUMABLE)) {
													level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
															stack.get(DataComponents.CONSUMABLE).sound().value(), player.getSoundSource(),
															0.5F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);

												}

												if (!stack.isEmpty()) {
													if (handler.insert(ItemResource.of(stack), 1, tx) != 1) return;
												}
												tx.commit();
											}
											;

										}
									}
								});
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.quick_use_belt.failed", e.getMessage()));
					return null;
				});
	}

	public void handlePackChange(final PackChangePayload changeData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						ItemStack stack = player.getItemInHand(changeData.hand());

						if (stack.getItem() == ForceRegistry.FORCE_PACK.get() || stack.getItem() == ForceRegistry.FORCE_BELT.get()) {
							stack.set(ForceComponents.PACK_COLOR, changeData.color());

							String customName = changeData.customName();
							if (customName.isEmpty()) {
								stack.remove(DataComponents.CUSTOM_NAME);
							} else {
								if (!stack.getHoverName().getString().equals(customName)) {
									stack.set(DataComponents.CUSTOM_NAME, Component.literal(customName).withStyle(ChatFormatting.YELLOW));
								}
							}
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.pack_change.failed", e.getMessage()));
					return null;
				});
	}

	public void handleCard(final RecipeToCardPayload recipeData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
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
					context.disconnect(Component.translatable("forcecraft.networking.recipe_to_card.failed", e.getMessage()));
					return null;
				});
	}

	public void handleSaveCard(final SaveCardRecipePayload saveData, final IPayloadContext context) {
		context.enqueueWork(() -> {
					if (context.player() instanceof ServerPlayer player) {
						// Handle tablet version
						Level level = player.level();
						ItemStack stack = getCardStack(player);
						if (!stack.isEmpty()) {
							if (player.containerMenu instanceof ItemCardMenu itemCardContainer) {
								CraftingContainer craftMatrix = itemCardContainer.getCraftMatrix();
								ResultContainer craftResult = itemCardContainer.getCraftResult();
								Optional<RecipeHolder<CraftingRecipe>> iRecipe = player.level().getServer().getRecipeManager()
										.getRecipeFor(RecipeType.CRAFTING, craftMatrix.asCraftInput(), level);
								iRecipe.ifPresent((holder) -> {
									CompoundTag recipeContents = new CompoundTag();
									if (craftMatrix.getContainerSize() > 9) {
										player.sendSystemMessage(Component.translatable("forcecraft.networking.save_card.too_large", "Crafting grid too large").withStyle(ChatFormatting.RED));
										return;
									}
									NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
									for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
										stacks.set(i, craftMatrix.getItem(i));
									}
									RecipeContentsData data = new RecipeContentsData(stacks, craftResult.getItem(0));
									stack.set(ForceComponents.RECIPE_CONTENTS, data);
								});
							}
						}
						player.sendSystemMessage(Component.literal("Recipe saved").withStyle(ChatFormatting.YELLOW));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("forcecraft.networking.save_card.failed", e.getMessage()));
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
