package com.doomvalueadjustment;

import com.google.inject.Provides;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Doom Loot Value Fix",
	description = "Subtracts the bogus Sun-kissed bones value from the Doom end-level loot total",
	tags = {"doom", "loot", "value", "bones"},
	enabledByDefault = true
)
public class DoomValueAdjustmentPlugin extends Plugin
{
	private static final int BOGUS_BONE_PRICE = 8_000;
	private static final double APPROXIMATE_DRAGON_FRACTION = 0.77;

	private static final Pattern VALUE_PATTERN = Pattern.compile("([\\d,]+)");
	private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("#,##0");

	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private DoomValueAdjustmentConfig config;

	private long originalTotalValue = -1;

	@Provides
	DoomValueAdjustmentConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DoomValueAdjustmentConfig.class);
	}

	@Override
	protected void startUp()
	{
		originalTotalValue = -1;
	}

	@Override
	protected void shutDown()
	{
		originalTotalValue = -1;
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == InterfaceID.DOM_END_LEVEL_UI)
		{
			originalTotalValue = -1;
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		Widget lootValueWidget = client.getWidget(InterfaceID.DomEndLevelUi.LOOT_VALUE);
		if (lootValueWidget == null || lootValueWidget.isHidden())
		{
			originalTotalValue = -1;
			return;
		}

		Widget lootContents = client.getWidget(InterfaceID.DomEndLevelUi.LOOT_CONTENTS);
		if (lootContents == null)
		{
			return;
		}

		Widget[] children = lootContents.getDynamicChildren();
		if (children == null)
		{
			return;
		}

		int totalBonesQty = 0;
		for (Widget child : children)
		{
			if (child != null && child.getItemId() == ItemID.SUNKISSED_BONES)
			{
				int qty = child.getItemQuantity();
				if (qty > 0)
				{
					totalBonesQty += qty;
				}
			}
		}

		if (totalBonesQty <= 0)
		{
			return;
		}

		String currentText = lootValueWidget.getText();
		if (currentText == null || currentText.isEmpty())
		{
			return;
		}

		if (originalTotalValue < 0)
		{
			originalTotalValue = parseGpFromText(currentText);
		}

		if (originalTotalValue <= 0)
		{
			return;
		}

		int valuePerBone;
		if (config.bonesValuation() == DoomValueAdjustmentConfig.BonesValuation.APPROXIMATE)
		{
			int dragonBonesPrice = itemManager.getItemPrice(ItemID.DRAGON_BONES);
			valuePerBone = (int) (dragonBonesPrice * APPROXIMATE_DRAGON_FRACTION);
		}
		else
		{
			valuePerBone = 0;
		}

		// Replace bogus 8k each with chosen value: subtract (8000 - valuePerBone) per bone
		long bonesDeduction = (long) totalBonesQty * (BOGUS_BONE_PRICE - valuePerBone);
		long correctedValue = Math.max(0L, originalTotalValue - bonesDeduction);
		String correctedText = "Value: " + VALUE_FORMAT.format(correctedValue) + " GP";

		if (!correctedText.equals(currentText))
		{
			lootValueWidget.setText(correctedText);
		}
	}

	private long parseGpFromText(String text)
	{
		Matcher m = VALUE_PATTERN.matcher(text);
		if (!m.find())
		{
			return 0;
		}
		try
		{
			return Long.parseLong(m.group(1).replace(",", ""));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
}
