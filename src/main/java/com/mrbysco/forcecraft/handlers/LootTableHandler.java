package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {
    public static final ResourceLocation BAT = new ResourceLocation("minecraft", "entities/bat");
    // To test use: /summon bat ~ ~ ~ {NoAI:1b}
    @SubscribeEvent
    public void onLootTableLoadEvent(LootTableLoadEvent event) {
        if(event.getName().equals(BAT)) {
            LootPool.Builder builder = LootPool.lootPool();
            builder.add(LootItem.lootTableItem(ForceRegistry.CLAW.get())
                    .setWeight(1)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                    .name("forcecraft_inject");
            builder.add(EmptyLootItem.emptyItem()
                    .setWeight(1))
                    .name("forcecraft_empty_roll");
            LootPool pool = builder.build();

            event.getTable().addPool(pool);
        }
    }
}
