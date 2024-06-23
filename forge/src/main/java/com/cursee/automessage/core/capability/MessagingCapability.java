package com.cursee.automessage.core.capability;

import com.cursee.automessage.core.Config;
import net.minecraft.nbt.CompoundTag;

public class MessagingCapability {
  private int playtime = 0;
  private int[] softCounts = new int[Config.messages.size()];
  private int[] hardCounts = new int[Config.messages.size()];
  
  public int getPlaytime() { return playtime; }
  public int[] getSoftCounts() { return softCounts; }
  public int[] getHardCounts() { return hardCounts; }
  
  public void incrementPlaytime() {
    this.playtime++;
  }
  
  public void incrementSoftCountAtIndex(int index) {
    this.softCounts[index]++;
  }
  
  public void incrementHardCountAtIndex(int index) {
    this.hardCounts[index]++;
  }
  
  public void resetSoftCounts() {
    for (int i = 0; i < this.softCounts.length; i++) {
      this.softCounts[i] = 0;
    }
  }
  
  public void copyOnDeath(MessagingCapability source) {
    this.playtime = source.playtime;
    this.softCounts = source.softCounts;
    this.hardCounts = source.hardCounts;
  }
  public void copyFrom(MessagingCapability source) {
    this.playtime = source.playtime;
    this.softCounts = source.softCounts;
    this.hardCounts = source.hardCounts;
  }
  public void saveNBTData(CompoundTag nbt) {
    nbt.putInt("playtime", playtime);
    nbt.putIntArray("soft_counts", softCounts);
    nbt.putIntArray("hard_counts", hardCounts);
  }
  public void loadNBTData(CompoundTag nbt) {
    playtime = nbt.getInt("playtime");
    softCounts = nbt.getIntArray("soft_counts");
    hardCounts = nbt.getIntArray("hard_counts");
  }
}
