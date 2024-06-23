package com.cursee.automessage.core.util;

import com.cursee.automessage.core.Config;

public class PlayerData {
	public int playtime = 0;
	public long[] soft_counts = new long[Config.messages.size()];
	public long[] hard_counts = new long[Config.messages.size()];
	
	public void incrementPlaytime() {
		this.playtime++;
	}
	
	public void incrementSoftCounterAtIndex(int index) {
		this.soft_counts[index]++;
	}
	public void incrementHardCounterAtIndex(int index) {
		this.hard_counts[index]++;
	}
}
