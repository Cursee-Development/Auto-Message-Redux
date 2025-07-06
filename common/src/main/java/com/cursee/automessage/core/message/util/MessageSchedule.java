package com.cursee.automessage.core.message.util;

import com.google.gson.annotations.SerializedName;

public enum MessageSchedule {
    @SerializedName("on_first_join") ON_FIRST_JOIN,
    @SerializedName("on_join_level") ON_JOIN_LEVEL,
    @SerializedName("on_death") ON_DEATH,
    @SerializedName("on_respawn") ON_RESPAWN,

//    @SerializedName("on_sleep_in_bed") ON_SLEEP_IN_BED,
//    @SerializedName("on_wake_up") ON_WAKE_UP,
//
//    @SerializedName("overworld_to_nether") OVERWORLD_TO_NETHER,
//    @SerializedName("overworld_to_end") OVERWORLD_TO_END,
//    @SerializedName("nether_to_overworld") NETHER_TO_OVERWORLD,
//    @SerializedName("end_to_overworld") END_TO_OVERWORLD;
}
