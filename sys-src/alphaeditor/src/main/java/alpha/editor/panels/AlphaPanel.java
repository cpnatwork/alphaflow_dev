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
package alpha.editor.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.editor.AdornmentChecker;
import alpha.editor.AlphaCardIcons;
import alpha.editor.AlphaCardStatus;
import alpha.editor.Editor;
import alpha.editor.EditorData;
import alpha.editor.adornmentVisualisation.AdornmentVisualisation;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.editor.handlers.FileDropHandler;
import alpha.editor.listeners.ChangeListener;
import alpha.editor.threads.AlphaFormsThread;
import alpha.editor.threads.FileOpenThread;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.apa.Priority;
import alpha.model.apa.Validity;
import alpha.model.apa.Visibility;
import alpha.model.identification.AlphaCardID;
import alpha.util.SpringPrototypeFacility;

/**
 * This class is derived from the class JPanel and offers additional parameters,
 * that allow an easier identification of the AlphaPanel.
 * 
 * 
 */
@Scope("prototype")
@Component
public class AlphaPanel extends JPanel {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaPanel.class.getName());

	/** The spf. */
	@Autowired
	private final SpringPrototypeFacility spf = null;

	/** The ed. */
	@Autowired
	private final EditorData ed = null;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The ad checker. */
	@Autowired
	private final AdornmentChecker adChecker = null;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5613216096764217039L;

	/** The aci. */
	private AlphaCardID aci;

	/** The status. */
	private AlphaCardStatus status;

	/** The icon. */
	private JLabel icon;

	/** The showAdornments. */
	private JPanel adornmentsPanel;

	/**
	 * Creates the.
	 * 
	 * @param aci
	 *            the aci
	 * @param editor
	 *            the editor
	 */
	public void init(final AlphaCardID aci, final Editor editor) {
		try {
			this.setLayout(new GridBagLayout());
			final FileDropHandler fdHandler = this.spf
					.autowire(new FileDropHandler());
			fdHandler.setAci(aci);
			this.setTransferHandler(fdHandler);
			final AlphaCardDescriptor card = this.apf.getAlphaCard(aci);
			this.configureStatus(card);

			this.aci = aci.clone();
			final String fsType = this.apf
					.getAlphaCard(aci)
					.readAdornment(
							CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
					.getValue();
			if (fsType.equals(FundamentalSemanticType.COORDINATION.value())) {
				this.setVisible(false);
			}
			final JLabel epId = new JLabel(aci.getEpisodeID());
			epId.setFont(new Font("", Font.PLAIN, 10));
			epId.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
			// myEpisodeId.setToolTipText("EpisodeID");
			final JLabel cardId = new JLabel(aci.getCardID());
			cardId.setFont(new Font("", Font.PLAIN, 10));
			cardId.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
			cardId.setPreferredSize(new Dimension(100, 25));
			cardId.setBackground(Color.BLUE);
			// myCurrentlyActiveCardId.setToolTipText("CardID");
			final JLabel cardTitle = new JLabel(card.readAdornment(
					CorpusGenericus.TITLE.value()).getValue());
			cardTitle.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
			// cardTitle.setToolTipText("AlphaCardDescriptor title");
			this.ed.getGbc().gridwidth = 1;
			this.ed.getGbc().weightx = 1;
			this.ed.getGbc().gridx = 0;
			this.ed.getGbc().gridy = 0;
			// JLabel icon = new
			// JLabel(AlphaCardIcons.DISPLAYED_CARD.getIcon());
			final JLabel icon = new JLabel("");
			icon.setHorizontalAlignment(SwingConstants.LEFT);
			icon.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
			icon.setPreferredSize(new Dimension(30, 25));
			this.icon = icon;
			this.activateIcon(true);
			this.add(icon, this.ed.getGbc());
			this.ed.getGbc().gridx = 1;
			this.add(epId, this.ed.getGbc());
			this.ed.getGbc().gridx = 3;
			this.add(cardId, this.ed.getGbc());
			this.ed.getGbc().gridheight = 2;
			this.ed.getGbc().gridx = 4;
			final JLabel attach = new JLabel(AlphaCardIcons.PAYLOAD.getIcon());
			attach.setHorizontalAlignment(SwingConstants.RIGHT);
			this.add(attach, this.ed.getGbc());
			this.ed.getGbc().gridheight = 1;
			this.ed.getGbc().gridwidth = 4;
			this.ed.getGbc().gridx = 0;
			this.ed.getGbc().gridy++;
			this.add(cardTitle, this.ed.getGbc());

			this.adornmentsPanel = this.createExtension(attach, aci);
			this.ed.getGbc().gridwidth = 5;
			this.ed.getGbc().gridx = 0;
			this.ed.getGbc().gridy++;
			this.add(this.adornmentsPanel, this.ed.getGbc());
			this.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(0, 0, 5, 0),
					BorderFactory.createLineBorder(this.status.getColor(), 2)));
			final Editor edi = editor;
			final String path = this.ed.getHomePath();
			final String spType = card.readAdornment(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
			this.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					assert (e.getSource() instanceof AlphaPanel);
					final AlphaPanel alphaPanel = (AlphaPanel) e.getSource();
					if (e.getClickCount() == 2) {
						if ((spType != null) && spType.equals("a-form.xml")) {
							final AlphaFormsThread aft = AlphaPanel.this.spf
									.autowire(new AlphaFormsThread(alphaPanel
											.getAci(), path));
							aft.start();
						} else {
							final FileOpenThread fot = AlphaPanel.this.spf
									.autowire(new FileOpenThread(path,
											alphaPanel.getAci()));
							fot.start();
						}
					}
					edi.setCurrent(alphaPanel.getAci());
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
				} /* not yet needed here */

				@Override
				public void mouseExited(final MouseEvent e) {
				} /* not yet needed here */

				@Override
				public void mousePressed(final MouseEvent e) {
				} /* not yet needed here */

				@Override
				public void mouseReleased(final MouseEvent e) {
				} /* not yet needed here */
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the extension.
	 * 
	 * @param attach
	 *            the attach
	 * @param aci
	 *            the aci
	 * @return the j panel
	 */
	private JPanel createExtension(final JLabel attach, final AlphaCardID aci) {
		final GridBagLayout gbl = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		final JPanel box = new JPanel(gbl);
		box.setName("Extension");
		box.setVisible(false);
		box.setAlignmentX((float) 0.5);

		try {

			final AlphaCardDescriptor alphacard = this.apf.getAlphaCard(aci);

			/* contributor */
			final JLabel contributor = this.createBox(
					CorpusGenericus.ACTOR.value(), aci);
			c.weightx = 1;
			c.weighty = 0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 0;
			box.add(contributor, c);

			/* object */
			final JLabel object = this.createBox(CorpusGenericus.OCID.value(),
					aci);
			c.gridwidth = 2;
			c.gridx = 3;
			box.add(object, c);

			/* visibility */

			final JLabel visible = this.createBox(
					CorpusGenericus.VISIBILITY.value(), aci);
			if (alphacard.readAdornment(CorpusGenericus.VISIBILITY.value())
					.getValue().equals(Visibility.PUBLIC.value().toUpperCase())) {
				visible.setIcon(AlphaCardIcons.VISIBILITY_PUBLIC.getIcon());
				visible.setText("");
			} else if (alphacard
					.readAdornment(CorpusGenericus.VISIBILITY.value())
					.getValue()
					.equals(Visibility.PRIVATE.value().toUpperCase())) {
				visible.setIcon(AlphaCardIcons.VISIBILITY_PRIVATE.getIcon());
				visible.setText("");
			}
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy++;
			box.add(visible, c);

			/* validity */
			final JLabel valid = this.createBox(
					CorpusGenericus.VALIDITY.value(), aci);
			if (alphacard.readAdornment(CorpusGenericus.VALIDITY.value())
					.getValue().equals(Validity.VALID.value().toUpperCase())) {
				valid.setIcon(AlphaCardIcons.VALIDITY_VALID.getIcon());
				valid.setText("");
			} else if (alphacard
					.readAdornment(CorpusGenericus.VALIDITY.value()).getValue()
					.equals(Validity.INVALID.value().toUpperCase())) {
				valid.setIcon(AlphaCardIcons.VALIDITY_INVALID.getIcon());
				valid.setText("");
			}
			c.gridx = 1;
			box.add(valid, c);

			/* version */
			final JLabel version = this.createBox(
					CorpusGenericus.VERSION.value(), aci);
			c.gridx = 2;
			box.add(version, c);

			/* variant */
			final JLabel variant = this.createBox(
					CorpusGenericus.VARIANT.value(), aci);
			c.gridx = 3;
			box.add(variant, c);

			/* sptype */
			final JLabel sptype = this.createBox(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value(), aci);
			final String payload_type = alphacard.readAdornment(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
			if (payload_type == null) {
				attach.setVisible(false);
				sptype.setText("N/A");
			} else if (payload_type.endsWith("a-form.xml")) {
				attach.setIcon(AlphaCardIcons.AFORMS_PAYLOAD.getIcon());
				attach.setVisible(true);
			} else {
				attach.setVisible(true);
			}
			c.gridx = 4;
			box.add(sptype, c);

			/* the following extensions are only available for content cards */
			if (alphacard
					.readAdornment(
							CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
					.getValue().equals(FundamentalSemanticType.CONTENT.value())) {
				/* deferred */
				final JLabel deferred = this.createBox(
						CorpusGenericus.DEFERRED.value(), aci);
				if (alphacard.readAdornment(CorpusGenericus.DEFERRED.value())
						.getValue().equalsIgnoreCase(Boolean.TRUE.toString())) {
					deferred.setIcon(AlphaCardIcons.DEFERRED.getIcon());
				}
				deferred.setText("");
				c.gridx = 0;
				c.gridy++;
				box.add(deferred, c);

				/* deleted */
				final JLabel deleted = this.createBox(
						CorpusGenericus.DELETED.value(), aci);
				if (alphacard.readAdornment(CorpusGenericus.DELETED.value())
						.getValue().equalsIgnoreCase(Boolean.TRUE.toString())) {
					deleted.setIcon(AlphaCardIcons.DELETED.getIcon());
				}
				deleted.setText("");
				c.gridx = 1;
				box.add(deleted, c);

				/* priority */
				final JLabel priority = this.createBox(
						CorpusGenericus.PRIORITY.value(), aci);
				final String priorityValue = alphacard.readAdornment(
						CorpusGenericus.PRIORITY.value()).getValue();
				AlphaPanel.LOGGER.finer("The value of adornment priority is "
						+ priorityValue);
				ImageIcon icon = null;
				if (priorityValue.equals(Priority.LOW.value().toUpperCase())) {
					icon = AlphaCardIcons.PRIORITY_LOW.getIcon();
					priority.setText("");
				} else if (priorityValue.equals(Priority.NORMAL.value()
						.toUpperCase())) {
					icon = AlphaCardIcons.PRIORITY_NORMAL.getIcon();
					priority.setText("");
				} else if (priorityValue.equals(Priority.HIGH.value()
						.toUpperCase())) {
					icon = AlphaCardIcons.PRIORITY_HIGH.getIcon();
					priority.setText("");
				}
				priority.setIcon(icon);
				c.gridx = 2;
				box.add(priority, c);

				/* duedate */
				final JLabel dueDate = this.createBox(
						CorpusGenericus.DUEDATE.value(), aci);
				c.gridwidth = 2;
				c.gridx = 3;
				box.add(dueDate, c);
			}
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return box;
	}

	/**
	 * Creates the box.
	 * 
	 * @param adornmentName
	 *            the adornment name
	 * @param aci
	 *            the aci
	 * @return the j label
	 */
	private JLabel createBox(final String adornmentName, final AlphaCardID aci) {
		try {
			final AlphaCardDescriptor ac = this.apf.getAlphaCard(aci);

			final JLabel boxLabel = new JLabel();
			boxLabel.setPreferredSize(new Dimension(45, 25));
			boxLabel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.BLACK),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));

			final AdornmentVisualisation adornmentVisualisation = new AdornmentVisualisation(
					ac.readAdornment(adornmentName),
					VisualisationState.AVAILABLE);
			boxLabel.setText(adornmentVisualisation.getValue());
			boxLabel.setToolTipText(adornmentName);

			final String user = this.ed.getConfig().getLocalNodeID()
					.getContributor().getActor();
			if (this.adChecker.isAlphaCardOwner(aci, user)) {
				// if (adChecker.isAdornmentChangeable(aci, adornmentName)) {
				boxLabel.addMouseListener(this.spf.autowire(new ChangeListener(
						adornmentName, aci)));
				// }
			}
			return boxLabel;
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Determines the status of an AlphaCardDescriptor.
	 * 
	 * @param ac
	 *            the ac
	 */
	private void configureStatus(final AlphaCardDescriptor ac) {

		AlphaCardStatus status = AlphaCardStatus.INVALIDPRIVATE;
		final Validity validity = Validity.fromValue(ac.readAdornment(
				CorpusGenericus.VALIDITY.value()).getValue());
		final Visibility visibility = Visibility.fromValue(ac.readAdornment(
				CorpusGenericus.VISIBILITY.value()).getValue());
		if ((validity == Validity.VALID) && (visibility == Visibility.PUBLIC)) {
			status = AlphaCardStatus.VALIDPUBLIC;
		} else if ((validity == Validity.INVALID)
				&& (visibility == Visibility.PUBLIC)) {
			status = AlphaCardStatus.INVALIDPUBLIC;
		} else if ((validity == Validity.VALID)
				&& (visibility == Visibility.PRIVATE)) {
			status = AlphaCardStatus.VALIDPRIVATE;
		}
		this.status = status;
	}

	/**
	 * Gets the aci.
	 * 
	 * @return the aci
	 */
	public AlphaCardID getAci() {
		return this.aci;
	}

	// /**
	// * Sets the aci.
	// *
	// * @param aci
	// * the aci to set
	// */
	// public void setAci(AlphaCardID aci) {
	// this.aci = aci;
	// }

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public AlphaCardStatus getStatus() {
		return this.status;
	}

	/**
	 * Activate icon.
	 * 
	 * @param activate
	 *            the activate
	 */
	public void activateIcon(final boolean activate) {
		if (activate) {
			this.icon.setIcon(AlphaCardIcons.DISPLAYED_CARD.getIcon());
		} else {
			this.icon.setIcon(null);
		}

	}

	/**
	 * Gets the showAdornments.
	 * 
	 * @return the adornmentsPanel
	 */
	public JPanel getAdornmentsPanel() {
		return this.adornmentsPanel;
	}

}
