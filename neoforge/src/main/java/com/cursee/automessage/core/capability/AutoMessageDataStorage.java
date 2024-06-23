package com.cursee.automessage.core.capability;

import com.cursee.automessage.Constants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AutoMessageDataStorage {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);
	
	public static final Supplier<AttachmentType<AutoMessageProperties>> PROPERTIES = ATTACHMENT_TYPES.register(
		"properties", () -> AttachmentType.serializable(AutoMessageProperties::new).build());
	
	public static void onPlayerClone(final PlayerEvent.Clone event) {
		//only handle respawn after death -> not portal transfers
		if (event.isWasDeath() && event.getOriginal().hasData(PROPERTIES)) {
			event.getEntity().getData(PROPERTIES).clone(event.getOriginal().getData(PROPERTIES));
		}
	}
	
	public static void onJoinWorld(final EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			AutoMessageProperties.syncFor(player);
		}
	}
}
