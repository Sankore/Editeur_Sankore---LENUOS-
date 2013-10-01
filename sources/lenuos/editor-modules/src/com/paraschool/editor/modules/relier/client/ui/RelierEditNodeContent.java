package com.paraschool.editor.modules.relier.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel.MoverCallback;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableHTML;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.editor.modules.commons.client.ui.edit.MediaButtonFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.Vignette;
import com.paraschool.editor.modules.commons.client.ui.edit.Vignette.RemoveCallback;
import com.paraschool.editor.modules.relier.client.i18n.RelierModuleMessages;
import com.paraschool.editor.modules.relier.client.jso.RelierNodeContentJSO;

/*
 * Created at 16 oct. 2010
 * By bathily
 */
public class RelierEditNodeContent extends Composite implements NodeContent {

	public static final int RIGHT_BLOC_COLUMN = 3;
	public static final int LEFT_BLOC_COLUMN = 0;
	
	private static RelierEditNodeContentUiBinder uiBinder = GWT.create(RelierEditNodeContentUiBinder.class);
	interface RelierEditNodeContentUiBinder extends UiBinder<Widget, RelierEditNodeContent> {}
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String left();
		String right();
		String bloc();
		String pin();
	}
	
	private final EventBus eventBus;
	private final EditModuleContext context;
	private final RelierModuleMessages messages;
	
	private final MoverCallback leftVignettesMoverCallback, rightVignettesMoverCallback; 
	private final RemoveCallback leftVignettesRemoveCallback, rightVignettesRemoveCallback;
	
	@UiField Button showStatementButton;
	@UiField Button hideStatementButton;
	@UiField Panel statementWrapper;
	@UiField Panel statementContainer;
	
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	
	@UiField(provided=true) Button addResource = MediaButtonFactory.createAddMediaButton();
	@UiField(provided=true) Button addSound = MediaButtonFactory.createAddSoundButton();
	@UiField(provided=true) Button addText = MediaButtonFactory.createAddTextButton();
	
	@UiField Button addToLeft;
	@UiField Button addToRight;
	
	@UiField FlexTable blocs;
	@UiField CssResource css;
	
	int nleft = 0, nright = 0;
	
	public RelierEditNodeContent(EventBus eventBus, EditModuleContext context, RelierModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		
		leftVignettesMoverCallback = new MoverCallback() {
			@Override
			public void onMove(int source, int destination) {
				if(destination >= 0 && destination < nleft) {
					moveBloc(source, destination, LEFT_BLOC_COLUMN);
				}
			}
		};
		
		rightVignettesMoverCallback = new MoverCallback() {
			@Override
			public void onMove(int source, int destination) {
				if(destination >= 0 && destination < nleft) {
					moveBloc(source, destination, RIGHT_BLOC_COLUMN);
				}
			}
		};
		
		leftVignettesRemoveCallback = new RemoveCallback() {
			
			@Override
			public void remove(Vignette v) {
				int i=v.getId()+1;
				for(; i<nleft ; i++) {
					removeBloc(i, LEFT_BLOC_COLUMN);
				}
				nleft--;
				if(nleft == nright)
					blocs.removeRow(i-1);
				else {
					blocs.clearCell(i-1, LEFT_BLOC_COLUMN);
					blocs.clearCell(i-1, LEFT_BLOC_COLUMN+1);
				}
			}
		};
		
		rightVignettesRemoveCallback = new RemoveCallback() {
			
			@Override
			public void remove(Vignette v) {
				int i=v.getId()+1;
				for(; i<nright ; i++) {
					removeBloc(i, RIGHT_BLOC_COLUMN);
				}
				nright--;
				if(nleft == nright)
					blocs.removeRow(i-1);
				else {
					blocs.clearCell(i-1, RIGHT_BLOC_COLUMN-1);
					blocs.clearCell(i-1, RIGHT_BLOC_COLUMN);
				}
			}
		};
		
		initBlocs();
	}
	
	private void moveBloc(int source, int destination, int col) {
		SimplePanel spanel = (SimplePanel)blocs.getWidget(source, col);
		SimplePanel dpanel = (SimplePanel)blocs.getWidget(destination, col);
		Vignette vsource = (Vignette) spanel.getWidget();
		Vignette vdestination = (Vignette) dpanel.getWidget();
		spanel.setWidget(vdestination);
		dpanel.setWidget(vsource);
		vsource.setId(destination);
		vdestination.setId(source);
	}
	
	private void removeBloc(int i, int col) {
		SimplePanel dpanel = (SimplePanel)blocs.getWidget(i-1, col);
		SimplePanel spanel = (SimplePanel)blocs.getWidget(i, col);
		Vignette vsource = (Vignette) spanel.getWidget();
		vsource.setId(i-1);
		dpanel.setWidget(vsource);
	}
	
	private void initBlocs() {
		blocs.insertRow(0);
		blocs.setWidget(0, LEFT_BLOC_COLUMN, createBlocWidget(addToLeft));
		blocs.setWidget(0, LEFT_BLOC_COLUMN+1, createPin(null, LEFT_BLOC_COLUMN));
		blocs.setWidget(0, RIGHT_BLOC_COLUMN-1, createPin(null, RIGHT_BLOC_COLUMN));
		blocs.setWidget(0, RIGHT_BLOC_COLUMN, createBlocWidget(addToRight));
		addToLeft.setVisible(true);
		addToRight.setVisible(true);
	}
	
	private SimplePanel createBlocWidget(Widget w) {
		SimplePanel panel = new SimplePanel();
		panel.addStyleName(css.bloc());
		panel.add(w);
		return panel;
	}
	
	private Pin createPin(Integer id, int col) {
		Pin p = new Pin(id);
		p.addStyleName(css.pin());
		p.addStyleName(col == LEFT_BLOC_COLUMN ? css.left() : css.right());
		return p;
	}
	
	private void checkRow() {
		if(nleft == nright)
			blocs.insertRow(nleft);
	}
	
	@UiHandler("addToLeft")
	protected void addToLeft(ClickEvent event) {
		addBlocToLeft(null);
	}
	
	private void addBlocToLeft(BlocJSO jso) {
		checkRow();
		if(nleft > nright && blocs.getWidget(nleft-1, LEFT_BLOC_COLUMN) != null){
			blocs.insertRow(nleft);
		}
		Vignette v = new Vignette(nleft, jso, eventBus, context, messages.cellIdPrefix(),
				leftVignettesMoverCallback, leftVignettesRemoveCallback);
		
		blocs.setWidget(nleft, LEFT_BLOC_COLUMN, createBlocWidget(v));
		blocs.setWidget(nleft, LEFT_BLOC_COLUMN+1, createPin(nleft, LEFT_BLOC_COLUMN));
		nleft++;
	}
	
	@UiHandler("addToRight")
	protected void addToRight(ClickEvent event) {
		addBlocToRight(null);
	}
	
	private void addBlocToRight(BlocJSO jso) {
		checkRow();
		if(nright > nleft && blocs.getWidget(nright-1, RIGHT_BLOC_COLUMN-1) != null){
			blocs.insertRow(nright);
		}
		Vignette v = new Vignette(nright, jso, eventBus, context, messages.cellIdPrefix(),
				rightVignettesMoverCallback, rightVignettesRemoveCallback);
		
		blocs.setWidget(nright, RIGHT_BLOC_COLUMN-1, createPin(nright, RIGHT_BLOC_COLUMN));
		blocs.setWidget(nright, RIGHT_BLOC_COLUMN, createBlocWidget(v));
		nright++;
	}
	
	@UiHandler(value={"showStatementButton","hideStatementButton"})
	protected void toogleStatement(ClickEvent event) {
		boolean toShow = event.getSource().equals(showStatementButton);
		if(toShow){
			statementWrapper.getElement().getStyle().setOpacity(1);
			statementWrapper.setHeight("auto");
		}
			
		else{
			statementWrapper.getElement().getStyle().setOpacity(0);
			statementWrapper.setHeight("0");
		}
			
		showStatementButton.setVisible(!toShow);
		hideStatementButton.setVisible(toShow);
	}
	
	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);
		return this;
	}

	private void updateWidget(NodeJSO jso) {
		new EditableMedia(eventBus, context, resource, addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		new EditableMedia(eventBus, context, sound, addSound, null, ModuleObject.Type.SOUND);
		
		RelierNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) sound.setMedia(temp, null);
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
				
				if(text.getText().trim().length() != 0 || sId != null || rId != null)
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
							showStatementButton.click();
						}
					});
			}
			
			final JsArray<BlocJSO> leftBlocs = contentJSO.getLeftBlocs();
			final JsArray<BlocJSO> rightBlocs = contentJSO.getRightBlocs();
			addBlocs(leftBlocs, LEFT_BLOC_COLUMN);
			addBlocs(rightBlocs, RIGHT_BLOC_COLUMN);
			
		}
		else{
			addBloc(null,LEFT_BLOC_COLUMN);
			addBloc(null,RIGHT_BLOC_COLUMN);
		}
			
		
		new EditableHTML(eventBus, context, text, addText);
		
		if(!ModuleUtils.canAddOrRemoveContent(context)) {
			showStatementButton.setVisible(false);
			addToLeft.setEnabled(false);
			addToRight.setEnabled(false);
		}
	}

	private void addBlocs(final JsArray<BlocJSO> jsos, final int col) {
		if(jsos != null && jsos.length() > 0)
			Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
				int i=0;
				@Override
				public boolean execute() {
					addBloc(jsos.get(i), col);
					return ++i < jsos.length();
				}
			});
		else
			addBloc(null, col);
	}
	
	private void addBloc(final BlocJSO jso, final int col) {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				switch (col) {
				case LEFT_BLOC_COLUMN:
					addBlocToLeft(jso);
					break;
				case RIGHT_BLOC_COLUMN:
					addBlocToRight(jso);
					break;
				}
			}
		});
	}
	
	@Override
	public NodeContentJSO getJSO() {
		RelierNodeContentJSO jso = RelierNodeContentJSO.createObject().cast();
		
		StatementJSO statementJSO = StatementJSO.createObject().cast();
		
		statementJSO.setStatement(text.getHTML());
		
		ModuleObject soundObject = sound.getObject();
		statementJSO.setSound(soundObject == null ? null : soundObject.getId());
		
		ModuleObject resourceObject = resource.getObject();
		statementJSO.setResource(resourceObject == null ? null : resourceObject.getId());
		
		jso.setStatement(statementJSO);
		
		JsArray<BlocJSO> leftblocsJSO = BlocJSO.createArray().cast();
		jso.setLeftBlocs(leftblocsJSO);
		scanColumn(LEFT_BLOC_COLUMN, leftblocsJSO);
		
		JsArray<BlocJSO> rightblocsJSO = BlocJSO.createArray().cast();
		jso.setRightBlocs(rightblocsJSO);
		scanColumn(3, rightblocsJSO);
		
		return jso;
	}
	
	private void scanColumn(int col, JsArray<BlocJSO> blocsjso) {
		int max = col == LEFT_BLOC_COLUMN ? nleft : nright;
		for(int i = 0 ; i < max ; i++) {
			blocsjso.push(((Vignette)((SimplePanel)blocs.getWidget(i, col)).getWidget()).getJSO());
		}
	}
}
