package lk.pixcapsoft.diamondscanner;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pixcapdiamondscanner implements ModInitializer {
    public static final String MOD_ID = "pixcapdiamondscanner";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Pixcap Diamond Scanner mod loaded!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("scandiamonds")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    BlockPos playerPos = source.getPlayer().getBlockPos();
                    World world = source.getWorld();

                    int radius = 32;
                    int found = 0;

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                BlockPos scanPos = playerPos.add(x, y, z);
                                if (world.getBlockState(scanPos).isOf(Blocks.DIAMOND_ORE) ||
                                    world.getBlockState(scanPos).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                                    source.sendMessage(Text.literal("💎 Found diamond at: " + scanPos.toShortString()));
                                    found++;
                                }
                            }
                        }
                    }

                    if (found == 0) {
                        source.sendMessage(Text.literal("No diamonds found nearby."));
                    } else {
                        source.sendMessage(Text.literal("Scan complete. Found " + found + " diamond ores."));
                    }

                    return 1;
                }));
        });
    }
}