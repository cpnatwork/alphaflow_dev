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
 * $Id: TemplateInjector.java 3792 2012-05-05 10:27:03Z uj32uvac $
 *************************************************************************/
package alpha.injector.templates;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.bulletin.AlphadocEventLogger;
import alpha.doyen.AlphaDoyenUtility;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.facades.AlphaPropsFacade;
import alpha.injector.Injector;
import alpha.injector.InjectorException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.CorpusGenericus;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.Participant;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
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
import alpha.templates.gui.wizard.WizardMessagePresenter;
import alpha.util.XmlBinder;

/**
 * Handles the drag'n'drop injection of an Alpha-Template A new Alpha-Doc is
 * created based on the template data.
 */
public class TemplateInjector extends Injector {

	/** The Constant LOGGER. */
	private transient static final Logger LOGGER = Logger
			.getLogger(TemplateInjector.class.getName());

	/**
	 * Instantiates a new template injector.
	 * 
	 * @param config
	 *            the config
	 * @param ip
	 *            the ip
	 * @param jarFile
	 *            the jar file
	 * @param apf
	 *            the apf
	 */
	public TemplateInjector(final AlphadocConfig config, final InetAddress ip,
			final File jarFile, final AlphaPropsFacade apf) {
		super(config, ip, jarFile, apf);
	}

	/**
	 * Creates an alphadoc from a template file.
	 * 
	 * @param file
	 *            the file
	 */
	// method is similiar to createDoc
	// from Injector-class, with special template functionality
	public void createDocFromTemplate(final File file) {

		AlphaTemplate at = null;
		final TemplateWizard tw = new TemplateWizard("Import Alpha-Template");
		final WizardMessagePresenter mp = new WizardMessagePresenter(tw);

		if (file.exists()) {
			final XmlBinder xml = new XmlBinder();
			at = (AlphaTemplate) xml.load(file.getAbsolutePath(),
					"alpha.model.template");

		} else {
			mp.print("Could not find Template. Template Import failed!");
			TemplateInjector.LOGGER.severe("Could not find Template at"
					+ file.getAbsolutePath());
			return;
		}

		if (at == null) {
			mp.print("Could not load file. Is it really a template? Template Import failed!");
			TemplateInjector.LOGGER.severe("Could not load template from:"
					+ file.getAbsolutePath());
			return;
		}

		// welcome screen
		mp.setWelcome(true);
		mp.print("Template is now being imported!");
		mp.setWelcome(false);

		this.readUserInput();

		// Custom button text
		Object[] options = { "Full Import", "Custom Import" };
		int n = JOptionPane
				.showOptionDialog(
						new JFrame(),
						"Do you want to perform a Full Import or choose the specific data from the Alpha-Template?",
						"Choose Import Type", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, null);

		boolean fullImport = false;

		if (n == 0) {
			fullImport = true;
		} else if (n != 1) {
			TemplateInjector.LOGGER.warning("Template Import failed!");
			mp.print("You have to choose either Full Import or Custom Import. Template Import failed!");
			System.exit(1);
		}

		// Build AlphaDoc
		this.ad = new AlphaDoc(this.epId);
		this.ad.setTitle(this.epName);

		ArrayList<AlphaCardDescriptor> acdlist = (ArrayList<AlphaCardDescriptor>) at
				.getDescriptorList();
		try {

			if (fullImport == true) {
				this.apaPl = at.getApaPayload();
				this.psaPl = at.getPsaPayload();
				acdlist = (ArrayList<AlphaCardDescriptor>) at
						.getDescriptorList();
			} else {

				// filter custom adornments
				this.apaPl = new APAFilter(
						new WizardAdornmentNamesPresenter(tw))
						.filterApa(at.getApaPayload(), null,
								"Which custom Alpha-Adornment-Types do you want to import?");

				// filter psa
				final PSAFilter psaFilter = new PSAFilter(
						new WizardAlphaCardNamesPresenter(tw));
				this.psaPl = psaFilter.filterPsa(at.getPsaPayload(), acdlist,
						"Which Alpha-Cards do you want to import?");

				// filter set of alpha cards descriptors based on previously
				// filtered psa
				acdlist = (ArrayList<AlphaCardDescriptor>) psaFilter
						.adaptDescriptorsToPsa(acdlist, this.psaPl);
			}

			for (final AlphaCardRelationship acr : this.psaPl
					.getListOfTodoRelationships()) {
				acr.getSrcID().setEpisodeID(epId);
				acr.getDstID().setEpisodeID(epId);
			}

			// cancel import, as Alpha-Doc must contain at least a single
			// Alpha-Card
			if (acdlist.size() == 0)
				throw new FilterException();

			if (fullImport == true) {
				this.craPl = at.getCraPayload();
			} else {
				final CRAFilter craFilter = new CRAFilter(
						new WizardContributorNamesPresenter(tw, at
								.getCraPayload().getOc()), at.getCraPayload());

				this.craPl = craFilter.filterCra();
			}

			// the user who imports the template should also participate
			this.addCurrentUserToCRA();

			this.config = this.currentconfig;
			this.config.setMyCurrentlyActiveCardId(acdlist.get(0).getId()
					.getCardID());

			this.config.setMyEpisodeId(this.epId);

			if (fullImport == false) {

				// filter adornments of each alpha card descriptor
				acdlist = (ArrayList<AlphaCardDescriptor>) new AdornmentsFilter(
						this.apaPl, new WizardAdornmentNamesPresenter(tw),
						new WizardAlphaCardNamesPresenter(tw), false)
						.filterAdornments(acdlist);

			}

			String folder = file.getName();
			if (folder.startsWith(".") || folder.startsWith("~")) {
				folder = folder.substring(1);
			}

			this.homePath = file.getAbsolutePath();
			this.homePath = this.homePath.replace(file.getName(), folder);

			this.homePath = this.homePath.substring(0,
					this.homePath.lastIndexOf("."));

			this.createFolder(this.homePath);

			try {
				this.eventLog = new AlphadocEventLogger(this.homePath);
			} catch (final IOException e) {
				TemplateInjector.LOGGER.severe("Error: " + e);
			}

			final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
			this.buildCoordinationCards(this.oc.getId(), aaf);

			PrototypedAdornment pa = null;
			if (this.craPl.getOc() != null) {

				// update oc id (necessary if oc was imported)
				pa = this.apaPl.getContentCardAdornmentPrototype()
						.readAdornment(CorpusGenericus.OCID.value());
				pa.setValue(this.craPl.getOc().getId());
				this.apaPl.getContentCardAdornmentPrototype()
						.updateOrCreateAdornment(pa);
			} else {
				this.craPl.setOc(this.oc);
			}
			// store the alphacards
			for (AlphaCardDescriptor acd : acdlist) {
				// work with descriptor only if it is part of new descriptor set
				// determined in mergePSA
				if (this.psaPl.getListOfTodoItems().contains(acd.getId())) {
					// descriptors must be completed, as only those adornments
					// are
					// stored in template, whose values differ from default
					acd = at.completeAdornments(
							acd,
							new AlphaAdaptiveFacadeImpl().cloneAlphaCardDescriptor(this.apaPl
									.getContentCardAdornmentPrototype()),
							this.craPl.getActorNames(), this.config
									.getLocalNodeID().getContributor()
									.getActor());
					if (at.getCraPayload().getOc() != null) {

						// add new oc id
						acd.updateOrCreateAdornment(pa);

					}

					// add episode id
					acd.getId().setEpisodeID(this.config.getMyEpisodeId());

					final String path = this.homePath + "/" + this.epId + "/"
							+ acd.getId().getCardID();

					if (this.createFolder(path + "/desc/") == false) {
						this.rollback(this.homePath);
						TemplateInjector.LOGGER
								.severe("Error: Injection failed. Could not create descriptor folder.");
					}

					this.alphacard = acd;

					this.insertIntoHistory(this.alphacard, null);

				}
			}
			// add episode ids to psa & apa payloads
			for (final AlphaCardID id : this.psaPl.getListOfTodoItems()) {
				id.setEpisodeID(this.config.getMyEpisodeId());
			}
			this.apaPl.getContentCardAdornmentPrototype().getId()
					.setEpisodeID(this.config.getMyEpisodeId());

			this.serializeCoordinationCards();

			// serialize doc and config
			try {
				this.serialize(this.homePath, "alphaconfig");
			} catch (final InjectorException e) {
				TemplateInjector.LOGGER.severe("Could not serialize" + e);
			}

			try {
				this.serialize(this.homePath, "alphadoc");
			} catch (final InjectorException e) {
				TemplateInjector.LOGGER.severe("Could not serialize" + e);
			}

			// copy/rename jarfile to proper location
			final File f = new File(this.homePath);
			final File parent = f.getParentFile();
			try {
				this.copyFile(this.jarFile.getAbsolutePath(),
						parent.getAbsolutePath() + "/" + f.getName() + ".jar");
			} catch (final InjectorException e) {
				TemplateInjector.LOGGER.severe("Could not copy file" + e);
			}

			mp.print("Template Import successful!");
			return;
		} catch (final FilterException e) {
			TemplateInjector.LOGGER.warning("Template Import failed!");
			mp.print("Template Import failed!");
		}
		return;
	}

	/**
	 * Adds the current user to cra.
	 */
	private void addCurrentUserToCRA() {
		final ContributorID contributor = new ContributorID(
				CorpusGenericus.INSTITUTION.value(),
				CorpusGenericus.ROLE.value(), this.currentconfig
						.getLocalNodeID().getContributor().getActor());

		final HashSet<Participant> loO = (HashSet<Participant>) this.craPl
				.getListOfParticipants();
		final Participant part = AlphaDoyenUtility.getNormalParticipant();
		part.setContributor(contributor);

		final EndpointID node = new EndpointID(this.ip.getHostName(),
				this.ip.getHostAddress(), this.currentconfig
						.getLocalNodeID().getNode().getPort(),
				this.currentconfig.getMailAddress());
		node.setEmailAddress(this.currentconfig.getMailAddress());
		part.setNode(node);
		loO.add(part);
		this.craPl.setListOfParticipants(loO);
	}

}
