package cavebiomes.worldgeneration.dungeontypes;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import wtfcore.utilities.BlockSets;
import cavebiomes.worldgeneration.DungeonType;

public class DungeonTypeRain extends DungeonType{


		public DungeonTypeRain(String name) {
		super(name);
	}

		int xMin = -7;
		int xMax = 7;
		int yMin = -5;
		int yMax = -5;
		int zMin = -7;
		int zMax = 7;

		BlockSets.Modifier[] swampBlock = {BlockSets.Modifier.mossy_cobblestone, BlockSets.Modifier.cobblestone, BlockSets.Modifier.MossyStone};

		@Override
		public void generateCeiling(World world, Random random, int x, int y, int z)
		{
			gen.transformBlock(world, x, y, z, BlockSets.Modifier.WaterRainStone);
		}

		@Override
		public void generateFloor(World world, Random random, int x, int y, int z)
		{
			gen.setFluid(world, x, y, z, Blocks.water, swampBlock[random.nextInt(swampBlock.length)]);
		}

		@Override
		public void generateWalls(World world, Random rand, int x, int y, int z){
			gen.transformBlock(world, x, y, z, swampBlock[random.nextInt(swampBlock.length)]);
		}


}
