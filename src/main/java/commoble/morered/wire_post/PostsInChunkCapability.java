package commoble.morered.wire_post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.mojang.serialization.Codec;

import commoble.databuddy.codec.SetCodecHelper;
import commoble.databuddy.nbt.NBTListCodec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PostsInChunkCapability
{
	/** Don't get the default IPostsInChunk instance from this, it intentionally returns a broken instance that will probably cause crashes if used **/
	@CapabilityInject(IPostsInChunk.class)
	public static Capability<IPostsInChunk> INSTANCE = null;

	/** This codec serializes a list-like element **/
	public static final Codec<Set<BlockPos>> POST_SET_CODEC = SetCodecHelper.makeSetCodec(BlockPos.CODEC);
	
	public static class Storage implements Capability.IStorage<IPostsInChunk>
	{
		public static final String POSITIONS = "positions";
		
		@SuppressWarnings("deprecation")
		private static final NBTListCodec<BlockPos, CompoundNBT> POS_LISTER = new NBTListCodec<>(
			POSITIONS,
			NBTListCodec.ListNBTType.COMPOUND,
			NBTUtil::writeBlockPos,
			NBTUtil::readBlockPos);
		
		// this must return a CompoundNBT
		@SuppressWarnings("deprecation")
		@Override
		public INBT writeNBT(Capability<IPostsInChunk> capability, IPostsInChunk instance, Direction side)
		{
			return POS_LISTER.write(new ArrayList<>(instance.getPositions()), new CompoundNBT());
		}

		@SuppressWarnings("deprecation")
		@Override
		public void readNBT(Capability<IPostsInChunk> capability, IPostsInChunk instance, Direction side, INBT nbt)
		{
			if (nbt instanceof CompoundNBT)
			{
				instance.setPositions(new HashSet<>(POS_LISTER.read((CompoundNBT)nbt)));
			}
		}
		
	}
}
