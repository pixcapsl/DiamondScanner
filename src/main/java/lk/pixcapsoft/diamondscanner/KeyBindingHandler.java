package lk.pixcapsoft.diamondscanner;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    public static KeyBinding scanKey;

    public static void register() {
        scanKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pixcapdiamondscanner.scan", // translation key
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G, // default key
            "category.pixcapdiamondscanner" // category in controls menu
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scanKey.wasPressed()) {
                scanForDiamonds(client);
            }
        });
    }

    private static void scanForDiamonds(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        BlockPos playerPos = client.player.getBlockPos();
        int radius = 32;
        int found = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos scanPos = playerPos.add(x, y, z);
                    if (client.world.getBlockState(scanPos).isOf(Blocks.DIAMOND_ORE) ||
                        client.world.getBlockState(scanPos).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                        client.player.sendMessage(Text.literal("💎 Found diamond at: " + scanPos.toShortString()), false);
                        found++;
                    }
                }
            }
        }

        if (found == 0) {
            client.player.sendMessage(Text.literal("No diamonds found nearby."), false);
        } else {
            client.player.sendMessage(Text.literal("Scan complete. Found " + found + " diamond ores."), false);
        }
    }
}