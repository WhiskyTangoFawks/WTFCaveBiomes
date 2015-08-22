package cavebiomes.worldgeneration.cavetypes.shallow;

import java.util.Random;
import wtfcore.utilities.BlockSets;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cavebiomes.worldgeneration.CaveType;
import cavebiomes.worldgeneration.DungeonType;

public class CaveTypeJungleVolcano extends CaveType{

	public CaveTypeJungleVolcano(String name, int cavedepth, DungeonType[] dungeonlist) {
		super(name, cavedepth, dungeonlist);
	}
	BlockSets.Modifier[] stonemodifier = {BlockSets.Modifier.stoneMagmaCrust, BlockSets.Modifier.cobblestone};

	@Override
	public void generateCeilingAddons(World world, Random random, int x, int y, int z)
	{
		gen.genStalactite(world, x, y, z, depth);
	}

	@Override
	public void generateFloor(World world, Random random, int x, int y, int z)
	{
		int height = MathHelper.abs_int((MathHelper.abs_int(x/2) % 10)/2 -5)+MathHelper.abs_int((MathHelper.abs_int(z) % 10) -5) + random.nextInt(3)-6;

		if (height > 2)
		{
			if (random.nextBoolean()){
				gen.replaceBlock(world, x, y, z, Blocks.grass, 0);
			}
			else {
				gen.replaceBlock(world, x, y, z, Blocks.grass, 0);
				gen.setBlockWithoutNotify(world, x, y+1, z, Blocks.tallgrass, 2);
			}
		}
		else {
			if (height < -1){
				if (IsBlockSurrounded(world, x, y, z)){
					gen.setFluid(world, x, y, z, Blocks.lava);
				}

			}
			else if (random.nextBoolean()){
				gen.transformBlock(world, x, y, z, stonemodifier[random.nextInt(stonemodifier.length)]);
			}

			else {
				if (this.shouldGenFloorAddon()){
					gen.genStalagmite(world, x, y, z, depth);

				}
			}
		}
	}


	@Override
	public void generateCeiling(World world, Random random, int x, int y, int z)
	{
		if (random.nextBoolean()){
			gen.GenVines(world, x, y-1, z);
		}

	}



}
