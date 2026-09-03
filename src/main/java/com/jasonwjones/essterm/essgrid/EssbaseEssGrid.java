package com.jasonwjones.essterm.essgrid;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essbase.api.base.EssException;
import com.essbase.api.base.IEssIterator;
import com.essbase.api.dataquery.IEssCell;
import com.essbase.api.dataquery.IEssCell.EEssCellType;
import com.essbase.api.dataquery.IEssCubeView;
import com.essbase.api.dataquery.IEssCubeView.EEssIndentStyle;
import com.essbase.api.dataquery.IEssDataCell;
import com.essbase.api.dataquery.IEssDataCell.EEssDataCellType;
import com.essbase.api.dataquery.IEssGridView;
import com.essbase.api.dataquery.IEssMemberCell;
import com.essbase.api.dataquery.IEssOpKeepOnly;
import com.essbase.api.dataquery.IEssOpPivot;
import com.essbase.api.dataquery.IEssOpRemoveOnly;
import com.essbase.api.dataquery.IEssOpRetrieve;
import com.essbase.api.dataquery.IEssOpUpdate;
import com.essbase.api.dataquery.IEssOpZoomIn;
import com.essbase.api.dataquery.IEssOpZoomOut;
import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssOlapFileObject;
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
import com.saxifrages.essbase.util.ConversionDelegate;
import com.saxifrages.essbase.util.IteratorUtil;

class EssbaseEssGrid implements EssGrid {

	private static final Logger logger = LoggerFactory.getLogger(EssbaseEssGrid.class);

	private IEssCube cube;

	private IEssCubeView cubeView;

	private ChosenConnection connection;
	
	public void updateCubeViewProperties(AdhocOptions adhocOptions) throws Exception {
		// this.adhocOptions = adhocOptions;

		cubeView.setAliasNames(adhocOptions.isUseAliases());

		switch (adhocOptions.getIndentation()) {
		case NONE:
			cubeView.setIndentStyle(EEssIndentStyle.NONE);
			break;
		case SUBITEMS:
			cubeView.setIndentStyle(EEssIndentStyle.SUB_ITEMS);
			break;
		case TOTALS:
			cubeView.setIndentStyle(EEssIndentStyle.TOTALS);
			break;
		default:
			break;
		}
		cubeView.updatePropertyValues();
	}

	public EssbaseEssGrid(ChosenConnection connection, IEssCube cube) throws EssGridException {
		this.connection = connection;
		this.cube = cube;
		try {
			this.cubeView = cube.openCubeView("Essterm");
		} catch (EssException e) {
			throw new EssGridException("Problem opening grid view", e);
		}
	}
	
	@Override
	public ChosenConnection getConnection() {
		return connection;
	}

	@Override
	public void retrieve() throws EssGridException {
		try {
			IEssOpRetrieve retrieve = cubeView.createIEssOpRetrieve();
			cubeView.performOperation(retrieve);
		} catch (EssException e) {
			throw new EssGridException("Problem retrieving", e);
		}
	}

	@Override
	public void resetToDefault() {
		throw new UnsupportedOperationException("Resetting to the default view is only supported via the REST backend for now");
	}

	@Override
	public void zoomIn(Point point) {
		try {
			IEssOpZoomIn zoomIn = cubeView.createIEssOpZoomIn();
			zoomIn.addCell(point.getRow(), point.getCol());
			// EEssZoomInPreference.
			cubeView.performOperation(zoomIn);
		} catch (EssException e) {
			throw new EssGridException("Problem zooming in", e);
		}
	}

	@Override
	public void zoomIn(Point point, EnumSet<ZoomOptions> zoomOptions) throws Exception {
		boolean needsToSetIncludeBackToFalse = false;
		if (zoomOptions.contains(ZoomOptions.INCLUDE_SELECTION)) {
			if (!cubeView.isIncludeSelection()) {
				needsToSetIncludeBackToFalse = true;
			}
			cubeView.setIncludeSelection(true);
			cubeView.updatePropertyValues();
		}
		zoomIn(point);

		if (needsToSetIncludeBackToFalse) {
			cubeView.setIncludeSelection(false);
			cubeView.updatePropertyValues();
		}
	}

	@Override
	public void zoomOut(Point point) {
		try {
			IEssOpZoomOut zoomOut = cubeView.createIEssOpZoomOut();
			zoomOut.addCell(point.getRow(), point.getCol());
			cubeView.performOperation(zoomOut);
		} catch (EssException e) {
			throw new EssGridException("Problem zooming out", e);
		}
	}

	@Override
	public void keepOnly(Point point) {
		try {
			IEssOpKeepOnly keepOnly = cubeView.createIEssOpKeepOnly();
			keepOnly.addCell(point.getRow(), point.getCol());
			cubeView.performOperation(keepOnly);
		} catch (EssException e) {
			throw new EssGridException("Problem keeping only", e);
		}
	}

	@Override
	public void removeOnly(Point point) {
		try {
			IEssOpRemoveOnly removeOnly = cubeView.createIEssOpRemoveOnly();
			removeOnly.addCell(point.getRow(), point.getCol());
			cubeView.performOperation(removeOnly);
		} catch (EssException e) {
			throw new EssGridException("Problem removing only", e);
		}
	}

	@Override
	public void pivot(Point start, Point end) {
		try {
			IEssOpPivot pivot = cubeView.createIEssOpPivot();
			pivot.set(start.getRow(), start.getCol(), end.getRow(), end.getCol());
			cubeView.performOperation(pivot);
		} catch (EssException e) {
			throw new EssGridException("Problem pivoting", e);
		}
	}

	@Override
	public Grid<EssCell> getGrid() throws EssGridException {
		try {
			IEssGridView gridView = cubeView.getGridView();
			Grid<EssCell> grid = new BasicGrid<>(gridView.getCountRows(), gridView.getCountColumns());

			for (int row = 0; row < gridView.getCountRows(); row++) {
				for (int col = 0; col < gridView.getCountColumns(); col++) {
					IEssCell sourceCell = gridView.getCell(row, col);
					EssCell cell = createCell(sourceCell);
					grid.setCellData(row, col, cell);
				}
			}
			return grid;
		} catch (EssException e) {
			throw new EssGridException("Problem creating grid view", e);
		}
	}

	private EssCell createCell(IEssCell sourceCell) throws EssException {
		EEssCellType cellType = sourceCell.getCellType();
		if (cellType.equals(EEssCellType.DATA)) {
			IEssDataCell dataCell = (IEssDataCell) sourceCell;
			EEssDataCellType dataCellType = dataCell.getDataCellType();
			if (dataCellType.equals(EEssDataCellType.MISSING)) {
				return new MissingCell();
			} else if (dataCellType.equals(EEssDataCellType.DOUBLE)) {
				return new DoubleEssCell(dataCell.getDoubleValue());
			}
		} else if (cellType.equals(EEssCellType.MEMBER)) {
			IEssMemberCell memberCell = (IEssMemberCell) sourceCell;
			return new SimpleEssCell(memberCell.getMemberName());
		}
		return new SimpleEssCell("");
	}

	@Override
	public Collection<String> getCalcScripts() throws EssGridException {
		try {
			IEssIterator calcScriptIterator = cube.getOlapFileObjects(IEssOlapFileObject.TYPE_CALCSCRIPT);
			logger.info("There are {} calc scripts", calcScriptIterator.getCount());
			return IteratorUtil.iteratorToList(calcScriptIterator,
					new ConversionDelegate<IEssOlapFileObject, String>() {
						@Override
						public String convert(IEssOlapFileObject from) throws EssException {
							return from.getName();
						}
					});
		} catch (EssException e) {
			throw new EssGridException("Error fetching calc scripts", e);
		}
	}

	@Override
	public void runCalc(String calcName) throws Exception {
		logger.info("Calc {}", calcName);
		cube.calculate(false, calcName);
		logger.info("Finished");
	}

	@Override
	public void setData(Point point, double value) throws Exception {
		IEssOpUpdate update = cubeView.createIEssOpUpdate();
		IEssGridView gridView = cubeView.getGridView();
		gridView.setValue(point.getRow(), point.getCol(), value);
		cubeView.performOperation(update);
	}

	@Override
	public void clearData(Point point) throws Exception {
		IEssOpUpdate update = cubeView.createIEssOpUpdate();
		IEssGridView gridView = cubeView.getGridView();
		gridView.setCellContentType(point.getRow(), point.getCol(), IEssGridView.CELL_CONTENT_TYPE_MISSING);
		cubeView.performOperation(update);
	}

	@Override
	public List<String> getDimensionNames() {
		throw new UnsupportedOperationException("Member selection is only supported via the REST backend for now");
	}

	@Override
	public EssMemberNode getDimensionRoot(String dimensionName) {
		throw new UnsupportedOperationException("Member selection is only supported via the REST backend for now");
	}

	@Override
	public void setMembers(List<MemberPlacement> placements) {
		throw new UnsupportedOperationException("Member selection is only supported via the REST backend for now");
	}

}
