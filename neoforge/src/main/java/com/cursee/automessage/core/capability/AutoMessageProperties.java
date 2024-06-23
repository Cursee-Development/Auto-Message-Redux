package com.cursee.automessage.core.capability;

import com.cursee.automessage.core.Config;
import com.cursee.automessage.core.networking.Networking;
import com.cursee.automessage.core.networking.messages.MessageSyncAutoMessageSettings;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class AutoMessageProperties implements INBTSerializable<CompoundTag> {
	
	public int playtime;
	public int[] softCounts;
	public int[] hardCounts;
	
	public AutoMessageProperties() {
		this.playtime = 0;
		this.softCounts = new int[Config.messages.size()];
		this.hardCounts = new int[Config.messages.size()];
	}
	
	public void clone(AutoMessageProperties settings) {
		this.playtime = settings.playtime;
		this.softCounts = settings.softCounts;
		this.hardCounts = settings.hardCounts;
	}
	
	public void sync(ServerPlayer player) {
		Networking.sendTo(player, new MessageSyncAutoMessageSettings(this));
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
	public int[] getSoftCounts() { return this.softCounts; }
	public int[] getHardCounts() { return this.hardCounts; }
	
	public void incrementSoftCountAtIndex(int index) {
		this.softCounts[index]++;
	}
	
	public void incrementHardCountAtIndex(int index) {
		this.hardCounts[index]++;
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();

		tag.putInt("playtime", this.playtime);
		tag.putIntArray("softCounts", this.softCounts);
		tag.putIntArray("hardCounts", this.hardCounts);

		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		this.playtime = tag.getInt("playtime");
		this.softCounts = tag.getIntArray("softCounts");
		this.hardCounts = tag.getIntArray("hardCounts");
	}
}
