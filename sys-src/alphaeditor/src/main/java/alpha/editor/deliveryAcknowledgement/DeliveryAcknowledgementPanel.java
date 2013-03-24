package alpha.editor.deliveryAcknowledgement;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alpha.editor.EditorData;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.deliveryacknowledgment.AcknowledgementStructureItem;
import alpha.model.deliveryacknowledgment.AcknowledgmentStructure;

// TODO: Auto-generated Javadoc
/**
 * The Class DeliveryAcknowledgementPanel.
 */
@Component
public class DeliveryAcknowledgementPanel extends JPanel {

	/** The Constant LOGGER. */
	@SuppressWarnings("unused")
	transient private static final Logger LOGGER = Logger
			.getLogger(DeliveryAcknowledgementPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6605674808240492464L;

	/** The editor data. */
	@Autowired
	private final EditorData editorData = null;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The list of participant acks. */
	protected Vector<DeliveryAcknowledgementVisualisation> listOfParticipantAcks;

	/**
	 * Instantiates a new delivery acknowledgement panel.
	 */
	public DeliveryAcknowledgementPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(140, 100, 140, 100));
	}

	/**
	 * Update. Gets new list of DeliveryAcknowledgements.
	 */
	public void update() {
		try {
			this.listOfParticipantAcks = new Vector<DeliveryAcknowledgementVisualisation>();

			// currently active alphacard
			final AlphaCardDescriptor ac = this.apf
					.getAlphaCard(this.editorData.getCurrent());

			// LOGGER.info(" _____________ _________ getHeadAs:"
			// + this.apf.getAlphaDoc().getHeadAs(ac.getId()));

			final AcknowledgmentStructure as = this.apf.getAlphaDoc()
					.getHeadAs(ac.getId());

			// LOGGER.info(" _____________ _________ as.Acks:"
			// + as.getAcknowledgements());

			for (AcknowledgementStructureItem asi : as.getAcknowledgements()) {
				boolean acknowledged = (asi.getReceiverTimestamp() != null);
				this.listOfParticipantAcks
						.add(new DeliveryAcknowledgementVisualisation(asi
								.getSender().getContributor().getActor()
								.toString(), acknowledged));
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		int diameter = 200;
		int innerDiameter = 40;
		int centerX = 160;
		int centerY = 150;
		int numberOfParticipantsShown = (this.listOfParticipantAcks.size() > 10) ? 10
				: this.listOfParticipantAcks.size();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (numberOfParticipantsShown == 0) {
			DeliveryAcknowledgementVisualisation.getNoAckYetIcon().paintIcon(
					this, g2d, (int) centerX - innerDiameter / 2, (int) centerY - innerDiameter / 2);

			String str = "No ACKs needed yet.";
			FontMetrics fm = g2d.getFontMetrics();
			int fontWidth = fm.stringWidth(str);
			g2d.drawString(str, (int) centerX - fontWidth / 2,
					(int) centerY + 30);
		}

		for (int i = 0; i < numberOfParticipantsShown; i++) {
			double X = diameter / 2
					* Math.cos(2 * Math.PI / numberOfParticipantsShown * i);
			double Y = -diameter / 2
					* Math.sin(2 * Math.PI / numberOfParticipantsShown * i);

			listOfParticipantAcks
					.elementAt(i)
					.getIcon()
					.paintIcon(this, g2d,
							(int) X + centerX - innerDiameter / 2,
							(int) Y + centerY - innerDiameter / 2);

			// gets the string length as it is displayed in the chosen font
			FontMetrics fm = g2d.getFontMetrics();
			int fontWidth = fm.stringWidth(listOfParticipantAcks.elementAt(i)
					.getName());

			// cuts of the end of the string if it is to long
			String name = listOfParticipantAcks.elementAt(i).getName();
			if (fontWidth > 100) {
				name = name.substring(0, 10) + "...";
			}
			fontWidth = fm.stringWidth(name);

			// print name of participant centered under the icon
			g2d.setColor(Color.black);
			g2d.drawString(name, (int) X + centerX - fontWidth / 2, (int) Y
					+ centerY + 30);

			// display "..." hint to show that there are more than 10
			// participants
			if (this.listOfParticipantAcks.size() > 10) {
				g2d.setFont(new Font("Serif", Font.BOLD, 24));
				g2d.drawString("...", centerX, centerY);
			}

		}
	}

}
