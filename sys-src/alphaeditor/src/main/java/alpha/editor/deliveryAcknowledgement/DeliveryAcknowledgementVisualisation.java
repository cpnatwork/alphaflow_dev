package alpha.editor.deliveryAcknowledgement;

import javax.swing.ImageIcon;

// TODO: Auto-generated Javadoc
/**
 * The Class DeliveryAcknowledgementVisualisation.
 */
public class DeliveryAcknowledgementVisualisation {
	
	/** The name. */
	private String name;

	/** The acknowledged. */
	private boolean acknowledged;
	
	/**
	 * Instantiates a new delivery acknowledgement visualisation.
	 *
	 * @param name the name
	 * @param acknowledged the acknowledged
	 */
	public DeliveryAcknowledgementVisualisation(String name,
			boolean acknowledged) {
		super();
		this.name = name;
		this.acknowledged = acknowledged;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if is acknowledged.
	 *
	 * @return true, if is acknowledged
	 */
	public boolean isAcknowledged() {
		return acknowledged;
	}

	/**
	 * Sets the acknowledged.
	 *
	 * @param acknowledged the new acknowledged
	 */
	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		if (isAcknowledged()) {
			return DeliveryAcknowledgementVisualisation.getIconResource("participant_green.png");
		}
		return DeliveryAcknowledgementVisualisation.getIconResource("participant_grey.png");
	}
	
	/**
	 * Gets the no ack yet icon.
	 *
	 * @return the no ack yet icon
	 */
	public static ImageIcon getNoAckYetIcon(){
		return DeliveryAcknowledgementVisualisation.getIconResource("noAckYet.png");
	}

	/**
	 * Gets the icon resource.
	 * 
	 * @param name
	 *            the name
	 * @return the icon resource
	 */
	private static ImageIcon getIconResource(final String name) {
		return new ImageIcon(DeliveryAcknowledgementVisualisation.class.getClassLoader()
				.getResource("icons/" + name));
		
	}

}
