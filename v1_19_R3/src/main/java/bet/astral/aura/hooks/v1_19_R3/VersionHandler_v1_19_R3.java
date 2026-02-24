package bet.astral.aura.hooks.v1_19_R3;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.multiversion.Version;

@Version(
	internalVersion = "v1_19_R3",
	legacyVersion = "v1_19_R3",
	maximumVersion = "1.19.4",
	miniumVersion = "1.19.4",
	legacy = true
)
public class VersionHandler_v1_19_R3 implements VersionHandler {
	private AuraInternal aura;
	private AuraNettyInjector injector;
	@Override
	public void initialize() {
		aura = new Aura_v1_19_R3();
		injector = new NettyInjector_v1_19_R3();
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
