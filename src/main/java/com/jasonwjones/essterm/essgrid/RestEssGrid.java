package com.jasonwjones.essterm.essgrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appliedolap.essbase.EssCube;
import com.appliedolap.essbase.EssCubeView;
import com.appliedolap.essbase.EssDimension;
import com.appliedolap.essbase.EssScript;
import com.jasonwjones.essterm.grid.AdhocOptionCapability;
import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.grid.EssCell;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridException;
import com.jasonwjones.essterm.grid.EssMemberNode;
import com.jasonwjones.essterm.grid.MemberPlacement;
import com.jasonwjones.essterm.grid.Point;
import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.essterm.simplegrid.DoubleEssCell;
import com.jasonwjones.essterm.simplegrid.MissingCell;
import com.jasonwjones.essterm.simplegrid.SimpleEssCell;
import com.jasonwjones.griddly.Grid;
import com.jasonwjones.griddly.impl.BasicGrid;

/**
 * REST analog of {@link EssbaseEssGrid}, backed by essbase-rest-client's {@link EssCubeView}.
 *
 * <p>{@link #pivot} works structurally but has no confirmed-valid coordinate semantics yet (see
 * EssCubeView's own javadoc), so it may throw a live server error. {@link #setData} and
 * {@link #clearData} throw {@link UnsupportedOperationException}: essbase-rest-client has no cell
 * data-entry support at all yet. {@link #updateCubeViewProperties} applies whatever
 * {@link EssCubeView.GridPreferences} supports (see {@link #getSupportedOptions()} for exactly which
 * fields) - aliases-on/off and alias table selection aren't among them: the REST wire format has no
 * confirmed equivalent for either.
 */
class RestEssGrid implements EssGrid {

	private static final Logger logger = LoggerFactory.getLogger(RestEssGrid.class);

	private final ChosenConnection connection;

	private final EssCube cube;

	private EssCubeView cubeView;

	RestEssGrid(ChosenConnection connection, EssCube cube) {
		this.connection = connection;
		this.cube = cube;
		// openCubeView() alone isn't actually a fresh default: the REST API silently persists every
		// grid operation into a hidden per-user layout and reads it back on the next "default grid"
		// request, so without this reset a new ad hoc grid would pick up wherever a previous session
		// left off instead of the genuinely blank starting point a new grid should be.
		cube.resetDefaultView();
		this.cubeView = cube.openCubeView();
	}

	@Override
	public ChosenConnection getConnection() {
		return connection;
	}

	@Override
	public void retrieve() {
		cubeView.refresh();
	}

	@Override
	public void resetToDefault() {
		// Mirrors the same reset+reopen+refresh the constructor does for a brand new grid: the REST
		// API persists every operation into a hidden per-user layout, so simply re-opening isn't
		// enough on its own - it would just pick back up wherever this view left off.
		cube.resetDefaultView();
		this.cubeView = cube.openCubeView();
		cubeView.refresh();
	}

	@Override
	public void zoomIn(Point point) {
		cubeView.zoomIn(point.getRow(), point.getCol());
	}

	@Override
	public void zoomIn(Point point, EnumSet<ZoomOptions> zoomOptions) throws Exception {
		if (!zoomOptions.contains(ZoomOptions.INCLUDE_SELECTION)) {
			zoomIn(point);
			return;
		}
		// No per-call way to request this - "includeSelection" is a session-wide preference, not a
		// parameter on the zoom action itself. Toggle it on just for this one call and restore
		// whatever was there before, so a one-off "zoom in and keep the selection" doesn't leave the
		// session-wide default changed underneath the user.
		EssCubeView.GridPreferences before = cubeView.getPreferences();
		if (!before.includeSelection()) {
			cubeView.setPreferences(withIncludeSelection(before, true));
		}
		try {
			zoomIn(point);
		} finally {
			if (!before.includeSelection()) {
				cubeView.setPreferences(before);
			}
		}
	}

	private static EssCubeView.GridPreferences withIncludeSelection(EssCubeView.GridPreferences source, boolean includeSelection) {
		return new EssCubeView.GridPreferences(source.indentation(), source.suppressMissingRows(),
				source.suppressZeroRows(), source.suppressUnderscoreRows(), source.repeatMemberLabels(),
				source.zoomInPreference(), includeSelection, source.withinSelectedGroup(), source.removeUnselectedGroup());
	}

	@Override
	public void zoomOut(Point point) {
		cubeView.zoomOut(point.getRow(), point.getCol());
	}

	@Override
	public void keepOnly(Point point) {
		cubeView.keepOnly(point.getRow(), point.getCol());
	}

	@Override
	public void removeOnly(Point point) {
		cubeView.removeOnly(point.getRow(), point.getCol());
	}

	@Override
	public void pivot(Point start, Point end) {
		cubeView.pivot(start.getRow(), start.getCol(), end.getRow(), end.getCol());
	}

	@Override
	public void setData(Point point, double value) throws Exception {
		throw new UnsupportedOperationException("Setting cell data is not yet supported via the REST API");
	}

	@Override
	public void clearData(Point point) throws Exception {
		throw new UnsupportedOperationException("Clearing cell data is not yet supported via the REST API");
	}

	@Override
	public Grid<EssCell> getGrid() {
		Grid<EssCell> grid = new BasicGrid<>(cubeView.getRows(), cubeView.getColumns());
		for (int row = 0; row < cubeView.getRows(); row++) {
			for (int col = 0; col < cubeView.getColumns(); col++) {
				grid.setCellData(row, col, toCell(row, col));
			}
		}
		return grid;
	}

	private EssCell toCell(int row, int col) {
		String text = cubeView.getCell(row, col);
		if (cubeView.getCellType(row, col) == EssCubeView.CellType.DATA) {
			if (text == null || text.isBlank()) {
				return new MissingCell();
			}
			return new DoubleEssCell(Double.valueOf(text));
		}
		return new SimpleEssCell(text);
	}

	@Override
	public Collection<String> getCalcScripts() {
		List<String> names = new ArrayList<>();
		for (EssScript script : cube.getCalcScripts()) {
			names.add(script.getName());
		}
		return names;
	}

	@Override
	public void runCalc(String calcName) throws Exception {
		for (EssScript script : cube.getCalcScripts()) {
			if (script.getName().equals(calcName)) {
				script.execute();
				return;
			}
		}
		throw new EssGridException("No such calc script: " + calcName, null);
	}

	@Override
	public void updateCubeViewProperties(AdhocOptions adhocOptions) {
		cubeView.setPreferences(new EssCubeView.GridPreferences(
				toRestIndentation(adhocOptions.getIndentation()),
				adhocOptions.isSuppressMissingRows(),
				adhocOptions.isSuppressZeroRows(),
				adhocOptions.isSuppressUnderscores(),
				adhocOptions.isRepeatMemberLabels(),
				toRestZoomInPreference(adhocOptions.getZoomInPreference()),
				adhocOptions.isIncludeSelection(),
				adhocOptions.isWithinSelectedGroup(),
				adhocOptions.isRemoveUnselectedGroup()));
	}

	private static EssCubeView.Indentation toRestIndentation(AdhocOptions.Indentation indentation) {
		return switch (indentation) {
		case NONE -> EssCubeView.Indentation.NONE;
		case TOTALS -> EssCubeView.Indentation.TOTALS;
		case SUBITEMS -> EssCubeView.Indentation.SUBITEMS;
		};
	}

	// AdhocOptions.ZoomInPreference has four more values than the REST wire format can express
	// (SIBLING_LEVEL, SAME_LEVEL, SAME_GENERATION, FORMULAS are JAPI-only) - getSupportedOptions()
	// keeps those out of reach in the dialog for this backend, so this is never actually asked to
	// map one of them, but it needs a defined fallback rather than an incomplete switch.
	private static EssCubeView.ZoomInPreference toRestZoomInPreference(AdhocOptions.ZoomInPreference preference) {
		return switch (preference) {
		case ALL_LEVELS -> EssCubeView.ZoomInPreference.ALL_LEVELS;
		case BOTTOM_LEVEL -> EssCubeView.ZoomInPreference.BOTTOM_LEVEL;
		default -> EssCubeView.ZoomInPreference.NEXT_LEVEL;
		};
	}

	@Override
	public EnumSet<AdhocOptionCapability> getSupportedOptions() {
		return EnumSet.of(
				AdhocOptionCapability.INDENTATION,
				AdhocOptionCapability.SUPPRESS_MISSING_ROWS,
				AdhocOptionCapability.SUPPRESS_ZERO_ROWS,
				AdhocOptionCapability.SUPPRESS_UNDERSCORE_ROWS,
				AdhocOptionCapability.REPEAT_MEMBER_LABELS,
				AdhocOptionCapability.ZOOM_IN_NEXT_LEVEL,
				AdhocOptionCapability.ZOOM_IN_ALL_LEVELS,
				AdhocOptionCapability.ZOOM_IN_BOTTOM_LEVEL,
				AdhocOptionCapability.INCLUDE_SELECTION,
				AdhocOptionCapability.WITHIN_SELECTED_GROUP,
				AdhocOptionCapability.REMOVE_UNSELECTED_GROUP);
	}

	@Override
	public List<String> getDimensionNames() {
		List<String> names = new ArrayList<>();
		for (EssDimension dimension : cube.getDimensions()) {
			names.add(dimension.getName());
		}
		return names;
	}

	@Override
	public EssMemberNode getDimensionRoot(String dimensionName) {
		return new RestMemberNode(cube.getMember(dimensionName));
	}

	@Override
	public void setMembers(List<MemberPlacement> placements) {
		List<EssCubeView.MemberPlacement> restPlacements = new ArrayList<>();
		for (MemberPlacement placement : placements) {
			restPlacements.add(new EssCubeView.MemberPlacement(
					placement.getPoint().getRow(), placement.getPoint().getCol(), placement.getMemberName()));
		}
		cubeView.setMembers(restPlacements);
	}

}
