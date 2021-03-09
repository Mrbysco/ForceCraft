package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.registry.ForceRegistry;
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
                    .quality(0)
                    .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2))));
            builder.bonusRolls(0, 1)
                    .name("forcecraft_inject");
            LootPool pool = builder.build();

            event.getTable().addPool(pool);
        }
    }
}
