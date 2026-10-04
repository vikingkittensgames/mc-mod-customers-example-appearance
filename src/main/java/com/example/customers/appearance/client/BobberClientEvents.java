package com.example.customers.appearance.client;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.example.customers.appearance.BobberAppearance;
import com.example.customers.appearance.CustomersAppearanceExample;

import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearances;

@EventBusSubscriber(
        modid = CustomersAppearanceExample.MODID,
        value = Dist.CLIENT
)
public final class BobberClientEvents {
    private static final Map<
                    BobberModel.Variant,
                    ModelLayerLocation>
            MODEL_LAYERS = createModelLayers();

    private BobberClientEvents() {}

    @SubscribeEvent
    public static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event
    ) {
        MODEL_LAYERS.forEach(
                (variant, modelLayer) ->
                        event.registerLayerDefinition(
                                modelLayer,
                                () -> BobberModel.createBodyLayer(
                                        variant
                                )
                        )
        );
    }

    @SubscribeEvent
    public static void registerAppearance(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        CustomersVillagerClientAppearances.register(
                BobberAppearance.ID,
                context -> new BobberClientAppearance(
                        context,
                        MODEL_LAYERS
                )
        );
    }

    private static Map<
                    BobberModel.Variant,
                    ModelLayerLocation>
            createModelLayers() {
        Map<BobberModel.Variant, ModelLayerLocation> layers =
                new EnumMap<>(BobberModel.Variant.class);
        for (BobberModel.Variant variant :
                BobberModel.Variant.values()) {
            layers.put(
                    variant,
                    new ModelLayerLocation(
                            ResourceLocation.fromNamespaceAndPath(
                                    CustomersAppearanceExample.MODID,
                                    "bobber_"
                                            + variant.name()
                                                    .toLowerCase()
                            ),
                            "main"
                    )
            );
        }
        return Map.copyOf(layers);
    }
}
