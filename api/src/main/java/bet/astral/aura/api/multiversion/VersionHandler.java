package bet.astral.aura.api.multiversion;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface VersionHandler extends bet.astral.multiversion.VersionHandler {
	/**
	 * Returns the internal aura version which is handled by NMS versions
	 * @return internal aura
	 */
	AuraInternal getInternalAura();

	/**
	 * Returns the netty injector
	 * @return injector
	 */
	AuraNettyInjector getNettyInjector();
}
