/**************************************************************************
 * alpha-Flow: distributed case files in form of active documents
 * (supporting knowledge-driven ad-hoc processes in healthcare)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
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
 * $Id: ImportHandler.java 3786 2012-05-04 08:00:25Z uj32uvac $
 *************************************************************************/
package alpha.templates.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.logging.Logger;


import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.PSAPayload;
import alpha.model.template.AlphaTemplate;
import alpha.templates.exceptions.FilterException;
import alpha.templates.filters.APAFilter;
import alpha.templates.filters.AdornmentsFilter;
import alpha.templates.filters.CRAFilter;
import alpha.templates.filters.PSAFilter;
import alpha.templates.gui.wizard.TemplateWizard;
import alpha.templates.gui.wizard.WizardAdornmentNamesPresenter;
import alpha.templates.gui.wizard.WizardAlphaCardNamesPresenter;
import alpha.templates.gui.wizard.WizardContributorNamesPresenter;
import alpha.templates.gui.wizard.WizardFileChooser;
import alpha.templates.gui.wizard.WizardMessagePresenter;
import alpha.util.XmlBinder;

/**
 * Handles the import button behaviour. Merges the information from the existing
 * alpha doc with the template data
 */
public class ImportHandler {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = Logger
			.getLogger(ImportHandler.class.getName());

	/** The apf. */
	private final AlphaPropsFacade apf;

	/** The home path. */
	private final File pathFile;

	/** The wizard. */
	private TemplateWizard wizard;

	/** The template file. */
	private File templateFile;

	/**
	 * Instantiates a new import handler.
	 * 
	 * @param apf
	 *            the apf
	 * @param pathFile
	 *            the path file
	 */
	public ImportHandler(final AlphaPropsFacade apf, final File pathFile) {
		this.apf = apf;
		this.pathFile = pathFile;
	}

	/**
	 * Import template.
	 * 
	 */
	public final void importTemplate() {

		this.wizard = new TemplateWizard("Import Alpha-Template");

		final WizardMessagePresenter mp = new WizardMessagePresenter(
				this.wizard);
		mp.setWelcome(true);
		mp.print("Welcome to the Template-Merge-Import!");
		mp.setWelcome(false);

		this.templateFile = new WizardFileChooser(this.wizard, "Open").choose(
				this.pathFile,
				"  Choose location of the template you want to import:");

		AlphaTemplate at = null;
		XmlBinder xml;

		if (this.templateFile == null) {
			mp.print("No file chosen. Template Import failed!");
			ImportHandler.LOGGER.severe("user canceled import ");
			return;
		}

		if (this.templateFile.exists()) {
			xml = new XmlBinder();
			try {
				at = (AlphaTemplate) xml.load(new FileInputStream(
						this.templateFile), "alpha.model.template");
			} catch (final FileNotFoundException e) {
				mp.print("Could not find Template. Template Import failed!");
				ImportHandler.LOGGER.severe("Error: " + e);
				return;
			}

		} else {
			mp.print("Could not find Template. Template Import failed!");
			ImportHandler.LOGGER.warning("Template not found.");
			return;
		}
		if (at == null) {
			mp.print("Could not load file. Is it really a template? Template Import failed!");
			ImportHandler.LOGGER.severe("Could not load template from:"
					+ this.templateFile.getAbsolutePath());
			return;
		}

		final String homePath = this.apf
				.getJarFile()
				.getAbsolutePath()
				.substring(0,
						this.apf.getJarFile().getAbsolutePath().length() - 4)
				+ File.separator;

		ArrayList<AlphaCardDescriptor> acdlist = (ArrayList<AlphaCardDescriptor>) at
				.getDescriptorList();

		final List<AlphaCardDescriptor> completedList = new ArrayList<AlphaCardDescriptor>();

		try {
			final APAFilter apaFilter = new APAFilter(
					new WizardAdornmentNamesPresenter(this.wizard));
			final APAPayload newAPAPayload = apaFilter.mergeApa(
					this.apf.getAPA(), at.getApaPayload(),
					"Which custom Alpha-Adornment-Types do you want to add?");

			// set proper episode ids
			for (final AlphaCardID id : at.getPsaPayload().getListOfTodoItems()) {
				id.setEpisodeID(this.apf.getAlphaConfig().getMyEpisodeId());
			}
			for (final AlphaCardDescriptor acd : at.getDescriptorList()) {
				acd.getId().setEpisodeID(
						this.apf.getAlphaConfig().getMyEpisodeId());
			}

			final PSAFilter psaFilter = new PSAFilter(
					new WizardAlphaCardNamesPresenter(this.wizard));
			final PSAPayload newPsa = psaFilter.mergePsa(acdlist, this.apf
					.getPSA(), at.getPsaPayload(), this.apf.getAlphaDoc()
					.getEpisodeID());

			// creates list of new acds
			acdlist = (ArrayList<AlphaCardDescriptor>) psaFilter
					.createListOfAdditionalCards(acdlist, newPsa,
							this.apf.getPSA());

			final CRAFilter craFilter = new CRAFilter(
					new WizardContributorNamesPresenter(this.wizard, null),
					this.apf.getCRA());

			final CRAPayload newCRAPayload = craFilter.mergeCra(at
					.getCraPayload());

			// filter adornments of each alpha card descriptor
			acdlist = (ArrayList<AlphaCardDescriptor>) new AdornmentsFilter(
					newAPAPayload, new WizardAdornmentNamesPresenter(
							this.wizard), new WizardAlphaCardNamesPresenter(
							this.wizard), false).filterAdornments(acdlist);

			// add the new adornments to old alpha card descriptors
			final List<PrototypedAdornment> newAdornments = apaFilter
					.getUpdatedAdornments();
			this.updateOldDescriptors(newAdornments);

			// store new apa
			try {
				xml.store(newAPAPayload, homePath + "/"
						+ this.apf.getAlphaDoc().getEpisodeID() + "/$"
						+ CoordCardType.APA.value() + "/pl/Payload.xml",
						CoordCardType.APA.getModel());
			} catch (final IOException e) {
				ImportHandler.LOGGER.severe("Could not store new APA: " + e);
			}

			this.addNewParticipants(newCRAPayload);

			for (AlphaCardDescriptor acd : acdlist) {
				if (newPsa.getListOfTodoItems().contains(acd.getId())) {
					// descriptors must be completed, as only those adornments
					// are
					// stored in template, whose values differ from default
					acd = at.completeAdornments(
							acd,
							new AlphaAdaptiveFacadeImpl().cloneAlphaCardDescriptor(newAPAPayload
									.getContentCardAdornmentPrototype()),
							this.apf.getCRA().getActorNames(), this.apf
									.getAlphaConfig().getLocalNodeID()
									.getContributor().getActor());
					completedList.add(acd);
				}
			}
		} catch (final FilterException e) {
			ImportHandler.LOGGER.warning("Template Import failed!");
			mp.print("Template Import failed!");
			return;
		}

		// insert new alpha-cards
		for (final AlphaCardDescriptor acd : completedList) {
			try {
				if (!this.apf.getListOfACDs().contains(acd)) {
					this.apf.addAlphaCard(acd);
				}
			} catch (final Exception e) {
				ImportHandler.LOGGER.severe("Could not add AlphaCard: " + e);
			}
		}

		mp.print("Template Import successful!");

		return;
	}

	/**
	 * Update old descriptors.
	 * 
	 * @param newAdornments
	 *            the new adornments
	 */
	private void updateOldDescriptors(
			final List<PrototypedAdornment> newAdornments) {
		for (final AlphaCardDescriptor acd : this.apf.getListOfACDs()) {
			if (!acd.getId().getCardID().startsWith("$")) {
				for (final PrototypedAdornment pa : newAdornments) {
					acd.updateOrCreateAdornment(pa);
				}
				this.apf.updateAlphaCardDescriptor(acd.getId(), acd);
			}
		}
	}

	/**
	 * Adds the new participants.
	 * 
	 * @param newCRAPayload
	 *            the new cra payload
	 */
	private void addNewParticipants(final CRAPayload newCRAPayload) {

		final HashSet<Participant> plist = (HashSet<Participant>) this.apf
				.getCRA().getListOfParticipants();

		for (final Participant p : newCRAPayload.getListOfParticipants()) {
			if (!plist.contains(p)) {
				try {
					this.apf.addParticipant(p);
				} catch (final Exception e) {
					ImportHandler.LOGGER
							.severe("Could not add participant " + e);
				}
			}
		}

	}
}
