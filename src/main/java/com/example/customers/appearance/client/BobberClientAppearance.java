package com.example.customers.appearance.client;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearance;

public final class BobberClientAppearance
        implements CustomersVillagerClientAppearance {
    private final Map<BobberModel.Variant, BobberRenderer>
            renderers = new EnumMap<>(BobberModel.Variant.class);

    public BobberClientAppearance(
            EntityRendererProvider.Context context,
            Map<BobberModel.Variant, ModelLayerLocation> modelLayers
    ) {
        for (BobberModel.Variant variant :
                BobberModel.Variant.values()) {
            renderers.put(
                    variant,
                    new BobberRenderer(
                            context,
                            modelLayers.get(variant),
                            variant
                    )
            );
        }
    }

    @Override
    public MobRenderer<?, ?> getRenderer(
            CustomersVillager villager
    ) {
        return renderers.get(BobberModel.Variant.fromType(
                villager.getCustomersVillagerType()
        ));
    }
}
