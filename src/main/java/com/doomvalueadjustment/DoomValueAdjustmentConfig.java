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
		description = "How to value Sun-kissed bones in the loot total: leave as-is, or infer value as 77% of dragon bones GE price."
	)
	default BonesValuation bonesValuation()
	{
		return BonesValuation.DEFAULT;
	}

	@ConfigItem(
		keyName = "spiritSeedValuation",
		name = "Spirit seed value",
		description = "How to value spirit seeds in the loot total."
	)
	default SpiritSeedValuation spiritSeedValuation()
	{
		return SpiritSeedValuation.DEFAULT;
	}

	enum BonesValuation
	{
		DEFAULT("Default"),
		INFERRED("77% of dragon bones (GE)");

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

	enum SpiritSeedValuation
	{
		DEFAULT("Default"),
		ZERO_GP("0 GP"),
		SEED_PACK_AVERAGE("Seed pack average (80,000 GP)");

		private final String name;

		SpiritSeedValuation(String name)
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
