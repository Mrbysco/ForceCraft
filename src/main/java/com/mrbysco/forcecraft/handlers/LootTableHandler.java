package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.loot.EmptyLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {
    public static final ResourceLocation BAT = new ResourceLocation("minecraft", "entities/bat");
    // To test use: /summon bat ~ ~ ~ {NoAI:1b}
    @SubscribeEvent
    public void onLootTableLoadEvent(LootTableLoadEvent event) {
        if(event.getName().equals(BAT)) {
            LootPool.Builder builder = LootPool.lootPool();
            builder.add(ItemLootEntry.lootTableItem(ForceRegistry.CLAW.get())
                    .setWeight(1)
                    .apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
                    .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))
                    .name("forcecraft_inject");
            builder.add(EmptyLootEntry.emptyItem()
                    .setWeight(1))
                    .name("forcecraft_empty_roll");
            LootPool pool = builder.build();

            event.getTable().addPool(pool);
        }
    }
}
