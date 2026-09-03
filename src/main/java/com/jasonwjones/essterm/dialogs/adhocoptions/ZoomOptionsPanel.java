package com.jasonwjones.essterm.dialogs.adhocoptions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.jasonwjones.essterm.grid.AdhocOptionCapability;
import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.grid.AdhocOptions.ZoomInPreference;

/**
 * Mirrors the classic ad hoc "Zoom" tab (the "Zoom In" preference and "Member Retention" options).
 *
 * <p>Lanterna's {@code RadioBoxList} can only be disabled as a whole, not per item, so unlike
 * {@code DisplayOptionsPanel}'s independently-greyable checkboxes, the "Zoom In" list only offers the
 * options the current connection actually supports - anything else is named in a note below the list
 * instead of shown as a disabled, unselectable item.
 */
public class ZoomOptionsPanel extends Panel {

	private static final Map<ZoomInPreference, AdhocOptionCapability> ZOOM_IN_CAPABILITIES = new LinkedHashMap<>();
	static {
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.NEXT_LEVEL, AdhocOptionCapability.ZOOM_IN_NEXT_LEVEL);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.ALL_LEVELS, AdhocOptionCapability.ZOOM_IN_ALL_LEVELS);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.BOTTOM_LEVEL, AdhocOptionCapability.ZOOM_IN_BOTTOM_LEVEL);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.SIBLING_LEVEL, AdhocOptionCapability.ZOOM_IN_SIBLING_LEVEL);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.SAME_LEVEL, AdhocOptionCapability.ZOOM_IN_SAME_LEVEL);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.SAME_GENERATION, AdhocOptionCapability.ZOOM_IN_SAME_GENERATION);
		ZOOM_IN_CAPABILITIES.put(ZoomInPreference.FORMULAS, AdhocOptionCapability.ZOOM_IN_FORMULAS);
	}

	private static final Map<ZoomInPreference, String> ZOOM_IN_LABELS = new LinkedHashMap<>();
	static {
		ZOOM_IN_LABELS.put(ZoomInPreference.NEXT_LEVEL, "Next Level");
		ZOOM_IN_LABELS.put(ZoomInPreference.ALL_LEVELS, "All Levels");
		ZOOM_IN_LABELS.put(ZoomInPreference.BOTTOM_LEVEL, "Bottom Level");
		ZOOM_IN_LABELS.put(ZoomInPreference.SIBLING_LEVEL, "Sibling Level");
		ZOOM_IN_LABELS.put(ZoomInPreference.SAME_LEVEL, "Same Level");
		ZOOM_IN_LABELS.put(ZoomInPreference.SAME_GENERATION, "Same Generation");
		ZOOM_IN_LABELS.put(ZoomInPreference.FORMULAS, "Formulas");
	}

	public ZoomOptionsPanel(AdhocOptions options, EnumSet<AdhocOptionCapability> supported) {
		super();

		List<ZoomInPreference> available = new ArrayList<>();
		List<String> unavailable = new ArrayList<>();
		for (Map.Entry<ZoomInPreference, AdhocOptionCapability> entry : ZOOM_IN_CAPABILITIES.entrySet()) {
			if (supported.contains(entry.getValue())) {
				available.add(entry.getKey());
			} else {
				unavailable.add(ZOOM_IN_LABELS.get(entry.getKey()));
			}
		}
		if (available.isEmpty()) {
			available.add(ZoomInPreference.NEXT_LEVEL);
		}

		Panel zoomIn = new Panel();
		zoomIn.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));

		RadioBoxList<String> zoomInList = new RadioBoxList<String>();
		zoomInList.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		for (ZoomInPreference preference : available) {
			zoomInList.addItem(ZOOM_IN_LABELS.get(preference));
		}
		int selectedIndex = available.indexOf(options.getZoomInPreference());
		zoomInList.setCheckedItemIndex(selectedIndex < 0 ? 0 : selectedIndex);
		zoomInList.addListener(new RadioBoxList.Listener() {
			@Override
			public void onSelectionChanged(int selectedIndex, int previousSelection) {
				options.setZoomInPreference(available.get(selectedIndex));
			}
		});
		zoomIn.addComponent(zoomInList.withBorder(Borders.singleLine("Zoom In")));

		if (!unavailable.isEmpty()) {
			zoomIn.addComponent(new Label("Not available: " + String.join(", ", unavailable)));
		}
		addComponent(zoomIn);

		Panel memberRetention = new Panel();
		memberRetention.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		memberRetention.addComponent(checkBox("Include Selection", options.isIncludeSelection(),
				options::setIncludeSelection, supported.contains(AdhocOptionCapability.INCLUDE_SELECTION)));
		memberRetention.addComponent(checkBox("Within Selected Group", options.isWithinSelectedGroup(),
				options::setWithinSelectedGroup, supported.contains(AdhocOptionCapability.WITHIN_SELECTED_GROUP)));
		memberRetention.addComponent(checkBox("Remove Unselected Groups", options.isRemoveUnselectedGroup(),
				options::setRemoveUnselectedGroup, supported.contains(AdhocOptionCapability.REMOVE_UNSELECTED_GROUP)));
		addComponent(memberRetention.withBorder(Borders.singleLine("Member Retention")));
	}

	private static CheckBox checkBox(String label, boolean initiallyChecked, java.util.function.Consumer<Boolean> onChange, boolean enabled) {
		CheckBox checkBox = new CheckBox(label);
		checkBox.setChecked(initiallyChecked);
		checkBox.addListener(new CheckBox.Listener() {
			@Override
			public void onStatusChanged(boolean checked) {
				onChange.accept(checked);
			}
		});
		checkBox.setEnabled(enabled);
		return checkBox;
	}

}
