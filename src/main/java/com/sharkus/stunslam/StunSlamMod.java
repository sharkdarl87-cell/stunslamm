package com.sharkus.stunslam;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class StunSlamMod implements ClientModInitializer {
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        StunSlamFeature.init();
        // Taste "K" schaltet Stun Slam ein/aus (in den Steuerungs-Einstellungen aenderbar)
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.stunslam.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K,
                KeyMapping.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (toggleKey.consumeClick()) {
                StunSlamFeature.enabled = !StunSlamFeature.enabled;
                if (mc.player != null) {
                    mc.player.displayClientMessage(Component.literal(
                            "Stun Slam: " + (StunSlamFeature.enabled ? "AN" : "AUS")), true);
                }
            }
        });
    }
}
