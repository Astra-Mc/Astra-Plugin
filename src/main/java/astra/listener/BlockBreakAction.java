package astra.listener;

import astra.block.BlockOreMagicGenCooldown;
import astra.block.BlockWheatGen;
import astra.block.BlockWheatGenCooldown;
import astra.mongodb.PlayerDB;
import astra.plugin;
import astra.block.BlockOreMagicGen;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.GameType;
import cn.nukkit.scheduler.Task;

import java.util.concurrent.ThreadLocalRandom;

public class BlockBreakAction implements Listener {

    private static final int BlockMagicGenID = new BlockOreMagicGen().getId(); // get custom block IDS at runtime (bcz they aren't consistent)
    private static final int BlockWheatGenID = new BlockWheatGen().getId();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock(); // get block from event
        Player player = event.getPlayer(); // get player from event
        if (block.getId() == BlockMagicGenID) {
            if (GameType.from(player.gamemode) == GameType.SURVIVAL) {
                player.getLevel().addSound(new Vector3(block.x, block.y, block.z), Sound.BREAK_AMETHYST_BLOCK);

                for (int i = 0; i < 15; i++) {

                    plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            player.getLevel().addParticleEffect(new Vector3f(
                                    (float) (block.x + ThreadLocalRandom.current().nextDouble(0.25, 0.75)),
                                    (float) (block.y + ThreadLocalRandom.current().nextDouble(0.25, 0.75)),
                                    (float) (block.z + ThreadLocalRandom.current().nextDouble(0.25, 0.75))
                            ), "astra:magic_ore_break", -1, player.getLevel().getDimension(), player);
                        }
                    }, i, true);
                }

                PlayerDB.setPlayerCoinsInDB(player, PlayerDB.getPlayerCoinsFromDB(player) + 1);

                plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {

                    @Override
                    public void onRun(int i) {
                        block.level.setBlock(new Vector3(block.x, block.y, block.z), new BlockOreMagicGenCooldown()); // run a scheduled task to replace the block with the cooldown block (Need of scheduled task bcz: can't replace block directly ~ it needs a little delay)
                    }

                }, 1);

                plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {

                    @Override
                    public void onRun(int i) {
                        block.level.setBlock(new Vector3(block.x, block.y, block.z), new BlockOreMagicGen()); // run a scheduled task to replace the block after time with the generator block again
                    }

                }, 500);

            }
        }
        else if (block.getId() == BlockWheatGenID) {
            if (GameType.from(player.gamemode) == GameType.SURVIVAL) {
                player.getLevel().addSound(new Vector3(block.x, block.y, block.z), Sound.DIG_GRASS);

                for (int i = 0; i < 15; i++) {

                    plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            player.getLevel().addParticleEffect(new Vector3f(
                                    (float) (block.x + ThreadLocalRandom.current().nextDouble(0.25, 0.75)),
                                    (float) (block.y + ThreadLocalRandom.current().nextDouble(0.25, 0.75)),
                                    (float) (block.z + ThreadLocalRandom.current().nextDouble(0.25, 0.75))
                            ), ParticleEffect.CROP_GROWTH.getIdentifier(),-1, player.getLevel().getDimension(), player);
                        }
                    }, i, true);
                }

                plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {

                    @Override
                    public void onRun(int i) {
                        block.level.setBlock(new Vector3(block.x, block.y, block.z), new BlockWheatGenCooldown()); // run a scheduled task to replace the block with the cooldown block (Need of scheduled task bcz: can't replace block directly ~ it needs a little delay)
                    }

                }, 1);

                plugin.getInstance().getServer().getScheduler().scheduleDelayedTask(new Task() {

                    @Override
                    public void onRun(int i) {
                        block.level.setBlock(new Vector3(block.x, block.y, block.z), new BlockWheatGen()); // run a scheduled task to replace the block after time with the generator block again
                    }

                }, 500);

            }
        }
    }
}