package com.doomvalueadjustment;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("doomvalueadjustment")
public interface DoomValueAdjustmentConfig extends Config
{
	@ConfigItem(
		keyName = "bonesValuation",
		name = "Sun-kissed bones value",
		description = "How to value Sun-kissed bones in the loot total: 0 GP, or approximate value as 77% of dragon bones GE price."
	)
	default BonesValuation bonesValuation()
	{
		return BonesValuation.ZERO_GP;
	}

	enum BonesValuation
	{
		ZERO_GP("0 GP"),
		APPROXIMATE("77% of dragon bones (GE)");

		private final String name;

		BonesValuation(String name)
		{
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
