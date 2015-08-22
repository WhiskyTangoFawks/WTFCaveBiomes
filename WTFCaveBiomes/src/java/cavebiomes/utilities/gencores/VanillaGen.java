package cavebiomes.utilities.gencores;

import java.util.Random;

import cavebiomes.blocks.BlockIcicle;
import cavebiomes.blocks.BlockSpeleothems;
import cavebiomes.blocks.CaveBlocks;
import wtfcore.utilities.BlockInfo;
import wtfcore.utilities.BlockSets;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;


public class VanillaGen {
	public VanillaGen() {
	}
	public Random random = new Random();

	public static VanillaGen getGenMethods(){
		if (Loader.isModLoaded("UndergroundBiomes")){
			return new UBCGen();
		}
		else {
			return new VanillaGen();
		}
	}

	/**
	 **Used to set a block, based on a modifier and the block at the given location
	 **/
	public void transformBlock(World world, int x, int y, int z, BlockSets.Modifier modifier){
		BlockInfo blockandmeta = getBlockToReplace(world, x, y, z);
		Block blockToSet = BlockSets.floorBlock.get(new BlockInfo(blockandmeta.block, blockandmeta.meta, modifier));
		if (blockToSet != null){
			setBlockWithoutNotify(world, x, y, z, blockToSet, blockandmeta.meta);
		}
	}
	/**
	 **Used to replace a block.  Checks that the block is replaceable first
	 **/
	public void replaceBlock(World world, int x, int y, int z, Block block, int metadata){
		if (BlockSets.ReplaceHashset.contains(world.getBlock(x, y, z))){
			setBlockWithoutNotify(world,x, y, z, block, metadata);
		}
	}
	/**
	 **Used to set a block at y+1, based on a modifier and the block at the given location.  e.g. cobblestone boulders
	 **/
	public void setStoneAddon(World world, int x, int y, int z, BlockSets.Modifier modifier){
		BlockInfo blockandmeta = getBlockToReplace(world, x, y, z);
		Block blockToSet = BlockSets.floorBlock.get(new BlockInfo(blockandmeta.block, blockandmeta.meta, modifier));
		if (blockToSet != null){
			setBlockWithoutNotify(world, x, y+1, z, blockToSet, blockandmeta.meta);
		}
		else {

		}
	}


	/**
	 **Used to set an ice patch above the given block, checks that it's a normal block first
	 **/
	public void freezeBlock(World world, int x, int y, int z){
		//Add a method to generate frozen fences, torches, and rails
		if (world.getBlock(x,y,z).isBlockNormalCube()){
			setBlockWithoutNotify(world, x, y+1, z, CaveBlocks.IcePatch, 0);
		}
	}

	/**
	 **Sets the block to the fluid
	 **/
	public void setFluid(World world, int x, int y, int z, Block fluid) {
		setFluid(world, x, y, z, fluid, null);
	}

	/**
	 **Sets the block to the fluid, and transforms the block below it
	 **/
	public void setFluid(World world, int x, int y, int z, Block fluid, BlockSets.Modifier modifier) {

		if (BlockSets.ReplaceHashset.contains(world.getBlock(x, y, z))){
			setBlockWithoutNotify(world,x, y, z, fluid, 0);
			world.scheduleBlockUpdate(x, y, z, fluid, random.nextInt(200)+100);

			if (modifier != null){
				transformBlock(world, x ,y-1, z, modifier);
			}
		}
	}

	/**
	 **used by the cave in dungeon type to get the stone type for UBC compatibility
	 **/
	public void genFloatingStone(World world, int x, int y, int z, Block modifier){
		setBlockWithoutNotify(world, x, y, z, modifier, 0);
	}


	public void genStalagmite(World world, int x, int y, int z, int depth){
		genStalagmite(world, x, y, z, depth, null);
	}

	public void genStalagmite(World world, int x, int y, int z, int depth, Block modifier){

		float size = random.nextFloat();

		switch (depth){
		case 1 :
			if (size < 0.95F){ genStalagmite(world, x, y, z, modifier, 1);}
			else { genStalagmite(world, x, y, z, modifier, 2);}

			break;
		case 2:
			if (size < 0.85F){ genStalagmite(world, x, y, z, modifier, 1);}
			else if (size < 0.95F){ genStalagmite(world, x, y, z, modifier, 2);}
			else { genStalagmite(world, x, y, z, modifier, 3);}
			break;
		case 3 :
			if (size < 0.55F){ genStalagmite(world, x, y, z, modifier, 1);}
			else if (size < 0.85F){ genStalagmite(world, x, y, z, modifier, 2);}
			else { genStalagmite(world, x, y, z, modifier, 3);}
			break;
		}
	}

	public void genStalagmite(World world, int x, int y, int z, Block modifier, int size){

		BlockInfo blockandmetadata= this.getBlockToReplace(world, x, y, z);
		Block block = blockandmetadata.block;
		int metadata = blockandmetadata.meta;
		if (!(CaveBlocks.StoneTypeHashSet.contains(block))){return;}
		y++;
		Block [] speleothemSet = CaveBlocks.getSpeleothemSet(block, modifier);

		//sets the base floor block
		if (size == 1){
			setBlockWithoutNotify(world,x, y, z, speleothemSet[BlockSpeleothems.stalagmitesmall], metadata);
		}
		else if (size == 2 ){
			setBlockWithoutNotify(world,x, y, z, speleothemSet[BlockSpeleothems.stalagmitelargebase], metadata);
			setBlockWithoutNotify(world,x, y+1, z, speleothemSet[BlockSpeleothems.stalagmitelargetip], metadata);

		}
		else {

			setBlockWithoutNotify(world,x, y, z, speleothemSet[BlockSpeleothems.stalagmitelargebase], metadata);
			setBlockWithoutNotify(world,x, y+1, z, speleothemSet[BlockSpeleothems.largecolumn], metadata);

			if (world.getBlock(x,  y+3,  z) == block){
				setBlockWithoutNotify(world,x, y+2, z, speleothemSet[BlockSpeleothems.stalactitelargebase], metadata);
				return;
			}
			else {
				setBlockWithoutNotify(world,x, y+2, z, speleothemSet[BlockSpeleothems.stalagmitelargetip], metadata);
				return;
			}
		}

	}

	public void genStalactite(World world, int x, int y, int z, int depth){
		genStalactite(world, x, y, z, depth, null);
	}

	public void genStalactite(World world, int x, int y, int z, int depth, Block modifier){
		float size = random.nextFloat();
		switch (depth){
		case 1 :
			if (size < 0.85F){ genStalactite(world, x, y, z, depth, modifier, 1);}
			else { genStalactite(world, x, y, z, depth, modifier, 2);}
			break;
		case 2:
			if (size < 0.65F){ genStalactite(world, x, y, z, depth, modifier, 1);}
			else if (size < 0.90F){ genStalactite(world, x, y, z, depth, modifier, 2);}
			else { genStalactite(world, x, y, z, depth, modifier, 3);}
			break;
		case 3 :
			if (size < 0.50F){ genStalactite(world, x, y, z, depth, modifier, 1);}
			else if (size < 0.85F){ genStalactite(world, x, y, z, depth, modifier, 2);}
			else { genStalactite(world, x, y, z, depth, modifier, 3);}
			break;
		}
	}


	public void genStalactite(World world, int x, int y, int z, int depth, Block modifier, int size){
		
		Block down1;
		
		if (!world.isAirBlock(x,  y-1, z)){return;}

		BlockInfo blockandmetadata = getBlockToReplace(world, x, y, z);

		Block base =blockandmetadata.block;
		int metadata = blockandmetadata.meta;

		Block[] speleothemSet = CaveBlocks.getSpeleothemSet(base, modifier);

		if (CaveBlocks.StoneTypeHashSet.contains(base)){
			for (int i = 0; i < size; i++){
				down1 = world.getBlock(x, (y-i-1), z);

				if (i==0){
					if (size>1 && down1 == Blocks.air) {
						setBlockWithoutNotify(world,x, y-i, z, speleothemSet[1], metadata);
					}
					else {
						setBlockWithoutNotify(world,x, y-i, z, speleothemSet[0], metadata);
						break;
					}
				}
				else if (i<size-1 && down1 == Blocks.air){
					setBlockWithoutNotify(world,x, y-i, z, speleothemSet[3], metadata);
				}
				else{
					if (down1==base){
						setBlockWithoutNotify(world,x, y-i, z, speleothemSet[5], metadata);
						break;
					}

					else{
						setBlockWithoutNotify(world,x, y-i, z, speleothemSet[2], metadata);
						break;
					}
				}
			}
		}

		else if (base ==Blocks.dirt && depth < 2){
			if (modifier == Blocks.ice){
				setBlockWithoutNotify(world,x, y, z, CaveBlocks.frozenRoots, 0);
			}
			else {
				setBlockWithoutNotify(world,x, y, z, CaveBlocks.Roots, 0);
			}
		}
	}

	/**
	 **Generates an icicle hanging from the ceiling
	 **/
	public  void genIcicle (World world, int x, int y, int z){
		if (random.nextBoolean()){
			setBlockWithoutNotify(world,x, y, z, BlockIcicle.IcicleLargeBase, 0);
			setBlockWithoutNotify(world,x, y-1, z, BlockIcicle.IcicleLargeTip, 0);
		}
		else {
			setBlockWithoutNotify(world,x, y, z, BlockIcicle.IcicleSmall, 0);
		}
	}
	/**
	 **Generates vines hanging from the ceiling
	 **/
	public boolean GenVines(World world, int x, int y, int z)
	{
		int metadata = 0;
		boolean spawn = false;

		if (!world.isAirBlock(x+1, y, z)){
			if (!world.isAirBlock(x-1, y, z)){
				if (!world.isAirBlock(x, y, z+1)){
					if (!world.isAirBlock(x, y, z-1)){
						return false;
					}
				}
			}
		}

		if (world.getBlock(x, y, z-1).renderAsNormalBlock()){
			metadata=4; spawn=true;
		}
		else if (world.getBlock(x, y, z+1).renderAsNormalBlock())	{
			metadata= 1; spawn=true;
		}
		else if (world.getBlock(x-1, y, z).renderAsNormalBlock())	{
			metadata= 2; spawn=true;
		}
		else if (world.getBlock(x+1, y, z).renderAsNormalBlock())	{
			metadata= 8; spawn=true;
		}
		if (spawn)	{
			for (int loop=0; loop< random.nextInt(4)+3 && world.isAirBlock(x, y-loop, z); loop++ )
				world.setBlock(x, y-loop, z, Blocks.vine, metadata, 0);
		}
		return spawn;
	}

	/**
	 **UBC sensitive version of world.getBlock- is overridden in UBCGen if UBC is installed
	 **/
	public BlockInfo getBlockToReplace(World world, int x, int y, int z){
		return new BlockInfo(world.getBlock(x,y,z), world.getBlockMetadata(x,y,z) );
	}

	/**
	 **Use instead of world.setBlock, when you don't want it to update adjacent blocks.  non-fluid blocks placed during world generation should use this method.
	 **/
	public boolean setBlockWithoutNotify(World world, int x, int y, int z, Block block, int metadata){
		int flags = 0;
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
		if ((flags & 1) != 0){
		}
		if (world.captureBlockSnapshots && !world.isRemote)	{
			blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, x, y, z, flags);
			world.capturedBlockSnapshots.add(blockSnapshot);
		}
		boolean flag = chunk.func_150807_a(x & 15, y, z & 15, block, metadata);
		if (!flag && blockSnapshot != null)	{
			world.capturedBlockSnapshots.remove(blockSnapshot);
			blockSnapshot = null;
		}
		world.theProfiler.startSection("checkLight");
		world.func_147451_t(x, y, z);
		world.theProfiler.endSection();
		return flag;
	}

}
