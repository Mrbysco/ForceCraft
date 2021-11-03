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
            LootPool.Builder builder = LootPool.builder();
            builder.addEntry(ItemLootEntry.builder(ForceRegistry.CLAW.get())
                    .weight(1)
                    .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
                    .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                    .name("forcecraft_inject");
            builder.addEntry(EmptyLootEntry.func_216167_a()
                    .weight(1)
                    .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))))
                    .name("forcecraft_empty_roll");
            LootPool pool = builder.build();

            event.getTable().addPool(pool);
        }
    }
}
