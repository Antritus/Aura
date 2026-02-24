package bet.astral.aura.hooks.v1_9_R1;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.multiversion.Version;

@Version(
	internalVersion = "v1_9_R1",
	legacyVersion = "v1_9_R1",
	maximumVersion = "1.9.2",
	miniumVersion = "1.9",
	legacy = true
)
public class VersionHandler_v1_9_R1 implements VersionHandler {
	private AuraInternal aura;
	private AuraNettyInjector injector;
	@Override
	public void initialize() {
		aura = new Aura_v1_9_R1();
		injector = new NettyInjector_v1_9_R1();
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
