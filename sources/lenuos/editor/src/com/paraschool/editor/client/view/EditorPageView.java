package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.paraschool.editor.client.presenter.Menu;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public class EditorPageView extends PageView  implements com.paraschool.editor.client.presenter.PagePresenter.Display{

	protected interface EditorPageViewCssResource extends CssResource {
		String arrowChoixInteractivite();
		String edit();
		
		String enonce();
		String enonceHead();
		String enonceMiddle();
		String enonceBottom();
	}
	
	public interface Resources extends PageView.Resources, AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source(value={"css/EditorPageView.css","css/Constants.css"}) EditorPageViewCssResource editCss();
		
		@Source("images/arrow_choixInteractivite.png") ImageResource arrowChoixInteractivite();
		@Source("images/enonce_pg_bgHaut.png") ImageResource enonceHead();
		@Source("images/enonce_pg_bgMil.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource enonceMiddle();
		@Source("images/enonce_pg_bgBas.png") ImageResource enonceBottom();

	}
	{
		Resources.INSTANCE.editCss().ensureInjected();
	}
	
	protected final PageMenu menu;
	
	@Inject
	protected EditorPageView() {
		super();
		container.addStyleName(Resources.INSTANCE.editCss().edit());
		
		menu = new PageMenu();
		HTML clear = new HTML();
		clear.setStyleName(Resources.INSTANCE.appCss().clear());
		container.add(menu);
		container.add(clear);
		
		menu.addStyleName(Resources.INSTANCE.appCss().flotL());
	}

	public Menu getMenu() {
		return menu;
	}
	
}
