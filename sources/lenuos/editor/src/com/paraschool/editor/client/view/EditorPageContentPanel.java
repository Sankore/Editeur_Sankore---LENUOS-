package com.paraschool.editor.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.paraschool.editor.client.view.EditorPageView.EditorPageViewCssResource;

public class EditorPageContentPanel extends PageContentPanel {

	protected final HTML advice;
	protected FlowPanel enonceParent;
	
	public EditorPageContentPanel() {
		super();
		
		HTML head = new HTML();
		HTML bottom = new HTML();
		enonceParent = (FlowPanel) enonce.getParent();
		enonceParent.insert(head,0);
		enonceParent.add(bottom);
		
		EditorPageViewCssResource css = EditorPageView.Resources.INSTANCE.editCss();
		enonceParent.setStyleName(css.enonce());
		head.setStyleName(css.enonceHead());
		enonce.setStyleName(css.enonceMiddle());
		bottom.setStyleName(css.enonceBottom());
		
		enonceParent.setVisible(false);
		
		advice = new HTML();
		advice.setStyleName(css.arrowChoixInteractivite());
		add(advice);
	}

	public void showEnonce(boolean show) {
		//enonceParent.setVisible(show);
		advice.setVisible(!show);
	}
	
}
