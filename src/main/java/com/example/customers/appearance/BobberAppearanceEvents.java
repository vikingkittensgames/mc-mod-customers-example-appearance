package com.example.customers.appearance;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearance;

@EventBusSubscriber(modid = CustomersAppearanceExample.MODID)
public final class BobberAppearanceEvents {
    private BobberAppearanceEvents() {}

    @SubscribeEvent
    public static void registerAppearances(RegisterEvent event) {
        event.register(
                CustomersVillagerAppearance.APPEARANCE_REGISTRY_KEY,
                registry -> registry.register(
                        BobberAppearance.ID,
                        new BobberAppearance()
                )
        );
    }
}
