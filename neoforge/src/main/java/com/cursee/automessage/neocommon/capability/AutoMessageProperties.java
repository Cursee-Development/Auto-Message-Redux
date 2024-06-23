package com.cursee.automessage.neocommon.capability;

import com.cursee.automessage.config.Config;
import com.cursee.automessage.neonetwork.Networking;
import com.cursee.automessage.neonetwork.messages.MessageSyncAutoMessageSettings;
import com.cursee.automessage.neoregistry.AutoMessageDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class AutoMessageProperties implements INBTSerializable<CompoundTag> {
	
	public int playtime;
	public long[] soft_counts;
	public long[] hard_counts;
	
	public AutoMessageProperties() {
		this.playtime = 0;
		this.soft_counts = new long[Config.messages.size()];
		this.hard_counts = new long[Config.messages.size()];
	}
	
	public void clone(AutoMessageProperties settings) {
		this.playtime = settings.playtime;
		this.soft_counts = settings.soft_counts;
		this.hard_counts = settings.hard_counts;
	}
	
	public void sync(ServerPlayer player) {
		Networking.sendTo(player, new MessageSyncAutoMessageSettings(this.playtime, this.soft_counts, this.hard_counts));
	}
	
	public static void syncFor(ServerPlayer player) {
		player.getData(AutoMessageDataStorage.PROPERTIES).sync(player);
	}
	
//	@Override
//	public @UnknownNullability CompoundTag serializeNBT() {
//
//		CompoundTag nbt = new CompoundTag();
//
//		nbt.putInt("playtime", this.playtime);
//		nbt.putIntArray("softCounts", this.softCounts);
//		nbt.putIntArray("hardCounts", this.hardCounts);
//
//		return nbt;
//	}
//
//	@Override
//	public void deserializeNBT(CompoundTag nbt) {
//		this.playtime = nbt.getInt("playtime");
//		this.softCounts = nbt.getIntArray("softCounts");
//		this.hardCounts = nbt.getIntArray("hardCounts");
//	}
	
	public void incrementPlaytime() { this.playtime++; }
	public int getPlaytime() { return this.playtime; }
	public long[] getSoftCounts() { return this.soft_counts; }
	public long[] getHardCounts() { return this.hard_counts; }
	
	public void incrementSoftCountAtIndex(int index) {
		this.soft_counts[index]++;
	}
	
	public void incrementHardCountAtIndex(int index) {
		this.hard_counts[index]++;
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag nbt = new CompoundTag();

		nbt.putInt("playtime", this.playtime);
		nbt.putLongArray("soft_counts", this.soft_counts);
		nbt.putLongArray("hard_counts", this.hard_counts);

		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		this.playtime = nbt.getInt("playtime");
		this.soft_counts = nbt.getLongArray("soft_counts");
		this.hard_counts = nbt.getLongArray("hard_counts");
	}
}
