package bet.astral.aura.hooks.v1_19_R2;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.multiversion.Version;

@Version(
	internalVersion = "v1_19_R2",
	legacyVersion = "v1_19_R2",
	maximumVersion = "1.19.3",
	miniumVersion = "1.19.3",
	legacy = true
)
public class VersionHandler_v1_19_R2 implements VersionHandler {
	private AuraInternal aura;
	private AuraNettyInjector injector;
	@Override
	public void initialize() {
		aura = new Aura_v1_19_R2();
		injector = new NettyInjector_v1_19_R2();
	}

	@Override
	public AuraInternal getInternalAura() {
		return aura;
	}

	@Override
	public AuraNettyInjector getNettyInjector() {
		return injector;
	}
}
