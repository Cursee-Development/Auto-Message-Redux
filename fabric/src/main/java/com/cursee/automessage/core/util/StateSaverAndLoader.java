package com.cursee.automessage.core.util;

import com.cursee.automessage.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends SavedData {
	public HashMap<UUID, PlayerData> players = new HashMap<>();
//	@Override
//	public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
//		CompoundTag playerGroupNBT = new CompoundTag();
//		players.forEach(((uuid, playerData) -> {
//			CompoundTag playerIndividualNBT = new CompoundTag();
//
//			playerIndividualNBT.putInt("playtime", playerData.playtime);
//			playerIndividualNBT.putIntArray("soft_counters", playerData.soft_counters);
//			playerIndividualNBT.putIntArray("hard_counters", playerData.hard_counters);
//
//			playerGroupNBT.put(uuid.toString(), playerIndividualNBT);
//		}));
//		nbt.put("players", playerGroupNBT);
//		return nbt;
//	}
	
	
	// 1.20.1-prior
	public static StateSaverAndLoader createFromNbt(CompoundTag tag) {
		StateSaverAndLoader state = new StateSaverAndLoader();
		CompoundTag playerGroupNBT = tag.getCompound("players");
		playerGroupNBT.getAllKeys().forEach(key -> {
			PlayerData playerData = new PlayerData();
	
			playerData.playtime = playerGroupNBT.getCompound(key).getInt("playtime");
			playerData.soft_counts = playerGroupNBT.getCompound(key).getLongArray("soft_counters");
			playerData.hard_counts = playerGroupNBT.getCompound(key).getLongArray("hard_counters");
	
			UUID uuid = UUID.fromString(key);
			state.players.put(uuid, playerData);
		});
		return state;
	}

	private static StateSaverAndLoader create(CompoundTag tag, HolderLookup.Provider provider) {
		StateSaverAndLoader state = new StateSaverAndLoader();
		CompoundTag groupData = tag.getCompound("players");
		groupData.getAllKeys().forEach(key -> {
			PlayerData individualData = new PlayerData();

			individualData.playtime = groupData.getCompound(key).getInt("playtime");
			individualData.soft_counts = groupData.getCompound(key).getLongArray("soft_counters");
			individualData.hard_counts = groupData.getCompound(key).getLongArray("hard_counters");

			UUID uuid = UUID.fromString(key);
			state.players.put(uuid, individualData);
		});
		return state;
	}
	
	private static Factory<StateSaverAndLoader> type = new Factory<>(
		StateSaverAndLoader::new, // If there's no 'StateSaverAndLoader' yet create one
		StateSaverAndLoader::create, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
		null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
	);

	public static StateSaverAndLoader getServerState(MinecraftServer server) {
		DimensionDataStorage persistentStateManager = server.getLevel(Level.OVERWORLD).getDataStorage();
		
		
		// 1.20.1-prior
		// StateSaverAndLoader state = persistentStateManager.computeIfAbsent(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, CommonConstants.MOD_ID);
		
		// 1.20.2-later
		StateSaverAndLoader state = persistentStateManager.computeIfAbsent(type, Constants.MOD_ID);
		
		
		state.setDirty();
		return state;
	}
	public static PlayerData getPlayerState(LivingEntity player) {
		StateSaverAndLoader serverState = getServerState(player.level().getServer());
		PlayerData playerState = serverState.players.computeIfAbsent(player.getUUID(), uuid -> new PlayerData());
		return playerState;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		CompoundTag groupData = new CompoundTag();
		players.forEach(((uuid, playerData) -> {
			CompoundTag individualData = new CompoundTag();

			individualData.putInt("playtime", playerData.playtime);
			individualData.putLongArray("soft_counters", playerData.soft_counts);
			individualData.putLongArray("hard_counters", playerData.hard_counts);

			groupData.put(uuid.toString(), individualData);
		}));
		tag.put("players", groupData);
		return tag;
	}
}
