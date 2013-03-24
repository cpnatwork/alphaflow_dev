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
 * $Id: ExportHandler.java 3650 2012-03-08 23:54:03Z sipareis $
 *************************************************************************/
package alpha.templates.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.cra.CRAPayload;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
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
 * Handles the Export Button behaviour. Creates a new alpha template and fills
 * it with the information gathered in the filters.
 */
public class ExportHandler {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = Logger
			.getLogger(ExportHandler.class.getName());

	/** The apf. */
	private final AlphaPropsFacade apf;

	/** The home path. */
	private final File pathFile;

	/** The template wizard. */
	private TemplateWizard wizard;

	/** The file. */
	private File templateFile;

	/** The template path. */
	private String templatePath;

	private final boolean fullExport;

	/**
	 * Instantiates a new template handler.
	 * 
	 * @param apf
	 *            the alpha props facade
	 * @param pathFile
	 *            the path file
	 */
	public ExportHandler(final AlphaPropsFacade apf, final File pathFile,
			final boolean fullExport) {
		this.apf = apf;
		this.pathFile = pathFile;
		this.fullExport = fullExport;
	}

	/**
	 * when export button is clicked, this handler method exports a xml template
	 * file including the coordination cards and all descriptors.
	 */
	public final void exportTemplate() {

		this.wizard = new TemplateWizard("Export alpha-Template");

		// welcome screen in wizard
		final WizardMessagePresenter mp = new WizardMessagePresenter(
				this.wizard);
		mp.setWelcome(true);
		mp.print("Welcome to the Template-Export function!");
		mp.setWelcome(false);

		// choose location where to save template
		this.templateFile = new WizardFileChooser(this.wizard, "Save")

		.choose(this.pathFile, "  Choose location where to save the template.");

		// user canceled export
		if (this.templateFile == null) {
			ExportHandler.LOGGER
					.warning("Exporting aborted due to user selection.");
			mp.print("No path chosen. Export failed.");
			return;
		} else {
			this.templatePath = this.templateFile.getAbsolutePath();
		}

		// check if user chose right format
		if (!this.templatePath.endsWith(".xml")) {
			if (this.templatePath.contains(".")) {
				ExportHandler.LOGGER
						.warning("Exporting aborted due to wrong export format.");
				mp.print("Wrong file extension. Must be XML. Template Import failed!");
				return;
			} else {
				// user forgot file extension, add
				this.templatePath += ".xml";
			}
		}

		try {
			final APAPayload apaPayload;
			PSAPayload psaPayload;
			ArrayList<AlphaCardDescriptor> descriptorList;
			final CRAPayload craPayload;
			if (this.fullExport == false) {

				// filter custom adornment types
				apaPayload = new APAFilter(new WizardAdornmentNamesPresenter(
						this.wizard))
						.filterApa(this.apf.getAPA(), null,
								"Which custom Alpha-Adornment-Types do you want to export?");
				// do not export episode id
				apaPayload.getContentCardAdornmentPrototype().getId()
						.setEpisodeID(null);

				// copy as psa data is changed during filtering
				psaPayload = this.clonePsaWithoutEpisodeId();

				final PSAFilter psafilter = new PSAFilter(
						new WizardAlphaCardNamesPresenter(this.wizard));

				// get a copy of current descriptors
				descriptorList = (ArrayList<AlphaCardDescriptor>) this
						.cloneDescriptorsWithoutEpisodeId();

				// filters psa, removes unchosen cards + all relationships they
				// take
				// part
				psaPayload = psafilter.filterPsa(psaPayload, descriptorList,
						"Which Alpha-Cards do you want to export?");

				descriptorList = (ArrayList<AlphaCardDescriptor>) psafilter
						.adaptDescriptorsToPsa(descriptorList, psaPayload);
				if (descriptorList.size() == 0) {
					ExportHandler.LOGGER
							.warning("No Alpha-Cards were chosen for template export.");
				}

				final CRAFilter craFilter = new CRAFilter(
						new WizardContributorNamesPresenter(this.wizard,
								this.apf.getCRA().getOc()), this.apf.getCRA());
				craPayload = craFilter.filterCra();

				descriptorList = (ArrayList<AlphaCardDescriptor>) craFilter
						.removeActors(craPayload, descriptorList);

				final AdornmentsFilter af = new AdornmentsFilter(apaPayload,
						new WizardAdornmentNamesPresenter(this.wizard),
						new WizardAlphaCardNamesPresenter(this.wizard), true);

				// do not export oc id if necessary, remove from apa
				if (craPayload.getOc() == null) {
					final LinkedHashSet<CorpusGenericus> cgs = (LinkedHashSet<CorpusGenericus>) af
							.getNeverExportedAdornments();
					cgs.add(CorpusGenericus.OCID);
					af.setNeverExportedAdornments(cgs);
					final PrototypedAdornment pa = apaPayload
							.getContentCardAdornmentPrototype().readAdornment(
									CorpusGenericus.OCID.value());
					pa.setValue(CorpusGenericus.OCID.value());
					apaPayload.getContentCardAdornmentPrototype()
							.updateOrCreateAdornment(pa);
				}
				descriptorList = (ArrayList<AlphaCardDescriptor>) af
						.filterAdornments(descriptorList);

			} else {

				// full export

				apaPayload = this.apf.getAPA();
				// do not export episode id
				apaPayload.getContentCardAdornmentPrototype().getId()
						.setEpisodeID(null);

				// copy as psa data is changed during filtering
				psaPayload = this.clonePsaWithoutEpisodeId();

				// get a copy of current descriptors
				descriptorList = (ArrayList<AlphaCardDescriptor>) this
						.cloneDescriptorsWithoutEpisodeId();
				descriptorList = new PSAFilter(null)
						.createListOfContentCards(descriptorList);

				// now the set of all adornments which may be exported is
				// created and duplicated for each alpha card in order to reuse
				// the method applyAdornmentsFilter
				List<LinkedHashSet<String>> adornmentSets = new ArrayList<LinkedHashSet<String>>();
				AdornmentsFilter adornmentsFilter = new AdornmentsFilter(
						apaPayload, null, null, true);

				Set<String> neverExport = new LinkedHashSet<String>();

				for (CorpusGenericus cg : adornmentsFilter
						.getNeverExportedAdornments()) {
					neverExport.add(cg.value());
				}

				for (AlphaCardDescriptor acd : descriptorList) {

					LinkedHashSet<String> adornmentSet = new LinkedHashSet<String>();

					Collection<PrototypedAdornment> coll = acd.readAdornments();

					for (PrototypedAdornment pa : coll) {
						if (!neverExport.contains(pa.getName())) {
							adornmentSet.add(pa.getName());

						}
					}

					adornmentSets.add(adornmentSet);
				}

				adornmentsFilter.applyAdornmentsFilter(descriptorList,
						adornmentSets);

				craPayload = this.apf.getCRA();

			}

			final AlphaTemplate at = new AlphaTemplate();
			at.setCraPayload(craPayload);
			at.setApaPayload(apaPayload);
			at.setDescriptorList(descriptorList);
			at.setPsaPayload(psaPayload);

			String output = "Template Export successful!";
			final XmlBinder xml = new XmlBinder();
			try {
				xml.store(at, this.templatePath, "alpha.model.template");
				ExportHandler.LOGGER.info("Saving template: "
						+ this.templateFile + ".");
			} catch (final IOException e) {
				ExportHandler.LOGGER.severe("Could not save "
						+ this.templateFile.getName() + e);
				output = "Could not save template. Template Import failed!";
			}

			mp.print(output);

		} catch (final FilterException e) {
			ExportHandler.LOGGER.warning("Template Export failed!");
			mp.print("Template Export failed!");
		}

	}

	/**
	 * Clone psa without episode id.
	 * 
	 * @return the pSA payload
	 */
	private PSAPayload clonePsaWithoutEpisodeId() {
		final PSAPayload psaPayload = new PSAPayload();

		for (final AlphaCardID id : this.apf.getPSA().getListOfTodoItems()) {
			psaPayload.getListOfTodoItems().add(
					new AlphaCardID(null, id.getCardID()));
		}
		for (final AlphaCardRelationship acr : this.apf.getPSA()
				.getListOfTodoRelationships()) {
			psaPayload.getListOfTodoRelationships().add(
					new AlphaCardRelationship(new AlphaCardID(null, acr
							.getSrcID().getCardID()), new AlphaCardID(null, acr
							.getDstID().getCardID()), acr.getType()));
		}
		return psaPayload;
	}

	/**
	 * Clone descriptors and delete episode id.
	 * 
	 * @return the array list
	 */
	private List<AlphaCardDescriptor> cloneDescriptorsWithoutEpisodeId() {
		final ArrayList<AlphaCardDescriptor> clonedList = new ArrayList<AlphaCardDescriptor>();

		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();

		for (final AlphaCardDescriptor acd : this.apf.getListOfACDs()) {
			final AlphaCardDescriptor acd_copy = aaf
					.cloneAlphaCardDescriptor(acd);
			acd_copy.getId().setEpisodeID(null);
			clonedList.add(acd_copy);
		}
		return clonedList;
	}

}
