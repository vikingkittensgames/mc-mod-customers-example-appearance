package com.example.customers.appearance;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearance;
import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

public final class BobberAppearance
        implements CustomersVillagerAppearance {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    CustomersAppearanceExample.MODID,
                    "bobbers"
            );
    private static final SoundEvent AMBIENT_SOUND =
            sound("bobber_ambient");
    private static final SoundEvent HURT_SOUND =
            sound("bobber_hurt");
    private static final SoundEvent DEATH_SOUND =
            sound("bobber_death");
    private static final SoundEvent STEP_SOUND =
            sound("bobber_step");
    private static final SoundEvent YES_SOUND =
            sound("bobber_yes");
    private static final SoundEvent NO_SOUND =
            sound("bobber_no");

    @Override
    public Component getName() {
        return Component.translatable(
                "appearance.customersappearanceexample.bobbers"
        );
    }

    @Override
    public boolean isApplicable(CustomersVillager villager) {
        return villager.getCustomersVillagerType()
                != CustomersVillagerType.SUPPLIER;
    }

    @Override
    public SoundEvent getAmbientSound(
            CustomersVillager villager
    ) {
        return AMBIENT_SOUND;
    }

    @Override
    public SoundEvent getHurtSound(CustomersVillager villager) {
        return HURT_SOUND;
    }

    @Override
    public SoundEvent getDeathSound(CustomersVillager villager) {
        return DEATH_SOUND;
    }

    @Override
    public SoundEvent getStepSound(CustomersVillager villager) {
        return STEP_SOUND;
    }

    @Override
    public SoundEvent getYesSound(CustomersVillager villager) {
        return YES_SOUND;
    }

    @Override
    public SoundEvent getNoSound(CustomersVillager villager) {
        return NO_SOUND;
    }

    private static SoundEvent sound(String path) {
        return SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(
                        CustomersAppearanceExample.MODID,
                        path
                )
        );
    }
}
