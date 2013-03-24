package alpha.props;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.cra.Participant;
import alpha.model.deliveryacknowledgment.LocalTimestamp;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;
import alpha.overnet.event.DeliveryAcknowledgement;

/**
 * The Class AcknowledgementUtility performs at the connection point of the rule
 * engine and the application itself. It holds routines to handle and generate
 * AcknowledgementDeliveries.
 */
public class AcknowledgementUtility {

	/** The global {@link AlphaPropsFacade}. */
	private final AlphaPropsFacade apf;

	/**
	 * Instantiates a new token utility.
	 * 
	 * @param apf
	 *            the apf
	 */
	public AcknowledgementUtility(final AlphaPropsFacade apf) {
		this.apf = apf;
	}

	/**
	 * Generate delivery acknowledgement for given values.
	 * 
	 * @param acid
	 *            the acid
	 * @param versionMap
	 *            the version map
	 * @return the delivery acknowledgement
	 */
	public DeliveryAcknowledgement generateDeliveryAcknowledgement(
			AlphaCardID acid, VersionMap versionMap) {

		DeliveryAcknowledgement da = new DeliveryAcknowledgement(acid,
				versionMap, new LocalTimestamp(),
				this.apf.getParticipantByActor(this.apf.getAlphaConfig()
						.getLocalNodeID().getContributor().getActor()));

		apf.insertIntoDrools(da);

		return da;
	}

	/**
	 * Handle delivery acknowledgement.
	 * 
	 * @param da
	 *            the delivery acknowledgement
	 */
	public void handleDeliveryAcknowledgement(DeliveryAcknowledgement da) {

		final AlphaCardID acid = da.getAcid();
		final Participant sender = da.getSender();
		final LocalTimestamp senderTimestamp = da.getSenderTimestamp();
		final VersionMap versionMap = da.getVersionMap();

		apf.updateAcknowledgementStructure(acid, versionMap, sender,
				senderTimestamp, new LocalTimestamp());
	}

	/**
	 * Initialize acknowledgement structure.
	 * 
	 * @param acd
	 *            the acd
	 */
	public void initializeAcknowledgementStructure(AlphaCardDescriptor acd) {
		apf.initializeAcknowledgementStructure(acd.getId(), acd.getVersionMap());
	}
}
