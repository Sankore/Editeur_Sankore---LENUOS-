package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel.MoverCallback;

/*
 * Created at 18 oct. 2010
 * By bathily
 */
public class Blocs extends FlexTable {

	protected static final String ID_PREFIX="B";
	
	private String idPrefix = ID_PREFIX;
	
	public interface AddBlocCallback {
		void onAddBloc(int column, Blocs blocs);
	}
	
	public interface RemoveBlocCallback {
		void onRemoveBloc(int column, Blocs blocs);
	}
	
	public interface MoveBlocCallback {
		void onMove(int from, int to);
	}
	
	class BlocMoverCallback implements MoverCallback {

		final int cellIndex;
		public BlocMoverCallback(ClickEvent event) {
			cellIndex = getCellForEvent(event).getCellIndex();
		}

		@Override
		public void onMove(int source, int destination) {
			if(destination >= 0 && destination < getCellCount(0)) {
				int previousIndex = cellIndex;
				
				for(int i=1 ; i<getRowCount(); i++) {
					Widget previous = getWidget(i, previousIndex);
					Widget dest = getWidget(i, destination);
					
					setWidget(i, previousIndex, dest);
					setWidget(i, destination, previous);
					
				}
				if(moveBlocCallback != null)
					moveBlocCallback.onMove(previousIndex, destination);
			}
		}
		
	}
	
	private AddBlocCallback addBlocCallback;
	private RemoveBlocCallback removeBlocCallback;
	private MoveBlocCallback moveBlocCallback;
	private Button addBlocButton;
	
	private int maxBloc;
	private boolean canRemoveBloc = true;
	
	private String headStyleName = "bloc-head";
	private String coreStyleName = "bloc-core";
	private String bottomStyleName = "bloc-bottom";
	
	public Blocs() {
		this(1, -1);
	}
	
	public Blocs(int row, int maxBloc) {
		super();
		setMaxBloc(maxBloc);
		setRowCount(row);
		setCellPadding(0);
		setBorderWidth(0);
	}
	
	public Blocs(int row, int maxBloc, Button addBlocButton) {
		this(row, maxBloc);
		setAddBlocButton(addBlocButton);
	}
	
	public void setCanRemoveBloc(boolean canRemoveBloc) {
		this.canRemoveBloc = canRemoveBloc;
	}
	
	public void setRowCount(int row) {
		removeAllRows();
		for(int i=0 ; i<row ; i++)
			insertRow(i);
	}
	
	public void setMaxBloc(int maxBloc) {
		this.maxBloc = maxBloc;
	}

	public int getMaxBloc() {
		return maxBloc;
	}

	public void setHeadStyleName(String headStyleName) {
		this.headStyleName = headStyleName;
	}

	public String getHeadStyleName() {
		return headStyleName;
	}

	public void setCoreStyleName(String coreStyleName) {
		this.coreStyleName = coreStyleName;
	}

	public String getCoreStyleName() {
		return coreStyleName;
	}

	public void setBottomStyleName(String bottomStyleName) {
		this.bottomStyleName = bottomStyleName;
	}

	public String getBottomStyleName() {
		return bottomStyleName;
	}

	public void setIdPrefix(String idPrefix) {
		this.idPrefix = idPrefix;
	}

	public String getIdPrefix() {
		return idPrefix;
	}

	public void setAddBlocButton(final Button addBlocButton) {
		this.addBlocButton = addBlocButton;
		this.addBlocButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addBloc();
			}
		});
		
	}
	
	public void prepareBlocForEditing(int column) {
		final BlocHead head = new BlocHead(canRemoveBloc);
		head.number.setText(idPrefix+(column+1));
		
		head.number.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ContentMoverPopUpPanel(head.number, getCellForEvent(event).getCellIndex(),
						new BlocMoverCallback(event));
			}
		});
		
		head.remove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cell cell = Blocs.this.getCellForEvent(event);
				if(cell != null)
					removeBlocFromCell(cell);
			}
		});
		
		setWidget(0, column, head);
		
		if(addBlocButton != null && (maxBloc >-1 && maxBloc <= getCellCount(0)))
			addBlocButton.setEnabled(false);
	}
	
	public void addBloc() {
		final int count = getCellCount(0);
		prepareBlocForEditing(count);
		if(addBlocCallback != null)
			addBlocCallback.onAddBloc(count, this);
	}
	
	private void removeBlocFromCell(Cell cell) {
		int cellIndex = cell.getCellIndex();
		
		for(int i=0 ; i<getRowCount() ; i++) {
			removeCell(i, cellIndex);
		}
		
		
		for(int i=cellIndex ; i < getCellCount(0) ; i++) {
			BlocHead head = (BlocHead)getWidget(0, i);
			head.number.setText(idPrefix+(i+1));
		}
		
		if(removeBlocCallback != null)
			removeBlocCallback.onRemoveBloc(cell.getCellIndex(), this);
		
		if(addBlocButton != null && (maxBloc < 0 || maxBloc > getCellCount(0)))
			addBlocButton.setEnabled(true);
	}
	
	public void setAddBlocCallback(AddBlocCallback callback) {
		this.addBlocCallback = callback;
	}
	
	public void setRemoveBlocCallback(RemoveBlocCallback callback) {
		this.removeBlocCallback = callback;
	}
	
	public void setMoveBlocCallback(MoveBlocCallback callback) {
		this.moveBlocCallback = callback;
	}


	@Override
	public void setWidget(int row, int column, Widget widget) {
		super.setWidget(row, column, widget);
		String style = coreStyleName;
		if(row == 0) style = headStyleName;
		else if(row == getRowCount()-1) style = bottomStyleName;
		getCellFormatter().addStyleName(row, column, style);
	}
	
	
	
}
