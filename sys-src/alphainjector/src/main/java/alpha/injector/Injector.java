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
package alpha.injector;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.bulletin.AlphadocEventLogger;
import alpha.doyen.AlphaDoyenUtility;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.facades.AlphaPropsFacade;
import alpha.injector.templates.TemplateInjector;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.Payload;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.CorpusGenericusOC;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.offsync.time.VersionMapHistoryUtility;
import alpha.util.HardwareAddress;
import alpha.util.RNG;
import alpha.util.UIDGenerator;
import alpha.util.XmlBinder;
import alpha.vvs.Workspace;
import alpha.vvs.WorkspaceImpl;

/**
 * This class can either create a whole new AlphaDoc for the supplied document
 * or the document can be injected into the existing AlphaDoc.
 * 
 * 
 */
public class Injector {

	/** The Constant LOGGER. */
	private transient static final Logger LOGGER = Logger
			.getLogger(Injector.class.getName());

	/** The currentconfig. */
	protected AlphadocConfig currentconfig;

	/** The config. */
	protected AlphadocConfig config;

	/** The ad. */
	protected AlphaDoc ad;

	/** The psa pl. */
	protected PSAPayload psaPl;

	/** The cra pl. */
	protected CRAPayload craPl;

	/** The apa pl. */
	protected APAPayload apaPl;

	/** The arg. */
	private String arg;

	/** The ip. */
	protected InetAddress ip;

	/** The jar file. */
	protected File jarFile;

	/** The apf. */
	protected AlphaPropsFacade apf;

	/** The fdump. */
	protected byte[] fdump;

	/** The zero. */
	private boolean zero;

	/** The event log. */
	protected AlphadocEventLogger eventLog;

	/** The home path. */
	protected String homePath;

	/** The psa. */
	protected AlphaCardDescriptor psa;

	/** The cra. */
	protected AlphaCardDescriptor cra;

	/** The apa. */
	protected AlphaCardDescriptor apa;

	/** The card. */
	protected AlphaCardDescriptor alphacard;

	/** The oc. */
	protected ObjectUnderConsideration oc;

	/** The ep name. */
	protected String epName;

	/** The ep id. */
	protected String epId;

	/** The vvu. */
	private VersionMapHistoryUtility vvu;

	/**
	 * This constructor gets all the parameters that are needed for a successful
	 * execution of the Injector.
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
	public Injector(final AlphadocConfig config, final InetAddress ip,
			final File jarFile, final AlphaPropsFacade apf) {
		this.currentconfig = config;
		this.ip = ip;
		this.jarFile = jarFile;
		this.apf = apf;
	}

	/**
	 * This method is called to inject a passive document (e.g. pdf) with a
	 * active hull and integrate it into an AlphaDoc
	 * 
	 * @param args
	 *            the args
	 * @throws InjectorException
	 *             the injector exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void inject(final String[] args) throws InjectorException,
			IOException {

		// if system-propoerty listcards is set, then print all alpha-Card-IDs
		// and exit
		String listCards = System.getProperty("listcards");
		if (listCards != null) {
			PSAPayload psaPayload = this.apf.getPSA();
			final Set<AlphaCardID> laci = psaPayload.getListOfTodoItems();
			final Iterator<AlphaCardID> iter = laci.iterator();
			while (iter.hasNext()) {
				String id = iter.next().getCardID();
				if (!(id.startsWith("$"))) {
					Injector.LOGGER.info(id);
					System.out.println(id);
				}
			}
			System.exit(0);
		}

		if (args != null) {
			/*
			 * WARNING: we assume that the one-and-only parameter is the File to
			 * be injected
			 */
			this.arg = args[0];
		} else {
			System.exit(0);
		}

		if (this.eventLog == null) {
			if (new File(this.homePath + "bulletin.txt").exists()) {
				this.eventLog = new AlphadocEventLogger(this.homePath);
			}
		}

		File file = new File(this.arg);

		/* retrieving full name from 8.3 filename */
		if (this.arg.contains("~")) {
			final String first = this.arg.substring(
					this.arg.lastIndexOf("\\") + 1,
					this.arg.lastIndexOf(".") - 2).toLowerCase();
			final String last = this.arg.substring(
					this.arg.lastIndexOf(".") + 1).toLowerCase();
			final File parent = file.getParentFile();
			final long size = file.length();
			final File[] children = parent.listFiles();
			for (final File child : children) {
				if ((child.length() == size)
						&& child.getName().toLowerCase().contains(first)
						&& child.getName().toLowerCase().endsWith(last)) {
					file = child;
					break;
				}
			}
		}

		if (this.zero) {
			/* If there are no seed files a new AlphaDoc is created */

			// check if it is an xml file
			if (this.arg.substring(this.arg.lastIndexOf(".") + 1).toLowerCase()
					.equals("xml")) {
				this.injectXml(file);
			} else {
				this.createNewDoc(file);
			}

			System.exit(0);
		} else {

			/*
			 * If called with silent-Property, the user the payload is inserted
			 * into the current AlphaDoc
			 */
			final String silent = System.getProperty("silent");
			if (silent != null) {
				this.addToCurrent(file);
				System.exit(0);
			}

			/*
			 * The user may choose if a new AlphaDoc is created or if the
			 * payload is inserted into the current AlphaDoc
			 */
			final String[] options = { "New AlphaDoc", "Add to current" };
			final int result = JOptionPane
					.showOptionDialog(
							new JFrame(),
							"Create a new AlphaDoc or add payload to current AlphaDoc?",
							"Alph-O-Matic Injector", JOptionPane.PLAIN_MESSAGE,
							JOptionPane.PLAIN_MESSAGE, null, options, null);

			if (result == 0) {
				this.createNewDoc(file);
			} else if (result == 1) {
				this.addToCurrent(file);
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Injection failed due to users choice");
				// this case happens if the dialog box is closed
			}
			System.exit(0);
		}
	}

	/**
	 * This methods is called when an xml-file is injected It checks whether it
	 * is an ordinary payload (which is treated as usual) or an alphaTemplate.
	 * 
	 * @param file
	 *            the file
	 */
	protected void injectXml(final File file) {

		boolean isTemplate = false;

		if (file.exists()) {
			final XmlBinder xb = new XmlBinder();
			isTemplate = xb.validateRoot(file.getAbsolutePath(),
					"alpha.model.template");
		}
		if (isTemplate) {
			new TemplateInjector(this.currentconfig, this.ip, this.jarFile,
					this.apf).createDocFromTemplate(file);
			System.exit(0);
		} else {
			// no alphaTemplate, treatment just as in inject method
			try {
				this.createNewDoc(file);
			} catch (final InjectorException e) {
				Injector.LOGGER.severe("Error: " + e);
			} catch (final IOException e) {
				Injector.LOGGER.severe("Error: " + e);
			}
			System.exit(0);
		}

	}

	/**
	 * xml file is parsed to find out the name of the root element. not used
	 * anymore
	 * 
	 * @param path
	 *            the path
	 * @return the name of the xml root element
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private String getRootOfXml(final String path) {

		final DocumentBuilderFactory fact = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = fact.newDocumentBuilder();

			doc = builder.parse(path);
		} catch (final ParserConfigurationException e) {
			Injector.LOGGER.severe("Error: " + e);
		} catch (final SAXException e) {
			Injector.LOGGER.severe("Error: " + e);
		} catch (final IOException e) {
			Injector.LOGGER.severe("Error: " + e);
		}
		final Node node = doc.getDocumentElement();

		return node.getNodeName();
	}

	/**
	 * Adds the file to current a-Doc
	 * 
	 * @param file
	 *            the file
	 */
	protected void addToCurrent(final File file) {
		try {
			final String payloadPath = file.getAbsolutePath();
			final String payloadFileType = payloadPath.substring(payloadPath
					.lastIndexOf(".") + 1);

			this.config = this.apf.getAlphaConfig();
			this.homePath = this.apf.getHomePath();
			this.epId = this.config.getMyEpisodeId();
			this.ad = this.apf.getAlphaDoc();
			this.psaPl = this.apf.getPSA();
			this.craPl = this.apf.getCRA();
			this.apaPl = this.apf.getAPA();

			// Get the OC-ID of the alpha-Doc
			final AlphaCardDescriptor desc = this.apaPl
					.getContentCardAdornmentPrototype();
			final PrototypedAdornment proto = desc.readAdornment("OC ID");
			final String ocid = proto.getValue();

			this.oc = new ObjectUnderConsideration(ocid);

			final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();

			final Set<AlphaCardID> laci = this.psaPl.getListOfTodoItems();

			final LinkedList<String> list = new LinkedList<String>();
			list.add("New");

			final Iterator<AlphaCardID> iter1 = laci.iterator();
			while (iter1.hasNext()) {
				list.add(iter1.next().getCardID());
			}

			final Object[] values = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				values[i] = list.get(i);
			}

			String selected = null;

			// Check if system-properties want a silent operation
			// (addnewAlphaCard or updateAlphaCard)
			boolean addnewAlphaCard = false;
			boolean updateAlphaCard = false;
			if ((System.getProperty("silent") != null)
					&& (System.getProperty("cardID") == null))
				addnewAlphaCard = true;
			if ((System.getProperty("silent") != null)
					&& (System.getProperty("cardID") != null)) {
				updateAlphaCard = true;
				selected = System.getProperty("cardID");
			}

			if ((addnewAlphaCard)
					|| ((!updateAlphaCard) && ((selected = (String) JOptionPane
							.showInputDialog(new JFrame(),
									"Which AphaCard is the Payload meant for?",
									"AlphaCardDescriptor selection",
									JOptionPane.PLAIN_MESSAGE, null, values,
									values[0]))).toLowerCase().equals("new"))) {

				/* Create new AlphaCardDescriptor and add it to the AlphaDoc */
				this.alphacard = aaf.getInitialContentCardAPA(this.epId, ocid);
				this.alphacard.readAdornment(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).setValue(
						payloadFileType);

				// get role, title, institution, etc.
				final UserInputCollector ui = new UserInputCollector(this.epId,
						this.currentconfig);
				ui.fillAlphaCard(this.alphacard);
				this.eventLog.store("Created AlphaCardDescriptor: " + this.epId
						+ "/" + this.alphacard.getId().getCardID());
				Injector.LOGGER.info("Created AlphaCardDescriptor: "
						+ this.epId + "/" + this.alphacard.getId().getCardID());

				// add new AlphaCardID in PSA-Payload
				final LinkedHashSet<AlphaCardID> acids = new LinkedHashSet<AlphaCardID>();

				final Set<AlphaCardID> acIDset = this.psaPl
						.getListOfTodoItems();
				final Iterator<AlphaCardID> acIDiter = acIDset.iterator();
				while (acIDiter.hasNext()) {
					acids.add(acIDiter.next());
				}
				acids.add(this.alphacard.getId());
				this.psaPl.setListOfTodoItems(acids);

				// generate psa-Coordination
				this.psa = aaf.getInitialCoordinationCardAPA(this.epId, ocid);
				this.psa.setId(new AlphaCardID(this.epId, CoordCardType.PSA));
				this.psa.readAdornment(CorpusGenericus.TITLE.value()).setValue(
						CoordCardType.PSA.value());
				this.psa.readAdornment(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).setValue(
						"xml");

				this.eventLog.store("Changed PSA for new AlphaCard");
				Injector.LOGGER.info("Changed PSA for new AlphaCard");

				this.insertIntoHistory(this.psa, this.psaPl);

				/* Create folder for alphaCard payload (obsolete?) */
				boolean check = this.createFolder(this.homePath + "/"
						+ this.config.getMyEpisodeId() + "/"
						+ this.alphacard.getId().getCardID() + "/pl/");

				check = (check)
						&& (this.createFolder(this.homePath + "/"
								+ this.config.getMyEpisodeId() + "/"
								+ this.alphacard.getId().getCardID() + "/desc/"));

				if (check == false) {
					throw new Exception(
							"Could not create folders for alphaCard!");
				}

				// filling and storing the payload
				final Payload pl = new Payload();
				pl.setContent(Injector.toByteArray(new FileInputStream(
						new File(payloadPath))));
				this.insertIntoHistory(this.alphacard, pl);

				this.eventLog.store("Added Payload for the new AlphaCard");
				Injector.LOGGER.info("Added Payload for the new AlphaCard");

				if (file.delete() == false) {
					Injector.LOGGER.warning("Unable to delete the file "
							+ file.toString());
				}

			} else {
				// add in chosen alpha-card

				final AlphaCardID existingCardID = new AlphaCardID(
						this.currentconfig.getMyEpisodeId(), selected);

				final AlphaCardDescriptor existingCard = this.apf
						.getAlphaCard(existingCardID);

				/* Check if user is allowed to add payload */
				if (!existingCard
						.readAdornment(CorpusGenericus.ACTOR.value())
						.getValue()
						.substring(1)
						.equals(this.currentconfig.getLocalNodeID()
								.getContributor().getActor())) {
					JOptionPane
							.showMessageDialog(new JFrame(),
									"You are not allowed to add a payload to this AlphaCardDescriptor");
					return;
				}

				/* Read payload into byte array and create a payload object */
				final byte[] payloadContent = Injector
						.toByteArray(new FileInputStream(new File(payloadPath)));
				final Payload pl = new Payload();
				pl.setContent(payloadContent);

				final LinkedHashMap<String, String> adornmentChangeRequests = new LinkedHashMap<String, String>();
				adornmentChangeRequests.put(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
						payloadFileType);

				// FIXME (CPN@all): results in an NPE of Drools
				this.apf.updateAdornmentInstances(existingCardID,
						adornmentChangeRequests);

				this.apf.setPayload(existingCard.getId(), pl, null);
				this.eventLog.store("Added Payload to AlphaCardDescriptor "
						+ existingCard.getId().getEpisodeID() + "/"
						+ existingCard.getId().getCardID());

				this.eventLog
						.store("Changed syntacticPayloadType in AlphaCardDescriptor "
								+ existingCard.getId().getEpisodeID()
								+ "/"
								+ existingCard.getId().getCardID()
								+ " to "
								+ payloadFileType);

				/*
				 * Check if the dropped file shall be deleted after insertion
				 * into the AlphaDoc
				 */
				final int result = JOptionPane.showConfirmDialog(new JFrame(),
						"Delete payload source file?", "Input",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					if (file.delete() == false) {
						Injector.LOGGER.warning("Unable to delete the file "
								+ file.toString());
					}
				}
			}

		} catch (final Exception e) {
			Injector.LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method is called if the user wants the Alph-O-Matic Injector to
	 * create a new AlphaDoc.
	 * 
	 * @param file
	 *            the file
	 * @throws InjectorException
	 *             the injector exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void createNewDoc(final File file) throws InjectorException,
			IOException {
		String homeDir = null;
		try {
			String folder = file.getName();
			if (folder.startsWith(".") || folder.startsWith("~")) {
				folder = folder.substring(1);
			}

			homeDir = file.getAbsolutePath();
			homeDir = homeDir.replace(file.getName(), folder);
			final int sep = folder.lastIndexOf(".");
			final String type = folder.substring(sep + 1);
			folder = folder.substring(0, sep);
			homeDir = homeDir.substring(0, homeDir.lastIndexOf("."));
			boolean check = true;

			// By Konstantin: Damit Ordnerstruktur am gleichen Ort, wie
			// .jar-File auftaucht!
			homeDir = this.homePath.substring(0,
					this.homePath.substring(0, this.homePath.length() - 2)
							.lastIndexOf('/') + 1)
					+ folder;

			check = this.createFolder(homeDir);
			if (!check) {
				this.rollback(homeDir);
				throw new InjectorException("Injection failed");
			}

			/* Create new AlphadocEventLogger for new AlphaDoc */
			this.homePath = homeDir;
			this.eventLog = new AlphadocEventLogger(this.homePath);

			// read in epid, epname, oc
			this.readUserInput();

			/* Build AdornmentPrototype */
			final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();

			/* Build AlphaDoc */
			this.ad = new AlphaDoc(this.epId);
			this.ad.setTitle(this.epName);
			this.eventLog.store("Created new AlphaDoc with ID: " + this.epId);
			Injector.LOGGER.info("Created new AlphaDoc with ID: " + this.epId);

			// builds coordination cards
			this.buildCoordinationCards(this.oc.getId(), aaf);

			this.alphacard = aaf.getInitialContentCardAPA(this.epId,
					this.oc.getId());
			this.alphacard.readAdornment(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
					.setValue(type);
			final UserInputCollector ui = new UserInputCollector(this.epId,
					this.currentconfig);
			ui.fillAlphaCard(this.alphacard);
			this.eventLog.store("Created AlphaCardDescriptor: " + this.epId
					+ "/" + this.alphacard.getId().getCardID());
			Injector.LOGGER.info("Created AlphaCardDescriptor: " + this.epId
					+ "/" + this.alphacard.getId().getCardID());

			/* Build psaPayload */
			this.psaPl = new PSAPayload();
			final LinkedHashSet<AlphaCardID> acis = new LinkedHashSet<AlphaCardID>();
			acis.add(this.psa.getId());
			acis.add(this.cra.getId());
			acis.add(this.apa.getId());
			acis.add(this.alphacard.getId());
			final LinkedHashSet<AlphaCardRelationship> rels = new LinkedHashSet<AlphaCardRelationship>();
			this.psaPl.setListOfTodoItems(acis);
			this.psaPl.setListOfTodoRelationships(rels);

			final String port = this.createPort();
			final ContributorID contributor = new ContributorID(this.alphacard
					.readAdornment(CorpusGenericus.INSTITUTION.value())
					.getValue().substring(1), this.alphacard
					.readAdornment(CorpusGenericus.ROLE.value()).getValue()
					.substring(1), this.alphacard
					.readAdornment(CorpusGenericus.ACTOR.value()).getValue()
					.substring(1));

			this.craPl = this.buildCRA(port,
					this.currentconfig.getMailAddress(), contributor, this.oc);

			/* Build ApaPayload */
			this.apaPl = new APAPayload();
			this.apaPl.setContentCardAdornmentPrototype(aaf
					.getInitialContentCardAPA(this.epId, this.oc.getId()));

			/* Build AlphadocConfig */
			this.config = this.currentconfig;
			this.config.setMyEpisodeId(this.alphacard.getId().getEpisodeID());
			this.config.setMyCurrentlyActiveCardId(this.alphacard.getId()
					.getCardID());
			this.config.setLocalNodeID(this.currentconfig.getLocalNodeID());
			this.config.getLocalNodeID().getNode().setPort(port);

			/* Create folder for alphaCard payload */
			check = this.createFolder(homeDir + "/"
					+ this.config.getMyEpisodeId() + "/"
					+ this.config.getMyCurrentlyActiveCardId() + "/pl/");
			if (!check) {
				this.rollback(homeDir);
				throw new InjectorException(
						"Injection failed. Could not create payload folder.");
			}

			/* Create folder for alphaCard descriptor */
			check = this.createFolder(homeDir + "/"
					+ this.config.getMyEpisodeId() + "/"
					+ this.config.getMyCurrentlyActiveCardId() + "/desc");

			if (!check) {
				this.rollback(homeDir);
				throw new InjectorException(
						"Injection failed. Could not create descriptor folder.");
			}

			this.serializeCoordinationCards();
			final Payload pl = new Payload();
			pl.setContent(Injector.toByteArray(new FileInputStream(file)));
			this.insertIntoHistory(this.alphacard, pl);

			final String[] serial = { "alphaDoc", "alphaconfig", "alphacard" };
			for (final String obj : serial) {
				check = this.serialize(homeDir, obj);
				if (!check) {
					throw new InjectorException("Injection failed");
				}

			}

			// check = copyFile(arg, homeDir + "/" + epId + "/"
			// + config.getMyCurrentlyActiveCardId() + "/pl/" + "Payload."
			// + type);
			// if (!check) {
			// throw new InjectorException("Injection failed");
			// }

			this.eventLog.store("Added Payload to AlphaCardDescriptor "
					+ this.epId + "/" + this.alphacard.getId().getCardID());
			this.eventLog
					.store("Changed syntacticPayloadType in AlphaCardDescriptor "
							+ this.epId
							+ "/"
							+ this.alphacard.getId().getCardID()
							+ " to "
							+ type);

			final String dst = this.jarFile.getAbsolutePath().replace(
					this.jarFile.getName(), folder + ".jar");
			this.copyFile(this.jarFile.getAbsolutePath(), dst);

		} catch (final Exception e) {
			this.rollback(homeDir);
			Injector.LOGGER.severe("Error: " + e);
			e.printStackTrace();
			throw new InjectorException(e);
		}

	}

	/**
	 * builds folders and serializes coordination cards.
	 */
	protected void serializeCoordinationCards() {

		// create folders for coordination cards and serialize
		// AlphaCardDescriptor[] acdlist = { psa, apa, cra };
		// for (AlphaCardDescriptor acd : acdlist) {
		// String path = homePath + "/" + epId + "/" + acd.getId().getCardID();
		// createFolder(path + "/desc/");
		// createFolder(path + "/pl");
		// try {
		// serialize(path, acd
		// .readAdornment(CorpusGenericus.TITLE.value())
		// .getValue());
		// } catch (InjectorException e) {
		// LOGGER.severe("Could not serialize: " + e);
		// }
		// }
		this.insertIntoHistory(this.psa, this.psaPl);
		this.insertIntoHistory(this.apa, this.apaPl);
		this.insertIntoHistory(this.cra, this.craPl);
	}

	/**
	 * Insert into history.
	 * 
	 * @param acd
	 *            the acd
	 * @param pl
	 *            the pl
	 */
	protected void insertIntoHistory(final AlphaCardDescriptor acd,
			final Payload pl) {
		acd.getVersionMap()
				.putEntry(
						this.config.getLocalNodeID().getContributor()
								.getActor()
								+ "@" + HardwareAddress.getHardwareAddress(),
						acd.getVersionMap().getNumberOfModifications(
								this.config.getLocalNodeID().getContributor()
										.getActor()
										+ "@"
										+ HardwareAddress.getHardwareAddress()) + 1L);
		if (this.vvu == null) {
			final Workspace hwf = new WorkspaceImpl(new File(this.homePath
					+ "/" + this.config.getMyEpisodeId()));
			this.vvu = new VersionMapHistoryUtility(hwf, false);
		}
		this.createFolder(this.homePath + "/" + this.config.getMyEpisodeId()
				+ "/" + acd.getId().getCardID() + "/pl");

		try {
			this.vvu.insertIntoHistory(acd, pl);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads in episode name + id and object under consideration name + id.
	 */
	protected void readUserInput() {

		this.epName = "Episode name [case name]";
		this.epId = "Episode ID (will be generated)";
		final String ocId = UIDGenerator.generateUID();
		final String ocName = "Object under Consideration [i.e. patient]";
		this.oc = new ObjectUnderConsideration(ocId);
		final AdpAdornment aa = new AdpAdornment(
				CorpusGenericusOC.OCNAME.value(), ConsensusScope.GENERIC_STD,
				AdornmentDataType.STRING);
		aa.setValue(ocName);
		this.oc.updateOrCreateAdornment(aa);

		final JFrame frame = new JFrame();
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);
		final JPanel basic = new JPanel(new GridBagLayout());
		basic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel description = new JLabel(this.epName);
		description.setPreferredSize(new Dimension(120, 20));
		basic.add(description, gbc);

		final JTextField name = new JTextField(this.epName);
		name.setPreferredSize(new Dimension(120, 20));
		gbc.gridx++;
		basic.add(name, gbc);

		description = new JLabel(this.epId);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField id = new JTextField(UIDGenerator.generateUID());
		gbc.gridx++;
		basic.add(id, gbc);

		description = new JLabel(CorpusGenericus.OCID.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField ocIdT = new JTextField(ocId);
		gbc.gridx++;
		basic.add(ocIdT, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		basic.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		description = new JLabel(CorpusGenericusOC.OCNAME.value());
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		basic.add(description, gbc);

		final JTextField ocNameT = new JTextField(ocName);
		gbc.gridx++;
		basic.add(ocNameT, gbc);

		final Object[] options = { "OK" };
		final int ret = JOptionPane.showOptionDialog(frame, basic,
				"Episode Information", JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (ret == JOptionPane.OK_OPTION) {
			this.epName = name.getText();
			this.epId = id.getText();
			final AdpAdornment aa2 = new AdpAdornment(
					CorpusGenericusOC.OCNAME.value(),
					ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
			aa2.setValue(ocNameT.getText());
			this.oc = new ObjectUnderConsideration(ocIdT.getText());
			this.oc.updateOrCreateAdornment(aa2);

		}

	}

	/**
	 * Builds the coordination cards.
	 * 
	 * @param ocId
	 *            the oc id
	 * @param aaf
	 *            the alpha adaptive facade
	 */
	protected void buildCoordinationCards(final String ocId,
			final AlphaAdaptiveFacade aaf) {

		this.psa = aaf.getInitialCoordinationCardAPA(this.epId, ocId);
		this.psa.setId(new AlphaCardID(this.epId, CoordCardType.PSA));
		this.psa.readAdornment(CorpusGenericus.TITLE.value()).setValue(
				CoordCardType.PSA.value());
		this.psa.readAdornment(CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
				.setValue("xml");
		try {
			this.eventLog.store("Created PSA for AlphaDoc");
		} catch (final IOException e) {
			Injector.LOGGER.severe("Error: " + e);
		}
		Injector.LOGGER.info("Created PSA for AlphaDoc");

		this.cra = aaf.getInitialCoordinationCardAPA(this.epId, ocId);
		this.cra.setId(new AlphaCardID(this.epId, CoordCardType.CRA));
		this.cra.readAdornment(CorpusGenericus.TITLE.value()).setValue(
				CoordCardType.CRA.value());
		this.cra.readAdornment(CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
				.setValue("xml");
		try {
			this.eventLog.store("Created CRA for AlphaDoc");
		} catch (final IOException e) {
			Injector.LOGGER.severe("Error: " + e);
		}
		Injector.LOGGER.info("Created CRA for AlphaDoc");

		this.apa = aaf
				.getInitialCoordinationCardAPA(this.epId, this.oc.getId());
		this.apa.setId(new AlphaCardID(this.epId, CoordCardType.APA));
		this.apa.readAdornment(CorpusGenericus.TITLE.value()).setValue(
				CoordCardType.APA.value());
		this.apa.readAdornment(CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
				.setValue("xml");
		try {
			this.eventLog.store("Created APA for AlphaDoc");
		} catch (final IOException e) {
			Injector.LOGGER.severe("Error: " + e);
		}
		Injector.LOGGER.info("Created APA for AlphaDoc");

	}

	/**
	 * generate random port between 23000 and 23999.
	 * 
	 * @return the port number
	 */
	private String createPort() {

		int random = new RNG().generate();
		random = random % 1000;
		if (random < 0) {
			random += 1000;
		}
		random += 23000;

		final String port = String.valueOf(random);
		return port;
	}

	/**
	 * Builds the new cra.
	 * 
	 * @param port
	 *            the port
	 * @param mailAddress
	 *            the mail address
	 * @param contributor
	 *            the contributor
	 * @param oc
	 *            the oc
	 * @return the cRA payload
	 */
	protected CRAPayload buildCRA(final String port, final String mailAddress,
			final ContributorID contributor, final ObjectUnderConsideration oc) {
		/* Build CraPayload */
		this.craPl = new CRAPayload();
		this.craPl.setOc(oc);

		final LinkedHashSet<Participant> loO = new LinkedHashSet<Participant>();
		final Participant participant = AlphaDoyenUtility.changeToInitialParticipant(AlphaDoyenUtility.getNormalParticipant());
		participant.setNodeID(this.currentconfig.getLocalNodeID());
		loO.add(participant);

		this.craPl.setListOfParticipants(loO);
		return this.craPl;
	}

	/**
	 * Rollback.
	 * 
	 * @param path
	 *            the path
	 */
	protected void rollback(final String path) {
		final File deletee = new File(path);
		if (deletee.isDirectory()) {
			final File[] content = deletee.listFiles();
			for (final File file : content) {
				this.rollback(file.getAbsolutePath());
			}
		} else if (deletee.isFile()) {
			if (deletee.delete() == false) {
				Injector.LOGGER.warning("Unable to delete the file "
						+ deletee.toString());
			}
		}
	}

	/**
	 * This method is used to create the folder structure for a new alphaDoc.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	protected boolean createFolder(final String name) {
		boolean status = false;
		final File folder = new File(name);
		status = folder.mkdirs();
		status = folder.exists();
		return status;
	}

	/**
	 * This method is used when a new AlphaDoc is created to copy the payload
	 * and jar.
	 * 
	 * @param src
	 *            the src
	 * @param dst
	 *            the dst
	 * @return true, if successful
	 * @throws InjectorException
	 *             the injector exception
	 */
	protected boolean copyFile(final String src, final String dst)
			throws InjectorException {
		final boolean status = true;
		final File source = new File(src);
		final File destination = new File(dst);

		final long fsize = source.length();
		this.fdump = new byte[(int) fsize];

		FileInputStream fr;
		try {
			fr = new FileInputStream(source);
			fr.read(this.fdump);
			fr.close();
			if (destination.createNewFile() == false) {
				Injector.LOGGER.warning("The file " + destination
						+ " already exists.");
			}
			final FileOutputStream fw = new FileOutputStream(destination);
			fw.write(this.fdump);
			fw.flush();
			fw.close();
		} catch (final IOException e) {
			throw new InjectorException("Copy failed", e);
		}
		return status;
	}

	/**
	 * This method reads the file that is to be added to an AlphaCardDescriptor
	 * as payload.
	 * 
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws InjectorException
	 *             the injector exception
	 */
	protected byte[] readFile(final File file) throws InjectorException {
		final long fsize = file.length();
		final byte[] buffer = new byte[(int) fsize];

		FileInputStream fr;
		try {
			fr = new FileInputStream(file);
			fr.read(buffer);
			fr.close();
		} catch (final IOException e) {
			throw new InjectorException("Copy failed", e);
		}
		return buffer;
	}

	/**
	 * This method serializes the files of the new AlphaDoc.
	 * 
	 * @param folder
	 *            the folder
	 * @param name
	 *            the name
	 * @return true, if successful
	 * @throws InjectorException
	 *             the injector exception
	 */
	protected boolean serialize(final String folder, final String name)
			throws InjectorException {
		boolean status = true;
		String model = "alpha.model";
		final String docPath = folder;
		XmlBinder xb = null;
		try {
			xb = new XmlBinder();
			if (name.toLowerCase().equals("alphadoc")) {
				xb.store(this.ad, docPath + "/alphaDoc.xml", model);
			} else if (name.equals(CoordCardType.PSA.value())) {
				xb.store(this.psa, docPath + "/desc/Descriptor.xml", model);
				model = CoordCardType.PSA.getModel();
				xb.store(this.psaPl, docPath + "/pl/Payload.xml", model);
			} else if (name.equals(CoordCardType.CRA.value())) {
				xb.store(this.cra, docPath + "/desc/Descriptor.xml", model);
				model = CoordCardType.CRA.getModel();
				xb.store(this.craPl, docPath + "/pl/Payload.xml", model);
			} else if (name.equals(CoordCardType.APA.value())) {
				model = CoordCardType.APA.getModel();
				xb.store(this.apaPl, docPath + "/pl/Payload.xml", model);
				xb.store(this.apa, docPath + "/desc/Descriptor.xml", model);
			} else if (name.toLowerCase().equals("alphaconfig")) {
				model = "alpha.model.docconfig";
				xb.store(this.config, docPath + "/alphaconfig.xml", model);
			} else if (name.equals("alphacard")) {
				// xb.store(alphacard, docPath + "/" + ad.getEpisodeID() + "/"
				// + alphacard.getId().getCardID()
				// + "/desc/Descriptor.xml", "alpha.model");
			} else {
				status = false;
				throw new InjectorException(
						"Injection failed due to invalid serialization model");
			}
		} catch (final IOException e) {
			Injector.LOGGER.severe("IOError:" + e);
		}
		return status;
	}

	// /* Get a list of all AlphaCards and ask for the target Card */
	// final PSAPayload payload = this.apf.getPSA();
	//
	// final Set<AlphaCardID> laci = payload.getListOfTodoItems();
	// final int size = laci.size();
	//
	// final Object[] pos = new Object[size - 1];
	// final Iterator<AlphaCardID> iter1 = laci.iterator();
	// iter1.next();
	// for (int j = 0; j < (size - 1); j++) {
	// pos[j] = iter1.next().getCardID();
	// }
	// pos[0] = "New";
	//
	// String selected = (String) JOptionPane.showInputDialog(new JFrame(),
	// "Which AphaCard is the Payload meant for?",
	// "AlphaCardDescriptor selection", JOptionPane.PLAIN_MESSAGE,
	// null, pos, pos[0]);
	//
	// /* Create new AlphaCardDescriptor and add it to the AlphaDoc */
	// if (selected.toLowerCase().equals("new")) {
	// final UserInputCollector sg = new UserInputCollector(
	// this.currentconfig.getMyEpisodeId(), this.currentconfig);
	// try {
	// final AlphaCardDescriptor card = this.apf
	// .createNewAlphaCardDescriptor();
	// sg.fillAlphaCard(card);
	//
	// /* Add AlphaCardDescriptor to AlphaDoc */
	// try {
	// this.apf.addAlphaCard(card);
	// this.eventLog.store("Added AlphaCardDescriptor: "
	// + card.getId().getEpisodeID() + "/"
	// + card.getId().getCardID());
	// } catch (final Exception e1) {
	// throw new InjectorException("Adding failed", e1);
	// }
	//
	// selected = card.getId().getCardID();
	// } catch (final Exception e2) {
	// System.err.println(e2.getMessage());
	// e2.printStackTrace();
	// }
	// }
	//
	// try {
	// final AlphaCardDescriptor ac = this.apf
	// .getAlphaCard(new AlphaCardID(this.currentconfig
	// .getMyEpisodeId(), selected));
	//
	// /* Check if user is allowed to add payload */
	// if (!ac.readAdornment(CorpusGenericus.ACTOR.value())
	// .getValue()
	// .substring(1)
	// .equals(this.currentconfig.getLocalParticipant()
	// .getContributor().getActor())) {
	// JOptionPane
	// .showMessageDialog(new JFrame(),
	// "You are not allowed to add a payload to this AlphaCardDescriptor");
	// return;
	// }
	//
	// /* Read payload into byte array and create a payload object */
	// final byte[] buffer = this.readFile(file);
	// if (buffer == null) {
	// throw new InjectorException(
	// "Injector failed - Reading of payload returned NULL");
	// }
	// final Payload pl = new Payload();
	// pl.setContent(buffer);
	//
	// /* Add payload to AlphaDoc */
	// final LinkedHashMap<String, String> adornmentChangeRequests = new
	// LinkedHashMap<String, String>();
	// adornmentChangeRequests.put(
	// CorpusGenericus.SYNTACTICPAYLOADTYPE.value(), type);
	// this.apf.setPayload(ac.getId(), pl, adornmentChangeRequests);
	// this.eventLog.store("Added Payload to AlphaCardDescriptor "
	// + ac.getId().getEpisodeID() + "/" + ac.getId().getCardID());
	// this.eventLog
	// .store("Changed syntacticPayloadType in AlphaCardDescriptor "
	// + ac.getId().getEpisodeID()
	// + "/"
	// + ac.getId().getCardID() + " to " + type);
	//
	// /*
	// * Check if the dropped file shall be deleted after insertion into
	// * the AlphaDoc
	// */
	// final int result = JOptionPane.showConfirmDialog(new JFrame(),
	// "Delete payload source file?", "Input",
	// JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
	//
	// if (result == JOptionPane.YES_OPTION) {
	// if (file.delete() == false) {
	// Injector.LOGGER.warning("Unable to delete the file "
	// + file.toString());
	// }
	// }
	// } catch (final HeadlessException e) {
	// e.printStackTrace();
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * This method is called if the Alph-O-Matic Injector is run without seed
	 * files.
	 */
	public void setZero() {
		this.zero = true;
	}

	/**
	 * Sets the logger.
	 * 
	 * @param eventLog
	 *            the new logger
	 */
	public void setLogger(final AlphadocEventLogger eventLog) {
		this.eventLog = eventLog;
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
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @return the requested byte array
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static byte[] toByteArray(final InputStream input)
			throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		Injector.copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of
	 * <code>-1</code> after the copy has completed since the correct number of
	 * bytes cannot be returned as an int. For large streams use the
	 * <code>copyLarge(InputStream, OutputStream)</code> method.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static int copy(final InputStream input, final OutputStream output)
			throws IOException {
		final long count = Injector.copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.3
	 */
	public static long copyLarge(final InputStream input,
			final OutputStream output) throws IOException {
		final byte[] buffer = new byte[1024 * 4]; // DEFAULT_BUFFER_SIZE
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
