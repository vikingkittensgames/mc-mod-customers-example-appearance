package com.example.customers.appearance;

import net.neoforged.fml.common.Mod;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearanceRegistries;

@Mod(CustomersAppearanceExample.MODID)
public final class CustomersAppearanceExample {
    public static final String MODID = "customersappearanceexample";

    public CustomersAppearanceExample() {
        CustomersVillagerAppearanceRegistries.appearances().register(BobberAppearance.ID, BobberAppearance::new);
    }
}
