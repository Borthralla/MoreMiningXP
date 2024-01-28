package com.borthralla.mixin;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(Block.class)
public abstract class MoreMiningXPMixin {

    private IntProvider diamond_xp_range = UniformIntProvider.create(40, 50);
    private IntProvider low_range = UniformIntProvider.create(8, 16);
    private IntProvider medium_range = UniformIntProvider.create(32, 40);

    private List<String> diamond_blocks = new ArrayList<>(Arrays.asList("diamond_ore", "deepslate_diamond_ore"));
    private List<String> low_range_blocks = new ArrayList<>(Arrays.asList("coal_ore", "deepslate_coal_ore", "iron_ore",
            "deepslate_iron_ore", "copper_ore", "deepslate_copper_ore"));
    private List<String> medium_range_blocks = new ArrayList<>(Arrays.asList("gold_ore", "deepslate_gold_ore",
            "lapis_ore", "deepslate_lapis_ore", "redstone_ore", "deepslate_redstone_ore"));

    private static final Logger LOGGER = LoggerFactory.getLogger("more_mining_xp");

    @ModifyVariable(method = "dropExperienceWhenMined", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private IntProvider modifyExperience(IntProvider experience) {
        Block block = (Block) (Object) this;
        Identifier block_type = Registries.BLOCK.getId(block);
        String block_type_string = block_type.getPath();
        LOGGER.info("Running dropExperienceWhenMined mixin for block " + block_type_string);
        if (diamond_blocks.contains(block_type_string)) {
            LOGGER.info("Diamond ore identified, using Diamond XP range");
            return diamond_xp_range;
        }
        if (low_range_blocks.contains(block_type_string)) {
            LOGGER.info("Low range ore identified, using low range");
            return low_range;
        }
        if (medium_range_blocks.contains(block_type_string)) {
            LOGGER.info("medium range ore identified, using medium range");
            return medium_range;
        }
        LOGGER.info("No special ore found");
        // Return the original experience provider for other blocks
        return experience;
    }
}