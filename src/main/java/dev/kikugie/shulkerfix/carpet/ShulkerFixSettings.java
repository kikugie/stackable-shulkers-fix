package dev.kikugie.shulkerfix.carpet;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;

public class ShulkerFixSettings {
	public static final String SHULKERFIX = "shulkerfix";
	public static final String INTRUSIVE = "intrusive";

	@Rule(categories = {SHULKERFIX, INTRUSIVE, RuleCategory.BUGFIX})
	public static boolean hopperShulkerStacking = false;

	@Rule(categories = {SHULKERFIX, RuleCategory.BUGFIX, RuleCategory.EXPERIMENTAL})
	public static boolean clientShulkerSync = false;

	@Rule(categories = {SHULKERFIX, RuleCategory.BUGFIX, RuleCategory.EXPERIMENTAL})
	public static boolean legacyShulkerItemMerging = false;

	@Rule(categories = {SHULKERFIX, RuleCategory.FEATURE, RuleCategory.EXPERIMENTAL})
	public static boolean hopperCollectSingleShulkers = false;

	@Rule(categories = {SHULKERFIX, RuleCategory.FEATURE, RuleCategory.EXPERIMENTAL})
	public static boolean minecartCollectSingleShulkers = false;

	@Rule(categories = {SHULKERFIX, RuleCategory.FEATURE, RuleCategory.EXPERIMENTAL})
	public static boolean overstackedMinecartSlowdown = true;

	@Rule(categories = {SHULKERFIX, INTRUSIVE, RuleCategory.BUGFIX}, options = {"true", "false", "capped", "normalized"})
	public static String overstackedShulkerSignalStrength = "true";
}
