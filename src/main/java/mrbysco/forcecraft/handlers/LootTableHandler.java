package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.loot.EmptyLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {
    public static final ResourceLocation BAT = new ResourceLocation("minecraft", "entities/bat");
    @SubscribeEvent
    public void onLootTableLoadEvent(LootTableLoadEvent event) {
        if(event.getName().equals(BAT)) {
            LootPool.Builder builder = LootPool.builder();
            builder.addEntry(ItemLootEntry.builder(ForceRegistry.CLAW.get())
                    .weight(1)
                    .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2))))
                    .name("forcecraft_inject");
            builder.addEntry(EmptyLootEntry.func_216167_a()
                    .weight(15)
                    .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2))))
                    .name("forcecraft_empty_roll");
            LootPool pool = builder.build();

            event.getTable().addPool(pool);
        }
    }
}
