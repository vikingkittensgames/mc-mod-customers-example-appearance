package com.example.customers.appearance.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import com.example.customers.appearance.CustomersAppearanceExample;

import com.vikingkittens.mc.customers.customer.CustomerVillagerEntity;

public final class BobberRenderer
        extends MobRenderer<
                CustomerVillagerEntity,
                BobberModel<CustomerVillagerEntity>> {
    private final ResourceLocation texture;

    public BobberRenderer(
            EntityRendererProvider.Context context,
            ModelLayerLocation modelLayer,
            BobberModel.Variant variant
    ) {
        super(
                context,
                new BobberModel<>(
                        context.bakeLayer(modelLayer),
                        variant
                ),
                0.5F
        );
        texture = ResourceLocation.fromNamespaceAndPath(
                CustomersAppearanceExample.MODID,
                variant.texturePath()
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            CustomerVillagerEntity entity
    ) {
        return texture;
    }
}
