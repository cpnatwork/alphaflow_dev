package alpha.model.cra;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This defines the possible impact scopes a token can have.
 */
@XmlEnum
@XmlRootElement(name = "impactScope")
public enum ImpactScope {

	/** The INTRINSIC. */
	@XmlEnumValue("intrinsic")
	INTRINSIC("INTRINSIC"),
	/** The EXTRINSIC. */
	@XmlEnumValue("extrinsic")
	EXTRINSIC("EXTRINSIC");
	
	/** The value. */
	private final String value;

	/**
	 * Instantiates a new scope for a token.
	 * 
	 * @param name
	 *            the name of the scope
	 */
	ImpactScope(final String name) {
		this.value = name;
	}

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() {
		return this.value;
	}
	
	/**
	 * From value.
	 *
	 * @param name
	 *            the corresponding name to an impact scope
	 * @return the related scope of the token
	 */
	public static ImpactScope fromValue(final String name) {
		for (final ImpactScope c : ImpactScope.values()) {
			if (c.value.equalsIgnoreCase(name))
				return c;
		}
		throw new IllegalArgumentException(name.toString());
	}
}
