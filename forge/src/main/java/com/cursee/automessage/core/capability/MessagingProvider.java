package com.cursee.automessage.core.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessagingProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
  
  public static Capability<MessagingCapability> PLAYER_MESSAGING_CAPABILITY = CapabilityManager.get(new CapabilityToken<MessagingCapability>() { });
  private MessagingCapability messagingCapability = null;
  private final LazyOptional<MessagingCapability> optional = LazyOptional.of(this::createMessagingCapability);
  
  private MessagingCapability createMessagingCapability() {
    if (this.messagingCapability == null) {
      this.messagingCapability = new MessagingCapability();
    }
    
    return this.messagingCapability;
  }
  
  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == PLAYER_MESSAGING_CAPABILITY) {
      return optional.cast();
    }
    return LazyOptional.empty();
  }
  
//  @Override
//  public CompoundTag serializeNBT() {
//    CompoundTag nbt = new CompoundTag();
//    createMessagingCapability().saveNBTData(nbt);
//    return nbt;
//  }
//
//  @Override
//  public void deserializeNBT(CompoundTag nbt) {
//    createMessagingCapability().loadNBTData(nbt);
//  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag nbt = new CompoundTag();
    createMessagingCapability().saveNBTData(nbt);
    return nbt;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
    createMessagingCapability().loadNBTData(tag);
  }
}

