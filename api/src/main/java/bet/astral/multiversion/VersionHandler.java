package bet.astral.multiversion;

public interface VersionHandler {
	void initialize();

	default Version getVersion() {
		return getClass().getAnnotation(Version.class);
	}
}
