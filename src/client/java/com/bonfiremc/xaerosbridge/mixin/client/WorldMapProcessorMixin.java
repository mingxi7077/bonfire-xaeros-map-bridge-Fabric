package com.bonfiremc.xaerosbridge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "xaero.map.MapProcessor", remap = false)
public abstract class WorldMapProcessorMixin {
	@Redirect(
		method = "getMainId(ILnet/minecraft/class_634;)Ljava/lang/String;",
		at = @At(
			value = "INVOKE",
			target = "Lxaero/lib/common/config/single/SingleConfigManager;getEffective(Lxaero/lib/common/config/option/ConfigOption;)Ljava/lang/Object;",
			remap = false
		),
		remap = false
	)
	private Object bonfire$forceWorldMapSharedRoot(Object configManager, Object option) {
		return Boolean.FALSE;
	}
}
