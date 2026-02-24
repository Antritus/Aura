package bet.astral.aura.hooks.v1_20_R4;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.multiversion.Version;

@Version(
	internalVersion = "v1_20_R4",
	maximumVersion = "1.21.11",
	miniumVersion = "1.20.5"
)
public class VersionHandler_v1_20_R4 implements VersionHandler {
	private AuraInternal aura;
	private AuraNettyInjector injector;
	@Override
	public void initialize() {
		aura = new Aura_v1_20_R4();
		injector = new NettyInjector_v1_20_R4();
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
