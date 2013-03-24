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
package alpha.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import alpha.bulletin.AlphadocEventLogger;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.editor.deliveryAcknowledgement.DeliveryAcknowledgementPanel;
import alpha.editor.handlers.FileDropHandler;
import alpha.editor.insertionutil.InsertionUtility;
import alpha.editor.invitation.InvitationDialogController;
import alpha.editor.listeners.AddCardListener;
import alpha.editor.listeners.ChangeListener;
import alpha.editor.listeners.ExtensionMouseListener;
import alpha.editor.listeners.ModelMouseListener;
import alpha.editor.panels.AlphaCardAdornmentInstancesPanel;
import alpha.editor.panels.AlphaCardAdornmentSchemaPanel;
import alpha.editor.panels.AlphaPanel;
import alpha.editor.panels.ConnectionPanel;
import alpha.editor.panels.ContentCardAdornmentPrototypePanel;
import alpha.editor.threads.FileOpenThread;
import alpha.editor.threads.FilterThread;
import alpha.editor.threads.StoreConfigurationThread;
import alpha.editor.tokenVisualisation.TokenDialog;
import alpha.editor.tokenVisualisation.TokenPanel;
import alpha.editor.tokenVisualisation.TokenVisualisation;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.Participant;
import alpha.model.cra.PatientContact;
import alpha.model.cra.StandardTokens;
import alpha.model.cra.Token;
import alpha.model.deliveryacknowledgment.AcknowledgementStructureItem;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.templates.handlers.ExportHandler;
import alpha.templates.handlers.ImportHandler;
import alpha.util.CommunicationCredentialsCollector;
import alpha.util.SpringPrototypeFacility;

/**
 * This class creates an editor with a GUI that the user can utilize to edit the
 * AlphaDoc.
 * 
 * @version 2.0
 * 
 */
@org.springframework.stereotype.Component
public class Editor extends JFrame implements Observer {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(Editor.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3653137930377619738L;

	private JTree jTreeAcd;

	/** The spf. */
	@Autowired
	private final SpringPrototypeFacility spf = null;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The ad checker. */
	@Autowired
	private final AdornmentChecker adChecker = null;

	/** The ed. */
	@Autowired
	private final EditorData ed = null;

	/** The legend. */
	private JPanel legend;

	/** The notify. */
	private JPanel notify;

	/** The statusbar. */
	private JPanel statusbar;

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	/** The ad prototype panel. */
	private ContentCardAdornmentPrototypePanel adPrototypePanel;

	/** The ad schema panel. */
	private AlphaCardAdornmentSchemaPanel adSchemaPanel;

	/** The ad instance panel. */
	private AlphaCardAdornmentInstancesPanel adInstancePanel;

	/** The home path. */
	private String homePath;

	/** The alpha doc title. */
	private String alphaDocTitle;

	/** The worklist. */
	private JPanel worklist;

	/** The central. */
	private JPanel central;

	/** The members. */
	private JPanel members;

	/** The bulletin. */
	private JPanel bulletin;

	/** The acknowledgements. */
	private JPanel acknowledgements;

	/** The time_sheet. */
	private JPanel time_sheet;

	/** The event log. */
	private AlphadocEventLogger eventLog;

	/** The reports. */
	private JPanel reports;

	/** The doc title. */
	private JLabel docTitle;

	/** The documents. */
	private JPanel documents;

	/** The current documents. */
	private AlphaCardID[][] currentDocuments;

	/** The card panel. */
	private JPanel cardPanel;

	/** The invitation panel. */
	private JPanel invitationPanel;

	/** The textfields. */
	private final JTextField[] textfields = new JTextField[7];

	/** The participant table model. */
	@Autowired
	private final ParticipantTableModel participantModel = null;

	@Autowired
	private final TokenDialog td = null;

	protected DefaultMutableTreeNode rootAsSet;

	protected DefaultMutableTreeNode rootAs;

	// private JTree jTreeAsSet;

	private JTree jTreeAs;

	/** The Ack Panel. */
	private DeliveryAcknowledgementPanel ackPanel;

	/**
	 * Inits the.
	 */
	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		this.adPrototypePanel = this.applicationContext
				.getBean(ContentCardAdornmentPrototypePanel.class);
		this.adSchemaPanel = this.applicationContext
				.getBean(AlphaCardAdornmentSchemaPanel.class);
		this.adInstancePanel = this.applicationContext
				.getBean(AlphaCardAdornmentInstancesPanel.class);
		this.ackPanel = this.applicationContext
				.getBean(DeliveryAcknowledgementPanel.class);
	}

	/**
	 * Inits the.
	 * 
	 * @param aci
	 *            the aci
	 * @param eventLog
	 *            the event log
	 * @param config
	 *            the config
	 * @param homePath
	 *            the home path
	 * @throws AlphaEditorException
	 *             the alpha editor exception
	 */
	public void init(final AlphaCardID aci, final AlphadocEventLogger eventLog,
			final AlphadocConfig config, final String homePath)
			throws AlphaEditorException {

		this.ed.init(aci, config, homePath);

		this.homePath = homePath;
		this.eventLog = eventLog;
		this.notify = new JPanel();
		eventLog.setNotify(this.notify);
		this.ed.setNotify(this.notify);

		this.createGUI();
	}

	/**
	 * Gets the ed.
	 * 
	 * @return the ed
	 */
	public EditorData getEd() {
		return this.ed;
	}

	/**
	 * This method creates the GUI of the editor.
	 * 
	 * @throws AlphaEditorException
	 *             the alpha editor exception
	 */
	public void createGUI() throws AlphaEditorException {

		// /* set the look and feel */
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (ClassNotFoundException cnfe) {
		// LOGGER.severe(cnfe.toString());
		// } catch (InstantiationException ie) {
		// LOGGER.severe(ie.toString());
		// } catch (IllegalAccessException iae) {
		// LOGGER.severe(iae.toString());
		// } catch (UnsupportedLookAndFeelException ulafe) {
		// LOGGER.severe(ulafe.toString());
		// }
		/* configure window size */
		final int HSIZE = EditorData.HSIZE;
		final int VSIZE = EditorData.VSIZE;
		this.setMinimumSize(new Dimension(HSIZE / 2, VSIZE / 2));
		this.setMaximumSize(new Dimension(HSIZE, VSIZE));
		this.setPreferredSize(new Dimension(HSIZE, VSIZE));
		this.setSize(new Dimension(HSIZE, VSIZE));
		/* configure window title and favicon */
		this.setTitle("AlphaDoc Editor");
		final URL url = this.getClass().getClassLoader()
				.getResource("icons/favicon.png");
		final Image im = Toolkit.getDefaultToolkit().getImage(url);
		this.setIconImage(im);
		/* configure window position */
		final Toolkit toolkit = this.getToolkit();
		final Dimension size = toolkit.getScreenSize();
		this.setLocation(0, 0);
		// setLocation(size.width / 2 - getWidth() / 2, size.height / 2
		// - getHeight() / 2);
		Editor.LOGGER.finer("Set Editor Location with Parameters: "
				+ " Screen Size: " + size.width + "x" + size.height
				+ ", Editor Size: " + this.getWidth() + "x" + this.getHeight());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* create the subpanels of the editor */
		final JPanel basic = this.ed.getBasic();
		// basic.setTransferHandler(new FileDropHandler(null, ed.getConfig()));
		basic.setTransferHandler(this.spf.autowire(new FileDropHandler()));
		this.alphaDocTitle = this.apf.getAlphaDoc().getTitle();
		final JLabel headline = new JLabel();
		headline.setFont(new Font("", Font.BOLD, 20));
		try {
			headline.setText("Patient: "
					+ this.apf
							.getAlphaCard(
									this.apf.getAlphaDoc().getCoordCardId(
											CoordCardType.PSA))
							.readAdornment(CorpusGenericus.OCID.value())
							.getValue() + " (Episode: "
					+ this.apf.getAlphaDoc().getEpisodeID() + ")");
		} catch (final Exception e) {
			e.printStackTrace();
		}

		headline.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		basic.add(headline, BorderLayout.NORTH);
		basic.add(this.createMenuBar(), BorderLayout.WEST);
		basic.add(this.createCentral(), BorderLayout.CENTER);
		basic.add(this.createStatusBar(), BorderLayout.SOUTH);
		this.add(new JScrollPane(basic));
	}

	/**
	 * This method creates the JLabel containing the status bar of the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel createStatusBar() {
		final GridBagConstraints gbc = this.ed.getGbc();
		final Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String dat = sdf.format(cal.getTime());
		sdf = new SimpleDateFormat("HH:mm:ss");
		final String tim = sdf.format(cal.getTime());

		InetAddress i = null;
		try {
			i = InetAddress.getLocalHost();
		} catch (final UnknownHostException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.toString());
			System.exit(0);
		}
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		if (this.statusbar == null) {
			this.statusbar = new JPanel(new GridBagLayout());
		} else {
			this.statusbar.removeAll();
		}
		this.statusbar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		final JLabel doctor = new JLabel("Frank Burns");
		doctor.setText("UserNode: "
				+ this.ed.getConfig().getLocalNodeID().getContributor()
						.getActor());
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.statusbar.add(doctor, gbc);
		final JLabel node = new JLabel("Host: " + i.getHostName());
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.statusbar.add(node, gbc);
		final JLabel port = new JLabel("now");
		port.setText("Port: "
				+ this.ed.getConfig().getLocalNodeID().getNode().getPort());
		port.addMouseListener(this.spf.autowire(new ChangeListener("port")));
		gbc.gridx = 2;
		gbc.gridy = 0;
		this.statusbar.add(port, gbc);
		final JLabel date = new JLabel("today");
		date.setText("Date: " + dat);
		gbc.gridx = 3;
		gbc.gridy = 0;
		this.statusbar.add(date, gbc);
		final JLabel time = new JLabel("now");
		time.setText("Time: " + tim);
		gbc.gridx = 4;
		gbc.gridy = 0;
		this.statusbar.add(time, gbc);

		/* create notification popup */
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 5;
		this.notify.add(new JLabel("-"));
		this.notify.setVisible(false);
		// notify.setVisible(true);
		this.statusbar.add(this.notify, gbc);
		gbc.gridwidth = 1;

		return this.statusbar;
	}

	/**
	 * This method creates the central JPanel.
	 * 
	 * @return the j panel
	 * @throws AlphaEditorException
	 *             the alpha editor exception
	 */
	private JPanel createCentral() throws AlphaEditorException {
		this.central = new JPanel(new BorderLayout());

		JPanel cardPanelRightPart = new JPanel(new BorderLayout());
		cardPanelRightPart = this.createCardPanelRight(cardPanelRightPart);
		this.cardPanel = new JPanel(new BorderLayout());
		;
		this.cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		this.cardPanel.add(cardPanelRightPart, BorderLayout.NORTH);
		this.legend = this.createLegend();
		this.legend.setVisible(false);
		// this.cardPanel.add(this.legend, BorderLayout.CENTER);

		JPanel ackLegendPanel = new JPanel(new BorderLayout());
		ackLegendPanel.add(this.legend, BorderLayout.SOUTH);

		this.ackPanel.update();
		this.ackPanel.setVisible(false);
		// this.ackPanel.setSize(400, 400);
		this.ackPanel.setMinimumSize(new Dimension(400, 400));
		// this.ackPanel.setBackground(Color.green);
		ackLegendPanel.add(this.ackPanel, BorderLayout.NORTH);

		this.cardPanel.add(ackLegendPanel, BorderLayout.CENTER);

		final JButton showLegend = new JButton("Show/Hide Legend");
		showLegend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.ackPanel.setVisible(false);
				Editor.this.legend.setVisible(!Editor.this.legend.isVisible());
			}
		});

		final JButton showAcks = new JButton("Show/Hide Acks");
		showAcks.setEnabled(this.ed.getConfig().isAcknowledgeDelivery());
		showAcks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.ackPanel.update();
				Editor.this.ackPanel.setVisible(!Editor.this.ackPanel
						.isVisible());
				Editor.this.legend.setVisible(false);
			}
		});

		JPanel buttonSouthPanel = new JPanel();
		buttonSouthPanel.add(showLegend);
		buttonSouthPanel.add(showAcks);

		this.cardPanel.add(buttonSouthPanel, BorderLayout.SOUTH);
		// this.cardPanel.add(showLegend, BorderLayout.SOUTH);
		this.central.add(this.cardPanel, BorderLayout.EAST);

		/* create JPanel for the Project Time Sheet */
		this.time_sheet = new JPanel(new BorderLayout());
		Swing_Helper.initEditorPanel(this.time_sheet,
				"Time sheet for the AlphaDoc");

		this.createReport();
		final CRAPayload cra = this.apf.getCRA();
		this.createParticipants(cra.getListOfParticipants());
		this.createBulletin();
		this.createDocuments();
		this.createAcknowledgements();
		this.worklist = this.createWorklist();
		/*
		 * set the magnifying glasses of the AlphaPanels (only the current
		 * AlphaCard has to show one
		 */
		this.setCurrent(this.ed.getCurrent());

		/* Config: Content Card Adornment Prototype */
		this.adPrototypePanel.setLayout(new BorderLayout());
		Swing_Helper.initEditorPanel(this.adPrototypePanel,
				"AlphaDoc Adornment Prototype");
		this.adPrototypePanel.display();

		this.central.add(this.worklist);
		return this.central;
	}

	/**
	 * This method creates the JPanel containing the symbol legend of the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel createLegend() {
		final JPanel p = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = this.ed.getGbc();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.ipady = 5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		final JLabel topic = new JLabel("Symbol legend: ");
		topic.setFont(new Font("", Font.BOLD, 16));
		p.add(topic, gbc);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;

		int i = 0;
		for (final AlphaCardIcons ai : AlphaCardIcons.values()) {
			final JLabel symbol = new JLabel(ai.getDescription(), ai.getIcon(),
					SwingConstants.LEFT);
			gbc.gridx = (i % 2);
			p.add(symbol, gbc);
			gbc.gridy += gbc.gridx;
			i++;
		}
		gbc.ipady = 0;

		return p;
	}

	/**
	 * This method creates the JPanel containing the report view of the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel createReport() {
		final GridBagConstraints gbc = this.ed.getGbc();
		this.reports = new JPanel(new BorderLayout());
		Swing_Helper.initEditorPanel(this.reports,
				"Create Report for the AlphaDoc");
		final JPanel reports_i = new JPanel(new GridBagLayout());
		final JButton pdf = new JButton("Export as PDF");
		pdf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"TODO export as PDF");
			}
		});
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		reports_i.add(pdf, gbc);
		final JButton ps = new JButton("Export as PS");
		ps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane
						.showMessageDialog(new JFrame(), "TODO export as PS");
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		reports_i.add(ps, gbc);
		final JButton xml = new JButton("Export as XML");
		xml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"TODO export as XML");
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		reports_i.add(xml, gbc);

		this.reports.add(reports_i);
		return this.reports;
	}

	/**
	 * This method creates the JPanel containing the worklist view of the GUI.
	 * 
	 * @return the j panel
	 * @throws AlphaEditorException
	 *             the alpha editor exception
	 */
	private JPanel createWorklist() throws AlphaEditorException {

		if (this.worklist == null) {
			this.worklist = new JPanel(new BorderLayout());
		}

		final GridBagConstraints gbc = this.ed.getGbc();

		this.worklist.removeAll();
		Swing_Helper.initEditorPanel(this.worklist, null);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;

		final JPanel worklist_i = new JPanel(new GridBagLayout());

		this.setADTitle(this.alphaDocTitle);
		this.docTitle.setFont(new Font("", Font.BOLD, 18));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		worklist_i.add(this.docTitle, gbc);

		final JCheckBox open = new JCheckBox("Open only");
		open.setSelected(false);
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		worklist_i.add(open, gbc);
		open.addItemListener(new ModelMouseListener(true));

		final JCheckBox complete = new JCheckBox("Complete only");
		complete.setSelected(false);
		gbc.gridx = 2;
		gbc.gridy = 1;
		worklist_i.add(complete, gbc);
		complete.addItemListener(new ModelMouseListener(false));

		final JCheckBox box4 = new JCheckBox("Show Adornments");
		box4.setSelected(false);
		gbc.gridx = 0;
		gbc.gridy = 2;
		worklist_i.add(box4, gbc);
		box4.addItemListener(new ExtensionMouseListener());

		/*
		 * retrieve the AlphaCardIdentifiers of the AlphaCards that belong to
		 * the current user
		 */
		final AlphaCardID[][] currentWorklist = this.getDocuments();
		final String owner = this.ed.getConfig().getLocalNodeID()
				.getContributor().getActor();
		for (int i = 0; i < currentWorklist.length; i++) {
			if (!this.adChecker.isAlphaCardOwner(currentWorklist[i][0], owner)) {
				currentWorklist[i][0] = null;
			}
			if (!this.adChecker.isAlphaCardOwner(currentWorklist[i][1], owner)) {
				currentWorklist[i][1] = null;
			}
		}

		this.createAlphaPanels(currentWorklist, gbc, worklist_i);

		this.worklist.add(worklist_i, BorderLayout.NORTH);
		final JButton add = new JButton("Add AlphaCard");
		add.setMnemonic('a');
		add.addActionListener(this.spf.autowire(new AddCardListener()));
		this.worklist.add(add, BorderLayout.SOUTH);
		return this.worklist;
	}

	/**
	 * This method creates the JPanel containing the card view of the GUI.
	 * 
	 * @param cardPanelRight
	 *            the card panel right
	 * @return the j panel
	 */
	private JPanel createCardPanelRight(final JPanel cardPanelRight) {
		cardPanelRight.setBorder(Swing_Helper
				.createGuiElementBorder("AlphaCard Administration"));

		final JButton openPayload = new JButton("Open Payload");
		openPayload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final FileOpenThread fot = Editor.this.spf
						.autowire(new FileOpenThread(Editor.this.ed
								.getHomePath(), Editor.this.ed.getCurrent()));
				// FIXME doesn't work with Linux
				fot.start();
			}
		});
		final JButton setPayload = new JButton("Set Payload");
		setPayload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					new PayloadCreator(Editor.this.apf
							.getAlphaCard(Editor.this.ed.getCurrent()),
							Editor.this.central, Editor.this.apf);
				} catch (final Exception e1) {

					e1.printStackTrace();
				}
			}
		});

		final JPanel payloadButtonPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 0, 6, 0);
		payloadButtonPanel.add(setPayload, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 2, 6, 0);
		payloadButtonPanel.add(openPayload, gbc);
		cardPanelRight.add(payloadButtonPanel, BorderLayout.NORTH);

		// add a panel for the adaptive adornments instances
		this.adInstancePanel.setLayout(new BorderLayout());
		this.adInstancePanel.init();
		cardPanelRight.add(this.adInstancePanel, BorderLayout.CENTER);
		this.adInstancePanel.setVisible(true);

		// add a panel for the adaptive adornments schema
		this.adSchemaPanel.setLayout(new BorderLayout());
		this.adSchemaPanel.init();
		cardPanelRight.add(this.adSchemaPanel, BorderLayout.SOUTH);
		this.adSchemaPanel.setVisible(false);

		return cardPanelRight;
	}

	/**
	 * This method creates the JPanel containing the time sheet view of the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel createDocuments() {

		if (this.documents == null) {

			this.documents = new JPanel(new BorderLayout());
		}
		final GridBagConstraints gbc = this.ed.getGbc();

		this.documents.removeAll();
		Swing_Helper.initEditorPanel(this.documents, null);

		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		final JPanel documents_i = new JPanel(new GridBagLayout());

		this.docTitle = this.setADTitle(this.alphaDocTitle);
		this.docTitle.setFont(new Font("", Font.BOLD, 18));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		documents_i.add(this.docTitle, gbc);

		final Object[] filters = { "All", "Own", "Others" };
		final JComboBox<Object> open = new JComboBox<Object>(filters);

		final CRAPayload cra = this.apf.getCRA();
		final Set<ContributorID> contributors = cra.getLoContributors();
		for (final ContributorID contributor : contributors) {
			open.addItem(contributor.getActor());
		}
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		documents_i.add(open, gbc);
		open.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final FilterThread ft = new FilterThread(e.getSource(),
						Editor.this.ed.getConfig(), Editor.this.apf);
				ft.start();
			}
		});

		final JCheckBox own = new JCheckBox("Show Coordination");
		own.setName("showC");
		own.setSelected(false);
		gbc.gridx = 0;
		gbc.gridy = 2;
		documents_i.add(own, gbc);
		/* display coordination cards if checkbox is selected */
		own.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				try {
					final JCheckBox box = (JCheckBox) e.getSource();
					final JPanel model = (JPanel) box.getParent();
					final Component[] components = model.getComponents();
					for (final Component comp : components) {
						if (comp instanceof AlphaPanel) {
							final AlphaPanel ap = (AlphaPanel) comp;
							final String fsType = Editor.this.apf
									.getAlphaCard(ap.getAci())
									.readAdornment(
											CorpusGenericus.FUNDAMENTALSEMANTICTYPE
													.value()).getValue();
							if (fsType
									.contains(FundamentalSemanticType.COORDINATION
											.value())) {
								comp.setVisible(box.isSelected());
							}
						}
					}
				} catch (final Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		final JCheckBox showAdornments = new JCheckBox("Show Adornments");
		showAdornments.setName("showA");
		showAdornments.setSelected(false);
		gbc.gridx = 2;
		gbc.gridy = 2;
		documents_i.add(showAdornments, gbc);
		showAdornments.addItemListener(new ExtensionMouseListener());

		gbc.gridwidth = 1;

		/* Create visualisation for AlphaCards */
		this.currentDocuments = this.getDocuments();

		this.createAlphaPanels(this.currentDocuments, gbc, documents_i);

		this.documents.add(documents_i, BorderLayout.NORTH);
		final JButton add = new JButton("Add AlphaCard");
		add.setMnemonic('a');
		add.addActionListener(this.spf.autowire(new AddCardListener()));
		this.documents.add(add, BorderLayout.SOUTH);
		return this.documents;
	}

	/**
	 * Creates the alpha panels.
	 * 
	 * @param acis
	 *            the acis
	 * @param gbc
	 *            the gbc
	 * @param panel
	 *            the panel
	 */
	private void createAlphaPanels(final AlphaCardID[][] acis,
			final GridBagConstraints gbc, final JPanel panel) {

		for (int i = 0; i < acis.length; i++) {
			final AlphaPanel box = this.spf.autowire(new AlphaPanel());

			try {
				if (acis[i][0] != null) {
					if (this.apf.getAlphaCard(acis[i][0]) != null) {
						box.init(acis[i][0], this);
						gbc.gridwidth = 1;
						gbc.gridx = 0;
						gbc.gridy = i + 3;
						panel.add(box, gbc);
					}
				}

				if ((acis[i][0] != null) && (acis[i][1] != null)) {
					final ConnectionPanel connectionPanel = new ConnectionPanel();
					connectionPanel.setSrc(acis[i][0]);
					connectionPanel.setDst(acis[i][1]);
					gbc.gridwidth = 1;
					gbc.gridx = 1;

					final JLabel label = new JLabel(" ");
					connectionPanel.add(label);
					panel.add(connectionPanel, gbc);
				}

				if (acis[i][1] != null) {
					if (this.apf.getAlphaCard(acis[i][1]) != null) {
						final AlphaPanel rbox = this.spf
								.autowire(new AlphaPanel());
						rbox.init(acis[i][1], this);
						gbc.gridx = 2;
						gbc.gridy = i + 3;
						gbc.gridwidth = 1;
						panel.add(rbox, gbc);
					}
				}
			} catch (final Exception e) {
				// System.err.println(e.getMessage());
				// e.printStackTrace();
			}
		}
	}

	/**
	 * This method retrieves the AlphaCardID of all AlphaCards in an 2D-array
	 * that pays respect to relations between AlphaCards.
	 * 
	 * @return the documents
	 */
	@SuppressWarnings("unchecked")
	private AlphaCardID[][] getDocuments() {
		/** Retrieve data from drools via AlphaPropsFacade interface */
		final PSAPayload payload = this.apf.getPSA();

		final Set<AlphaCardID> laci = (Set<AlphaCardID>) ((HashSet<AlphaCardID>) payload
				.getListOfTodoItems()).clone();
		final Set<AlphaCardRelationship> lrel = payload
				.getListOfTodoRelationships();

		/** Build model that is to be visualized */
		final AlphaCardID[][] currentDocs = new AlphaCardID[laci.size()
				- lrel.size()][2];

		final LinkedHashMap<AlphaCardID, AlphaCardID> links = new LinkedHashMap<AlphaCardID, AlphaCardID>();
		final Iterator<AlphaCardRelationship> irel = lrel.iterator();
		while (irel.hasNext()) {
			final AlphaCardRelationship acr = irel.next();
			links.put(acr.getSrcID(), acr.getDstID());
			laci.remove(acr.getDstID());
		}

		final Iterator<AlphaCardID> iter = laci.iterator();

		int counter = 0;
		AlphaCardID[] row;
		while (iter.hasNext()) {
			row = currentDocs[counter];
			row[0] = iter.next();
			row[1] = links.get(row[0]);
			counter++;
		}
		/* Model complete. Contained in aci_model */
		this.currentDocuments = currentDocs;
		return currentDocs;
	}

	/**
	 * This method creates the JPanel containing a list of all members contained
	 * in the CRA-Payload and a details view.
	 * 
	 * @param docs2
	 *            the docs2
	 * @return the j panel
	 */
	private JPanel createParticipants(final Set<Participant> docs2) {
		if (this.members == null) {
			this.members = new JPanel(new BorderLayout());
		}
		this.members.removeAll();
		Swing_Helper.initEditorPanel(this.members, "Project Members");

		// final ParticipantTableModel participantModel = new
		// ParticipantTableModel();
		this.participantModel.updateData(docs2);

		final JTable participantsTable = new JTable(this.participantModel);

		// visualisation for participants' tokens
		final TokenPanel tokenPanel = new TokenPanel();

		participantsTable.setAutoCreateRowSorter(true);

		final ListSelectionModel listSelectionModel = participantsTable
				.getSelectionModel();
		listSelectionModel
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelectionModel
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(final ListSelectionEvent e) {
						final ListSelectionModel lsm = (ListSelectionModel) e
								.getSource();

						if (!lsm.isSelectionEmpty()) {
							final int row = participantsTable
									.convertRowIndexToModel(participantsTable
											.getSelectedRow());
							// Find out selection
							final int minIndex = lsm.getMinSelectionIndex();
							final int maxIndex = lsm.getMaxSelectionIndex();
							for (int i = minIndex; i <= maxIndex; i++) {
								if (lsm.isSelectedIndex(i)) {
									tokenPanel
											.update(Editor.this.participantModel
													.getToken(row));
									for (int i1 = 0; i1 < Editor.this.participantModel
											.getColumnCount(); i1++) {
										Editor.this.textfields[i1]
												.setText(Editor.this.participantModel
														.getValueAt(row, i1)
														.toString());
									}
								}

							}
						}

					}
				});
		participantsTable.setSelectionModel(listSelectionModel);

		// detail panel for selected table entry
		final GridBagConstraints gbc = new GridBagConstraints();
		final JPanel detailsPane = new JPanel(new GridBagLayout());
		detailsPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 5, 15, 20);
		final ImageIcon participantIcon = new ImageIcon(this.getClass()
				.getClassLoader().getResource("icons/participant.png"));
		detailsPane.add(new JLabel(participantIcon), gbc);

		gbc.gridx++;
		tokenPanel.init();
		detailsPane.add(tokenPanel, gbc);

		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 5, 2, 2);
		for (int n = 0; n < this.participantModel.getColumnCount(); n++) {
			gbc.gridx = 0;
			gbc.gridy++;
			final JLabel l = new JLabel(this.participantModel.getColumnName(n));
			l.setPreferredSize(new Dimension(80, 20));
			detailsPane.add(l, gbc);

			gbc.gridx = 1;
			this.textfields[n] = new JTextField();
			this.textfields[n].setPreferredSize(new Dimension(80, 20));
			detailsPane.add(this.textfields[n], gbc);
		}

		// do the layout
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(700);
		splitPane.setResizeWeight(1.0);
		splitPane.add(new JScrollPane(participantsTable));
		final JPanel rightHalf = new JPanel(new BorderLayout());
		rightHalf.add(detailsPane, BorderLayout.PAGE_START);
		splitPane.add(rightHalf);

		this.members.add(splitPane);
		return this.members;
	}

	/**
	 * This method creates the JPanel containing a list of all events contained
	 * in the log file.
	 * 
	 * @return the j panel
	 */
	private JPanel createBulletin() {

		if (this.bulletin == null) {
			this.bulletin = new JPanel(new BorderLayout());
		}
		this.bulletin.removeAll();
		Swing_Helper.initEditorPanel(this.bulletin, "Project Bulletin");

		final List<String> events = this.eventLog.getLog();
		final String[][] bData = new String[events.size()][2];
		int i = events.size() - 1;
		for (final String event : events) {
			bData[i][0] = event.substring(0, 19);
			bData[i][1] = event.substring(20);
			i--;
		}

		final String[] header = { "Time", "Event" };
		final JTable table = new JTable(bData, header);
		table.setAutoCreateRowSorter(true);

		this.bulletin.add(new JScrollPane(table));
		return this.bulletin;
	}

	/**
	 * 
	 */
	private JPanel createAcknowledgements() {
		if (this.acknowledgements == null) {
			this.acknowledgements = new JPanel(new BorderLayout());
		}
		this.acknowledgements.removeAll();
		Swing_Helper.initEditorPanel(this.acknowledgements,
				"Delivery Acknowledgements");

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.apf
				.getAlphaDoc().getEpisodeID());
		this.jTreeAcd = new JTree(root);

		// rootAsSet = new
		// DefaultMutableTreeNode("Versions of selected AlphaCard");
		// this.jTreeAsSet = new JTree(rootAsSet);

		rootAs = new DefaultMutableTreeNode("Acknowledgements");
		this.jTreeAs = new JTree(rootAs);

		DefaultMutableTreeNode treeNode = null;

		for (AlphaCardDescriptor acd : this.apf.getListOfACDs()) {
			treeNode = new DefaultMutableTreeNode(acd.getId());
			root.add(treeNode);
		}

		jTreeAcd.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		jTreeAcd.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) Editor.this.jTreeAcd
						.getLastSelectedPathComponent();

				if (selectedNode == null)
					// Nothing is selected.
					return;

				if (selectedNode.isLeaf()) {
					AlphaCardID acid = (AlphaCardID) selectedNode
							.getUserObject();

					DefaultMutableTreeNode root = new DefaultMutableTreeNode(
							acid.getCardID());

					Editor.this.rootAs.setUserObject(root);
					Editor.this.rootAs.removeAllChildren();

					for (AcknowledgementStructureItem asi : Editor.this.apf
							.getAlphaDoc().getHeadAs(acid)
							.getAcknowledgements()) {
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(
								asi.toString());
						Editor.this.rootAs.add(node);
					}

					((DefaultTreeModel) Editor.this.jTreeAs.getModel())
							.reload();

				}
			}
		});

		JPanel ackPanel = new JPanel();
		ackPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.ipadx = 148;
		gbc.ipady = 435;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new java.awt.Insets(11, 10, 11, 0);
		ackPanel.add(new JScrollPane(jTreeAcd), gbc);

		gbc.gridx++;
		ackPanel.add(new JScrollPane(jTreeAs), gbc);

		this.acknowledgements.add(ackPanel);

		return this.acknowledgements;
	}

	/**
	 * This method creates the JPanel containing a navigation menu for the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel createMenuBar() {
		final JPanel menu = new JPanel(new BorderLayout());
		menu.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		final GridBagLayout gbl = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel menu_i = new JPanel(gbl);
		menu_i.setBorder(Swing_Helper.createGuiElementBorder("Menu Bar"));
		final EditorData edata = this.ed;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 0, 1, 0);
		c.gridx = 0;
		c.gridy = 0;
		menu_i.add(Swing_Helper.createTitledSeparator("Activities"), c);

		JButton menuBarButton = new JButton("My Worklist");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.worklist);
				Editor.this.cardPanel.setVisible(true);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);
		menuBarButton = new JButton("Project Documents");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.documents);
				Editor.this.cardPanel.setVisible(true);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Reporting"), c);

		menuBarButton = new JButton("Project Bulletin");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.bulletin);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);
		menuBarButton = new JButton("Project Reports");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.reports);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);
		menuBarButton = new JButton("Project Time Sheet");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.time_sheet);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Actor Management"), c);

		// Display an overview of all locally known participating actors.
		menuBarButton = new JButton("List current actors");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.members);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		// Display the token dialog window.
		menuBarButton = new JButton("Change Tokens");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final AlphadocConfig config = Editor.this.ed.getConfig();
				td.init();
				td.showDialog();

				if (td.isSubmit()) {
					/*
					 * Normal mode: Checks whether changes of patientcontact
					 * token were made or if doyen token was forwarded and then
					 * invokes appropriate methods
					 */
					if ((td.getTabbedPane().getSelectedIndex() == 0)) {
						try {
							// Update PatientContact if changed
							if (td.isPatientContactChanged()) {
								String value = (td.isPatientContact() ? PatientContact.PATIENT_CONTACT
										.value()
										: PatientContact.NON_PATIENT_CONTACT
												.value());

								Token token = Editor.this.apf
										.getParticipantByActor(
												config.getLocalNodeID()
														.getContributor()
														.getActor()).readToken(
												StandardTokens.PATIENT_CONTACT
														.value());
								token.setValue(value);

								Editor.this.apf.updateToken(Editor.this.apf
										.getParticipantByActor(config
												.getLocalNodeID()
												.getContributor().getActor()),
										token, true);
							}
							// Send TokenPropagation for Doyen
							if ((td.getNewDoyen() != null)) {
								Editor.this.apf.propagateToken(td.getNewDoyen(), StandardTokens.DOYEN.value());
							}
						} catch (final Exception e1) {
							Editor.LOGGER.finer("Change Token: " + e1);
						}
					}
					/*
					 * Admin mode: This mode first collects all changes made in
					 * the admin panel of the token dialog then it forces a
					 * single CRA change with the new tokens like a reset of
					 * token values.
					 */
					else {
						if (td.getTabbedPane().getSelectedIndex() == 1) {
							// collect new Tokens
							final Map<String, String> newTokens = new HashMap<String, String>();

							for (final TokenVisualisation iv : td
									.getTokenVisus().values()) {
								if (iv.getState().equals(
										VisualisationState.UPDATE)) {
									newTokens.put(iv.getName(), iv.getValue());
								}
							}
							// proceed local change in CRA (full-update)
							try {
								Editor.this.apf.updateTokens(Editor.this.apf
										.getParticipantByActor(config
												.getLocalNodeID()
												.getContributor().getActor()),
										newTokens, true);
							} catch (final Exception e1) {
								Editor.LOGGER.finer("Change Token: " + e1);
							}
						}
					}
				}
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		// Display the invitation dialog window.
		menuBarButton = new JButton("Invite new actor");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final InvitationDialogController invitationDialogController = new InvitationDialogController(
						Editor.this.apf, (JFrame) SwingUtilities
								.getRoot(Editor.this.central), true);
				invitationDialogController.showDialog(true);
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		// Display the address information dialog.
		menuBarButton = new JButton("Edit address information");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final AlphadocConfig config = Editor.this.ed.getConfig();
				final CommunicationCredentialsCollector ccCollect = new CommunicationCredentialsCollector(
						config, false);
				ccCollect.showDialog();

				// Change local address information if user submits new
				// information
				if (ccCollect.isSubmit()) {
					config.setMailAddress(ccCollect.getMailAddress());
					config.setUsername(ccCollect.getUsername());
					config.setPassword(ccCollect.getPassword());
					config.setSmtpHost(ccCollect.getSmtpHost());
					config.setSmtpPort(ccCollect.getSmtpPort());
					config.setImapHost(ccCollect.getImapHost());
					config.setImapMode(ccCollect.getImapMode());
					config.setImapPort(ccCollect.getImapPort());
					config.setUseSpecificMailbox(ccCollect
							.isUseSpecificMailbox());
					config.setOwnKPPath(ccCollect.getOwnKPPath());
					config.setOwnKPPass(ccCollect.getOwnKPPass());
					config.setPkPath(ccCollect.getPkPath());
					config.setSsl(ccCollect.isSsl());
					config.getLocalNodeID().getNode()
							.setEmailAddress(ccCollect.getMailAddress());
					try {
						new StoreConfigurationThread(config,
								Editor.this.homePath).run();
					} catch (final IOException e2) {
						e2.printStackTrace();
					}

					// Update local CRA
					try {
						final Participant participant = Editor.this.apf
								.getParticipantByActor(config.getLocalNodeID()
										.getContributor().getActor());
						participant.setNodeID(config.getLocalNodeID());
						Editor.this.apf.addParticipant(participant);
					} catch (final Exception e1) {
						// e1.printStackTrace();
					}

					// Send a join message to all known actors
					Editor.this.apf.initiateJoin();

					// Reconnect to the overlay network using the new address
					// information
					Editor.this.apf.initializeOvernet();
				}
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Acknowledgements"), c);

		menuBarButton = new JButton("List Acknowledgements");
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Editor.this.displayPanel(Editor.this.acknowledgements);

				LOGGER.finer("List Acknowledgements____________\n"
						+ Editor.this.apf.getAlphaDoc().getAsList());
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Admin"), c);

		menuBarButton = new JButton("Adornment Prototype");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				Editor.this.displayPanel(edata.getAdornment_prototype());
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Process Templates"), c);

		menuBarButton = new JButton("Import");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				File pathFile = new File(Editor.this.homePath);
				pathFile = pathFile.getParentFile();
				new ImportHandler(Editor.this.apf, pathFile).importTemplate();

				Editor.this.refreshEditor();
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		menuBarButton = new JButton("Export");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				File pathFile = new File(Editor.this.homePath);
				pathFile = pathFile.getParentFile();
				new ExportHandler(Editor.this.apf, pathFile, false)
						.exportTemplate();
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		menuBarButton = new JButton("Full Export");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				File pathFile = new File(Editor.this.homePath);
				pathFile = pathFile.getParentFile();
				new ExportHandler(Editor.this.apf, pathFile, true)
						.exportTemplate();
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(menuBarButton, c);

		c.gridy++;
		menu_i.add(Swing_Helper.createTitledSeparator("Development"), c);

		// Force a complete refresh of alle graphical editor components relying
		// on persisted information
		menuBarButton = new JButton("Refresh editor");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				Editor.this.refreshEditor();
			}
		});
		c.gridy++;
		menu_i.add(menuBarButton, c);

		menuBarButton = new JButton("Insert serialized object");
		menuBarButton.setPreferredSize(new Dimension(200, 25));
		menuBarButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				new InsertionUtility(Editor.this.central, Editor.this.apf)
						.showInsertionDialog();
			}
		});

		c.gridy++;
		menu_i.add(menuBarButton, c);

		menu.add(menu_i, BorderLayout.NORTH);
		return menu;
	}

	/**
	 * This method displays the selected panel in the navigation menu in the
	 * central area.
	 * 
	 * @param displayPanel
	 *            the panel to display
	 */
	private void displayPanel(final JPanel displayPanel) {

		final JPanel[] panels = { this.worklist, this.documents, this.members,
				this.bulletin, this.reports, this.time_sheet,
				this.invitationPanel, this.acknowledgements,
				this.ed.getAdornment_prototype() };
		for (final JPanel p : panels) {
			if (p != null) {
				if (p.equals(displayPanel)) {
					p.setVisible(true);
					this.central.add(p);
					this.cardPanel.setVisible(false);
					this.ed.getBasic().updateUI();
				} else {
					p.setVisible(false);
				}
			}
		}
	}

	/**
	 * This method creates the JLabel containing the AlphaDoc title.
	 * 
	 * @param title
	 *            the title
	 * @return the j label
	 */
	private JLabel setADTitle(final String title) {
		// Line breaks in Labels only with HTML
		this.docTitle = new JLabel("<html>AlphaDoc:<br>" + title + "</html>");
		return this.docTitle;
	}

	/**
	 * Update alpha panel gu is.
	 */
	private void updateAlphaPanelGUIs() {
		/*
		 * setting magnifying glasses for the displayed AlphaCardDescriptor in
		 * 'Project Documents'
		 */
		Component[] components = this.documents.getComponents();
		for (final Component component : components) {
			if (component instanceof JPanel) {
				for (final Component c : ((JPanel) component).getComponents()) {
					if (c instanceof AlphaPanel) {
						final AlphaPanel panel = (AlphaPanel) c;
						panel.activateIcon(false);
						if (((AlphaPanel) c).getAci().equals(
								this.ed.getCurrent())) {
							panel.activateIcon(true);
						}
					}
				}
			}
		}
		/*
		 * setting magnifying glasses for the displayed AlphaCardDescriptor in
		 * 'My Worklist'
		 */
		components = this.worklist.getComponents();
		for (final Component component : components) {
			if (component instanceof JPanel) {
				for (final Component c : ((JPanel) component).getComponents()) {
					if (c instanceof AlphaPanel) {
						final AlphaPanel panel = (AlphaPanel) c;
						panel.activateIcon(false);
						if (((AlphaPanel) c).getAci().equals(
								this.ed.getCurrent())) {
							panel.activateIcon(true);
						}
					}
				}
			}
		}
	}

	/**
	 * This method gets the AlphaCardID of the newly selected
	 * AlphaCardDescriptor. After that the values of the cards adornments are
	 * loaded in the card view and it is checked which adornments are editable.
	 * 
	 * @param aci
	 *            the new current
	 */
	public void setCurrent(final AlphaCardID aci) {
		try {
			final AlphaCardDescriptor ac = this.apf.getAlphaCard(aci);

			/* do nothing if aci is invalid */
			if (ac == null) {
				Editor.LOGGER.severe("AC is null!");
				return;
			}
			this.ed.setCurrent(aci);

			this.ed.getConfig().setMyCurrentlyActiveCardId(aci.getCardID());
			final StoreConfigurationThread ct = this.spf
					.autowire(new StoreConfigurationThread(this.ed.getConfig(),
							this.ed.getHomePath()));
			try {
				ct.run();
			} catch (final IOException e1) {
				Editor.LOGGER.severe("Error: " + e1);
			}

			this.updateAlphaPanelGUIs();
			this.adInstancePanel.update();
			this.adSchemaPanel.update();
			this.ed.getBasic().updateUI();
			this.ackPanel.update();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable arg0, final Object arg1) {

		Editor.LOGGER.info("Editor is notified: "
				+ arg1.getClass().getSimpleName());

		/* refresh the GUI visualisation */
		this.createDocuments();
		try {
			this.createWorklist();
		} catch (final AlphaEditorException e) {
			Editor.LOGGER.severe("Error: " + e);
			JOptionPane.showMessageDialog(new JFrame(), "Refreshing failed");
		}
		this.updateAlphaPanelGUIs();
		this.createBulletin();
		this.createAcknowledgements();
		this.createParticipants(this.apf.getCRA().getListOfParticipants());
		this.ackPanel.update();

		this.ed.getBasic().updateUI();
		this.ed.getAdornmentsInstancesPanel().update();
		this.ed.getAdornmentsSchemaPanel().update();
		this.ed.getAdornment_prototype().initAdornmentVisu();

	}

	/**
	 * Sets the home path.
	 * 
	 * @param homePath
	 *            the new home path
	 */
	public void setHomePath(final String homePath) {
		this.homePath = homePath;
	}

	/**
	 * Refresh editor.
	 * 
	 */
	private void refreshEditor() {
		AlphaCardDescriptor psadesc = null;
		try {
			psadesc = this.apf.getAlphaCard(this.apf.getAlphaDoc()
					.getCoordCardId(CoordCardType.PSA));
		} catch (final Exception e) {
			Editor.LOGGER.severe("Error: " + e);
		}
		// trigger editor refresh
		this.update(null, psadesc);
		this.ed.getAdornmentsInstancesPanel().update();
		this.ed.getAdornmentsSchemaPanel().update();
		this.ed.getAdornment_prototype().initAdornmentVisu();
		this.ed.getAdornment_prototype().displayAdornments();
		this.ackPanel.update();
	}

}
