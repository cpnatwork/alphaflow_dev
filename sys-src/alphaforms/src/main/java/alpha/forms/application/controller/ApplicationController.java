/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.application.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import alpha.forms.AlphaFormsFacade;
import alpha.forms.application.view.AlphaFormsView;
import alpha.forms.application.view.panel.FormView;
import alpha.forms.clipboard.view.ClipboardView;
import alpha.forms.designer.view.DesignerView;
import alpha.forms.form.AlphaForm;
import alpha.forms.form.util.AlphaFormProvider;
import alpha.forms.form.validation.ValidatorFactory;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.SwitchViewSignal;
import alpha.forms.util.FormSaveListener;

/**
 * The Class ApplicationController.
 */
public class ApplicationController implements AlphaFormsFacade, Subscriber {

	/** The current view. */
	FormView currentView;

	/** The previous view. */
	FormView previousView;

	/** The form. */
	AlphaForm form;

	/** The parent. */
	AlphaFormsView parent;

	/** The save listeners. */
	protected Set<FormSaveListener> saveListeners = new HashSet<FormSaveListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#start()
	 */
	@Override
	public AlphaFormsView start() {
		this.parent = new AlphaFormsView();
		ValidatorFactory.setup();
		this.form = new AlphaForm();
		this.start(this.parent, this.form, false);
		return this.parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#start(java.io.InputStream)
	 */
	@Override
	public AlphaFormsView start(final InputStream forminput) {
		if (forminput == null)
			return null;
		this.parent = new AlphaFormsView();
		ValidatorFactory.setup();
		final PersistenceController p = new PersistenceController(this);
		final AlphaForm alphaForm = p.loadForm(forminput);
		this.form = alphaForm;
		this.start(this.parent, alphaForm, this.form.getWidgets().size() > 0);
		return this.parent;
	}

	/**
	 * Start.
	 * 
	 * @param parent
	 *            the parent
	 * @param form
	 *            the form
	 * @param showClipboard
	 *            the show clipboard
	 */
	protected void start(final AlphaFormsView parent, final AlphaForm form,
			final boolean showClipboard) {
		this.parent = parent;

		AlphaFormProvider.setForm(form);

		if (showClipboard) {
			this.currentView = new ClipboardView(form, this);
		} else {
			this.currentView = new DesignerView(form, this);
		}
		this.currentView.show(parent);

		SignalManager.getInstance().subscribeSink(this, "designer");
		SignalManager.getInstance().subscribeSink(this, "clipboard");
		SignalManager.getInstance().subscribeSink(this, "application");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#registerSaveListener(alpha.forms.util.
	 * FormSaveListener)
	 */
	@Override
	public void registerSaveListener(final FormSaveListener l) {
		this.saveListeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.signal.model.Subscriber#signalReceived(alpha.forms.signal
	 * .model.Signal)
	 */
	@Override
	public void signalReceived(final Signal s) {
		if (s instanceof SwitchViewSignal) {
			final SwitchViewSignal v = (SwitchViewSignal) s;
			if (v.getToView() == SwitchViewSignal.VIEWER) {
				this.currentView.pauseView();
				this.previousView = this.currentView;
				this.currentView = new ClipboardView(this.form, this);
				this.currentView.show(this.parent);
			} else if (v.getToView() == SwitchViewSignal.DESIGNER) {
				this.currentView.pauseView();
				this.form.revertToState("clipboardStart");
				this.currentView = new DesignerView(this.form, this);
				this.currentView.show(this.parent);
			}
			this.parent.updateUI();
		}
	}

	/**
	 * Persist form.
	 */
	public void persistForm() {
		this.persistForm(null);
	}

	/**
	 * Persist form.
	 * 
	 * @param state
	 *            the state
	 */
	public void persistForm(final String state) {
		final PersistenceController p = new PersistenceController(this);
		p.storeForm(this.form, state);
	}

	/**
	 * Fire save event.
	 * 
	 * @param data
	 *            the data
	 */
	public void fireSaveEvent(final ByteArrayOutputStream data) {
		for (final FormSaveListener l : this.saveListeners) {
			l.save(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#setDocumentStates(java.util.Collection)
	 */
	@Override
	public void setDocumentStates(final Collection<String> states) {
		if (this.form != null) {
			this.form.addDocumentStates(states);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#addDocumentState(java.lang.String)
	 */
	@Override
	public void addDocumentState(final String state) {
		if (this.form != null) {
			this.form.addDocumentState(state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#getView()
	 */
	@Override
	public AlphaFormsView getView() {
		return this.parent;
	}

}
