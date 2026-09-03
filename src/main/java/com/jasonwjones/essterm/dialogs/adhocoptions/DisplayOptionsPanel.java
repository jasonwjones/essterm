package com.jasonwjones.essterm.dialogs.adhocoptions;

import java.util.EnumSet;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.TextBox;
import com.jasonwjones.essterm.grid.AdhocOptionCapability;
import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.grid.AdhocOptions.Indentation;

/**
 * Mirrors the classic ad hoc "Display" tab (Indentation, Suppress, Replacement, Aliases, Cells).
 * Every control writes straight into the given {@link AdhocOptions} as it's changed, matching the
 * rest of this dialog's convention - there's no separate "apply" step within the panel itself.
 *
 * <p>Anything the current connection can't apply (see {@link AdhocOptionCapability}) is disabled
 * rather than hidden: the classic dialog's shape is kept intact even where a backend can't (yet)
 * back a given field. Lanterna's list widgets ({@code CheckBoxList}/{@code RadioBoxList}) can only be
 * disabled as a whole, not per item, so independently-greyable options use individual
 * {@code CheckBox} components instead.
 */
public class DisplayOptionsPanel extends Panel {

	private TextBox missingTextBox;

	private TextBox noAccessTextBox;

	public DisplayOptionsPanel(AdhocOptions options, EnumSet<AdhocOptionCapability> supported) {
		super();

		Panel indentationPanel = new Panel();
		indentationPanel.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));

		RadioBoxList<String> indentation = new RadioBoxList<String>();
		indentation.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		indentation.addItem("None");
		indentation.addItem("Subitems");
		indentation.addItem("Totals");
		indentation.setCheckedItemIndex(indentationIndex(options.getIndentation()));
		indentation.addListener(new RadioBoxList.Listener() {
			@Override
			public void onSelectionChanged(int selectedIndex, int previousSelection) {
				options.setIndentation(indentationForIndex(selectedIndex));
			}
		});
		indentation.setEnabled(supported.contains(AdhocOptionCapability.INDENTATION));
		indentationPanel.addComponent(indentation.withBorder(Borders.singleLine("Indentation")));
		addComponent(indentationPanel);

		Panel suppress = new Panel();
		suppress.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		suppress.addComponent(checkBox("#Missing Rows", options.isSuppressMissingRows(),
				options::setSuppressMissingRows, supported.contains(AdhocOptionCapability.SUPPRESS_MISSING_ROWS)));
		suppress.addComponent(checkBox("Zero Rows", options.isSuppressZeroRows(),
				options::setSuppressZeroRows, supported.contains(AdhocOptionCapability.SUPPRESS_ZERO_ROWS)));
		suppress.addComponent(checkBox("Underscore Characters", options.isSuppressUnderscores(),
				options::setSuppressUnderscores, supported.contains(AdhocOptionCapability.SUPPRESS_UNDERSCORE_ROWS)));
		addComponent(suppress.withBorder(Borders.singleLine("Suppress")));

		Panel replacement = new Panel(new GridLayout(2));
		replacement.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		replacement.addComponent(new Label("#Missing Label").setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
		this.missingTextBox = new TextBox(options.getMissingLabel());
		replacement.addComponent(this.missingTextBox);
		replacement.addComponent(new Label("#No Access Label"));
		this.noAccessTextBox = new TextBox(options.getNoAccessLabel());
		replacement.addComponent(this.noAccessTextBox);
		addComponent(replacement.withBorder(Borders.singleLine("Replacement")));

		Panel aliases = new Panel();
		aliases.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		aliases.addComponent(checkBox("Use aliases", options.isUseAliases(),
				options::setUseAliases, supported.contains(AdhocOptionCapability.USE_ALIASES)));
		aliases.addComponent(checkBox("Use both member names and aliases for rows", options.isUseBothMemberNameAndAlias(),
				options::setUseBothMemberNameAndAlias, supported.contains(AdhocOptionCapability.USE_BOTH_MEMBER_NAME_AND_ALIAS)));
		Panel aliasTableRow = new Panel(new GridLayout(2));
		aliasTableRow.addComponent(new Label("Alias table"));
		TextBox aliasTable = new TextBox(options.getAliasTableName() == null ? "" : options.getAliasTableName());
		aliasTable.setEnabled(supported.contains(AdhocOptionCapability.ALIAS_TABLE_SELECTION));
		aliasTable.setTextChangeListener((newText, byUser) -> {
			if (byUser) {
				options.setAliasTableName(newText.isBlank() ? null : newText);
			}
		});
		aliasTableRow.addComponent(aliasTable);
		aliases.addComponent(aliasTableRow);
		addComponent(aliases.withBorder(Borders.singleLine("Aliases")));

		Panel cells = new Panel();
		cells.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		cells.addComponent(checkBox("Repeat member labels", options.isRepeatMemberLabels(),
				options::setRepeatMemberLabels, supported.contains(AdhocOptionCapability.REPEAT_MEMBER_LABELS)));
		addComponent(cells.withBorder(Borders.singleLine("Cells")));
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

	private static int indentationIndex(Indentation indentation) {
		switch (indentation) {
		case NONE:
			return 0;
		case TOTALS:
			return 2;
		case SUBITEMS:
		default:
			return 1;
		}
	}

	private static Indentation indentationForIndex(int index) {
		switch (index) {
		case 0:
			return Indentation.NONE;
		case 2:
			return Indentation.TOTALS;
		case 1:
		default:
			return Indentation.SUBITEMS;
		}
	}

	String getMissingText() {
		return missingTextBox.getText();
	}

	String getNoAccessText() {
		return noAccessTextBox.getText();
	}

}
