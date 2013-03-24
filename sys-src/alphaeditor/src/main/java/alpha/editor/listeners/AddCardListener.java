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
 * $Id$
 *************************************************************************/
package alpha.editor.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.editor.AlphaFormHelper;
import alpha.editor.ContentCardCreator;
import alpha.editor.EditorData;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationshipType;

/**
 * This class enables the user to add additional AlphaCards to the AlphaDoc.
 * 
 */
@Scope("prototype")
@Component
public class AddCardListener implements ActionListener {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AddCardListener.class.getName());

	/** The apf. */
	protected @Autowired
	AlphaPropsFacade apf = null;

	/** The ed. */
	protected @Autowired
	EditorData ed = null;

	/**
	 * Instantiates a new adds the card listener.
	 */
	public AddCardListener() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		AlphaCardID aci1 = null;
		AlphaCardID aci2 = null;

		/* Query single card or combined referal + result report */
		final JFrame frame = new JFrame("Add_Menu");
		final Object[] values = { "One", "Two", "New a-Form" };
		final String question = "Create one or two AlphaCards?";
		final int ret = JOptionPane.showOptionDialog(frame, question,
				"Number of AlphaCards", JOptionPane.PLAIN_MESSAGE,
				JOptionPane.PLAIN_MESSAGE, null, values, values[0]);

		ContentCardCreator acc = new ContentCardCreator(this.apf);
		acc.setCra(this.apf.getCRA());
		acc.setUser(this.apf.getParticipantByActor(this.ed.getConfig().getLocalNodeID().getContributor().getActor()));
		AlphaCardDescriptor ac = acc.create(ret);
		if (ac == null) {
			return;
		}
		aci1 = ac.getId();

		/* if two AlphaCards are added the first one is a referral voucher */
		if (ret == 1) {
			ac.readAdornment(CorpusGenericus.ALPHACARDTYPE.value()).setValue(
					AlphaCardType.REFERRAL_VOUCHER.value());
		}

		try {
			// create File representation of AlphaCard Descriptor
			// this is done in vvs.store, which is called in drools
			// String id = ac.getId().getCardID();
			// File folder = new File(homePath +
			// apf.getAlphaDoc().getEpisodeID() + "/" + id + "/desc/");
			// boolean check = folder.mkdirs();
			//
			// if (check == false) {
			// throw new Exception();
			// }

			this.apf.addAlphaCard(ac);
			// apf.getVerVarStore().store(ac);
			this.ed.getAdornmentsInstancesPanel().update();
			this.ed.getAdornmentsSchemaPanel().update();
			this.ed.getAdornment_prototype().initAdornmentVisu();
			this.ed.getAdornment_prototype().displayAdornments();

		} catch (final Exception e1) {
			AddCardListener.LOGGER.severe("Error: " + e1);

			final StackTraceElement[] stack = e1.getStackTrace();
			final StringBuffer buf = new StringBuffer();
			for (final StackTraceElement stackTraceElement : stack) {
				buf.append(stackTraceElement.toString() + "\n");
			}
			final String trace = buf.toString();

			JOptionPane.showMessageDialog(new JFrame(),
					"FAIL: " + e1.toString() + "\n" + e1.getLocalizedMessage()
							+ "\n" + trace);
		}

		/* if we create an alphaForm hand off to AlphaFormCreator */
		if (ret == 2) {
			final AlphaFormHelper afc = new AlphaFormHelper(ac, this.apf);
			afc.createEmptyForm();
		}

		/*
		 * Create second AlphaCardDescriptor if chosen, second
		 * AlphaCardDescriptor has type "result report"
		 */
		if (ret == 1) {
			acc = new ContentCardCreator(this.apf);
			acc.setCra(this.apf.getCRA());
			acc.setUser(this.apf.getParticipantByActor(this.ed.getConfig().getLocalNodeID().getContributor().getActor()));
			ac = acc.create(ret);
			if (ac == null) {
				return;
			}
			aci2 = ac.getId();
			ac.readAdornment(CorpusGenericus.ALPHACARDTYPE.value()).setValue(
					AlphaCardType.RESULTS_REPORT.value());
			try {
				// this is done in vvs.store, which is called in drools
				// String id = ac.getId().getCardID();
				// File folder = new File(homePath +
				// apf.getAlphaDoc().getEpisodeID() + "/" + id + "/desc/");
				// boolean check = folder.mkdirs();
				// if (check == false) {
				// throw new Exception();
				// }

				this.apf.addAlphaCard(ac);
				this.apf.addRelationship(aci1, aci2,
						AlphaCardRelationshipType.REQUIRES_RESULT);
				// apf.getVerVarStore().store(ac);
				this.ed.getAdornmentsInstancesPanel().update();

			} catch (final Exception e1) {
				AddCardListener.LOGGER.severe("Error: " + e1);
				final StackTraceElement[] stack = e1.getStackTrace();
				final StringBuffer buf = new StringBuffer();
				for (final StackTraceElement stackTraceElement : stack) {
					buf.append(stackTraceElement.toString() + "\n");
				}
				final String trace = buf.toString();
				JOptionPane.showMessageDialog(
						new JFrame(),
						"FAIL: " + e1.toString() + "\n"
								+ e1.getLocalizedMessage() + "\n" + trace);
			}
		}
	}
}
