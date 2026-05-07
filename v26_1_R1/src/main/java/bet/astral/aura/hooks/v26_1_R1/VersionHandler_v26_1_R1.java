package bet.astral.aura.hooks.v26_1_R1;

import bet.astral.aura.api.AuraInternal;
import bet.astral.aura.api.internal.AuraNettyInjector;
import bet.astral.aura.api.multiversion.VersionHandler;
import bet.astral.multiversion.Version;

/**
 * This is exactly the same as v1_20_R4, but I have added this to "support" newer files. 1_20_R4 worked for 12 versions and will work for most updates of minecraft, but this file is only to have "modern" feeling.
 * AKA 2026 -> New year new versioning scheme of minecraft. Adds simplicity to the file system so nobody needs to guess that v1_20_R4 is for 26.1.1...
 */
@Version(
	internalVersion = "v26_1_R1",
	maximumVersion = "26.1.1",
	miniumVersion = "26.1.0"
)
public class VersionHandler_v26_1_R1 implements VersionHandler {
	private AuraInternal aura;
	private AuraNettyInjector injector;
	@Override
	public void initialize() {
		aura = new Aura_v26_1_R1();
		injector = new NettyInjector_v26_1_R1();
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
