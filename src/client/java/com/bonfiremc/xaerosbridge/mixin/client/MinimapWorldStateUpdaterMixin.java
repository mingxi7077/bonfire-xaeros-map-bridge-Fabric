package com.bonfiremc.xaerosbridge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "xaero.hud.minimap.world.state.MinimapWorldStateUpdater", remap = false)
public abstract class MinimapWorldStateUpdaterMixin {
	@Redirect(
		method = "getAutoRootContainerPath(I)Lxaero/hud/path/XaeroPath;",
		at = @At(
			value = "INVOKE",
			target = "Lxaero/lib/common/config/single/SingleConfigManager;getEffective(Lxaero/lib/common/config/option/ConfigOption;)Ljava/lang/Object;",
			remap = false
		),
		remap = false
	)
	private Object bonfire$forceMinimapSharedRoot(Object configManager, Object option) {
		return Boolean.FALSE;
	}
}
