package bet.astral.multiversion;

import io.github.classgraph.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class ClassFetcher {

	public static @Nullable VersionHandler fetch(String packageName) {
		ClassGraph classGraph = new ClassGraph()
			.enableAllInfo()
			.acceptPackages(packageName);

		String minecraftVersion = Bukkit.getMinecraftVersion();

		try (ScanResult scanResult = classGraph.scan()) {

			for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Version.class)) {

				AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(Version.class);
				AnnotationParameterValueList values = annotationInfo.getParameterValues();

				boolean legacy = (boolean) values.get("legacy").getValue();

				if (legacy && isLegacyVersioning(minecraftVersion)) {

					String legacyVersion = (String) values.get("legacyVersion").getValue();
					String currentLegacyVersion = getLegacyInternalVersion();

					if (currentLegacyVersion.equals(legacyVersion)) {
						return handle(classInfo);
					}

				} else if (!legacy) {

					String minVersion = (String) values.get("miniumVersion").getValue();
					String maxVersion = (String) values.get("maximumVersion").getValue();

					if (isSameOrNewerVersion(minecraftVersion, minVersion)
						&& isSameOrNewerVersion(maxVersion, minecraftVersion)) {
						return handle(classInfo);
					}
				}
			}
		}

		return null;
	}

	public static @NotNull VersionHandler handle(@NotNull ClassInfo classInfo) {
		try {
			return (VersionHandler)
				classInfo.loadClass()
					.getDeclaredConstructor()
					.newInstance();
		} catch (InstantiationException |
				 IllegalAccessException |
				 InvocationTargetException |
				 NoSuchMethodException e) {
			throw new RuntimeException("Failed to instantiate VersionHandler: "
				+ classInfo.getName(), e);
		}
	}

	public static String getLegacyInternalVersion() {
		return Bukkit.getServer()
			.getClass()
			.getPackage()
			.getName()
			.split("\\.")[3];
	}

	public static boolean isLegacyVersioning() {
		return isLegacyVersioning(Bukkit.getMinecraftVersion());
	}

	public static boolean isLegacyVersioning(String version) {
		return compareVersions(version, "1.20.5") < 0;
	}

	public static boolean isSameOrNewerVersion(@NotNull String current,
											   @NotNull String other) {
		return compareVersions(current, other) >= 0;
	}

	public static boolean isNewerVersion(@NotNull String current,
										 @NotNull String other) {
		return compareVersions(current, other) > 0;
	}

	public static int compareVersions(@NotNull String v1,
									  @NotNull String v2) {

		int[] a = splitVersionPatchDefault(v1);
		int[] b = splitVersionPatchDefault(v2);

		if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
		if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
		return Integer.compare(a[2], b[2]);
	}

	public static int @NotNull [] splitVersionPatchDefault(@NotNull String version) {
		String[] split = version.split("\\.");
		int[] result = new int[3];

		for (int i = 0; i < split.length && i < 3; i++) {
			result[i] = Integer.parseInt(split[i]);
		}

		return result;
	}

	public static boolean hasMinor(@NotNull String version) {
		return version.split("\\.").length > 1;
	}

	public static boolean hasPatch(@NotNull String version) {
		return version.split("\\.").length > 2;
	}
}
