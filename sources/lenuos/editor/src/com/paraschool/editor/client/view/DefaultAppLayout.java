package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.event.app.AuthenticatedEvent;
import com.paraschool.editor.client.event.app.AuthenticationEvent;
import com.paraschool.editor.client.event.app.AuthenticationEventHandler;
import com.paraschool.editor.client.event.app.ErrorMessageEvent;
import com.paraschool.editor.client.event.app.InfoMessageEvent;
import com.paraschool.editor.client.event.app.NotificationEvent;
import com.paraschool.editor.client.event.app.NotificationEventHandler;
import com.paraschool.editor.client.event.app.ParametersLoadedEvent;
import com.paraschool.editor.client.event.app.WarningMessageEvent;
import com.paraschool.editor.client.event.appmenu.DisconnectRequestEvent;
import com.paraschool.editor.client.event.appmenu.ExportAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.PreviewAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.PublishAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.QuitAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.SaveAppMenuEvent;
import com.paraschool.editor.client.event.project.ClosedProjectEvent;
import com.paraschool.editor.client.event.project.OpenedProjectEvent;
import com.paraschool.editor.client.event.project.StateProjectEvent;
import com.paraschool.editor.client.event.project.StateProjectEventHandler;
import com.paraschool.editor.client.i18n.AppConstants;

/*
 * Created at 9 juil. 2010
 * By Didier Bathily
 */
public class DefaultAppLayout extends Composite implements AppLayout {

	private static DefaultAppLayoutUiBinder uiBinder = GWT.create(DefaultAppLayoutUiBinder.class);
	interface DefaultAppLayoutUiBinder extends UiBinder<Widget, DefaultAppLayout> {}
	
	interface AppLayoutCssResource extends CssResource {
		
		String menuAnimationConstraint();
		
		String padre();
		String logo();
		String header();
		String loading();
		String headerMenu();
		String headerOne();
		String headerTwo();
		String login();
		String navigOne();
		String advice();
		String disconnect();
		
		String notification();
		String info();
		String warning();
		String error();
		
		String content();
		String logger();
		String disabled();
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/AppLayout.css","css/Constants.css"}) AppLayoutCssResource css();
		@Source("images/bg_body.png") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource body();
		@Source("images/header.jpg") ImageResource header();
		@Source("images/black-loading.gif") ImageResource blackLoading();
		@Source("images/disconnect.png") ImageResource disconnect();
	}

	@UiField Panel logger;
	
	@UiField FlowPanel root;
	@UiField FlowPanel head;
	@UiField HTML user;
	@UiField FlowPanel content;
	@UiField LayoutPanel menuPanel;
	@UiField AppMenu menu;
	@UiField HTML advice;
	@UiField HTMLPanel notification;
	@UiField InlineHTML notificationContent;
	@UiField Button disconnect;
	
	private final Timer adviceSwitcher;
	private final Timer notificationHidder;
	
	private final AppConstants constants;
	
	@Inject
	private DefaultAppLayout(final EventBus eventBus, final AppConstants constants) {
		initWidget(uiBinder.createAndBindUi(this));
		
		try{
			logger.setVisible(Boolean.parseBoolean(Location.getParameter("log")));
		}catch (NumberFormatException e) {
			logger.setVisible(false);
		}

		this.constants = constants;

		menuPanel.setWidgetTopHeight(notification, -Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.setWidgetTopHeight(advice, -Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.setWidgetTopHeight(menu, Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.forceLayout();
		
		adviceSwitcher = new Timer() {
			int cindex = -1;
			@Override
			public void run() {
				int length = constants.advices().length;
				int index = Random.nextInt(length);
				while(cindex != -1 && cindex == index)
					index = Random.nextInt(length);
				cindex = index;
				advice.setHTML(constants.advices()[cindex]);
			}
		};
		
		notificationHidder = new Timer() {
			@Override
			public void run() {
				hideNotification(null);
			}
		};

		adviceSwitcher.run();

		bindMenu(eventBus);
		bindNotification(eventBus);
		hideAppMenu();
		eventBus.addHandler(ParametersLoadedEvent.TYPE, new ParametersLoadedEvent.Handler() {
			@Override
			public void onLoad(ParametersLoadedEvent event) {
				if(event.getParameters().getCanPublish()) {
					menu.getPublishButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							eventBus.fireEvent(new PublishAppMenuEvent());
						}
					});
				}else {
					((Widget)menu.getPublishButton()).addStyleName(Resources.INSTANCE.css().disabled());
				}
			}
		});
	}
	
	private void bindMenu(final EventBus eventBus) {
		menu.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SaveAppMenuEvent());
			}
		});
		
		menu.getPreviewButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new PreviewAppMenuEvent());
			}
		});
		
		menu.getExportButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ExportAppMenuEvent());
			}
		});
		
		menu.getQuitButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new QuitAppMenuEvent(true));
			}
		});
		
		disconnect.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new DisconnectRequestEvent());
			}
		});
		
		eventBus.addHandler(AuthenticationEvent.TYPE, new AuthenticationEventHandler() {
			
			@Override
			public void onDeauthenticated(AuthenticationEvent event) {
				disconnect.setEnabled(false);
				disconnect.setVisible(false);
				user.setVisible(false);
				user.setText(null);
			}
			
			@Override
			public void onAuthenticated(AuthenticatedEvent event) {
				disconnect.setEnabled(true);
				disconnect.setVisible(true);
				user.setVisible(true);
				user.setText(event.getAuthor().getName());
			}
		});
		
		eventBus.addHandler(StateProjectEvent.TYPE, new StateProjectEventHandler() {
			
			@Override
			public void onOpened(OpenedProjectEvent event) {
				showAppMenu();
			}
			
			@Override
			public void onClosed(ClosedProjectEvent event) {
				hideAppMenu();
			}
		});
	}
	
	private void bindNotification(final EventBus eventBus) {
		eventBus.addHandler(NotificationEvent.TYPE, new NotificationEventHandler() {
			
			private void setAndShow(final String message, final String css) {
				notificationContent.setStyleName(css);
				notificationContent.setHTML(message);
				showNotification();
			}
			
			private void setAndShow(final NotificationEvent event, final String css) {
				hideNotification(new AnimationCallback() {
					public void onLayout(Layer layer, double progress) {}
					public void onAnimationComplete() {
						setAndShow(event.getMessage(), css);
					}
				});
				
			}

			public void onWarning(WarningMessageEvent event) {
				setAndShow(event,Resources.INSTANCE.css().warning());
			}
			
			public void onInfo(InfoMessageEvent event) {
				setAndShow(event,Resources.INSTANCE.css().info());
			}
			
			public void onError(ErrorMessageEvent event) {
				setAndShow(event,Resources.INSTANCE.css().error());
			}
		});
	}

	protected void showNotification() {
		menuPanel.setWidgetTopHeight(notification, 0, Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.animate(constants.notificationAnimationDelay());
		notificationHidder.schedule(constants.notificationHideDelay());
	}
	
	protected boolean isNotificationVisible() {
		return notification.getAbsoluteTop() > 0;
		
	}
	
	protected void hideNotification(AnimationCallback callback) {

		if(isNotificationVisible()){ // L'animation n'est pas lancé lorsqu'il n'y pas de déplacement, donc pas de callback. Il faut donc le lancer manuellement si la notification n'est pas déjà disponible
			notificationHidder.cancel();
			menuPanel.setWidgetTopHeight(notification, -Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
			if(callback != null)
				 menuPanel.animate(constants.menuAnimationDelay(), callback);
			else
				menuPanel.animate(constants.menuAnimationDelay());
		}else if(callback != null) callback.onAnimationComplete();
		
	}

	protected void hideAppMenu() {
		menuPanel.setWidgetTopHeight(menu, Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.setWidgetTopHeight(advice, 0, Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.animate(constants.menuAnimationDelay());
		adviceSwitcher.scheduleRepeating(constants.adviceSwitchDelay());
	}

	protected void showAppMenu() {
		menuPanel.setWidgetTopHeight(menu, 0, Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.setWidgetTopHeight(advice, -Resources.INSTANCE.appCss().menuHeight(), Unit.PX, Resources.INSTANCE.appCss().menuContentHeight(), Unit.PX);
		menuPanel.animate(constants.menuAnimationDelay());
		adviceSwitcher.cancel();
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.client.view.AppLayout#getContent()
	 */
	@Override
	public FlowPanel getContent() {
		return content;
	}

	@Override
	public Panel getLoggingPanel() {
		return logger;
	}

}
