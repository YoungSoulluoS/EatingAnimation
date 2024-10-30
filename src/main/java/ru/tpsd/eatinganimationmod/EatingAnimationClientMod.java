package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public class EatingAnimationClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Item item : Registries.ITEM) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("eat"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }
                if(livingEntity instanceof OtherClientPlayerEntity) {
                    if(itemStack.getMaxUseTime(livingEntity) > 16) {
                        return livingEntity.getActiveItem() != itemStack ? 0.0F : ((float)livingEntity.getItemUseTime() / (float)itemStack.getMaxUseTime(livingEntity)) % 1;
                    }
                    else {
                        return livingEntity.getActiveItem() != itemStack ? 0.0F : ((float)livingEntity.getItemUseTime() / 32.0f) % 0.5F;
                    }
                }
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / 30.0F;
            });

            ModelPredicateProviderRegistry.register(item, Identifier.of("eating"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }
                return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });

        }
        FabricLoader.getInstance().getModContainer("eatinganimationid").ifPresent(eatinganimation ->
                ResourceManagerHelper.registerBuiltinResourcePack(EatingAnimationClientMod.locate("supporteatinganimation"), eatinganimation, ResourcePackActivationType.DEFAULT_ENABLED));
    }

    public static Identifier locate(String path) {
        return Identifier.ofVanilla(path);
    }
}
