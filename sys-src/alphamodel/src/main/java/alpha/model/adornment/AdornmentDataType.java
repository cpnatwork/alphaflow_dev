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
 * $Id: AdornmentDataType.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.model.adornment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

import java.util.logging.Logger;


/**
 * This defines the data types a dynamic Adornment can adopt.
 *
 * @author cpn
 * @version $Id: AdornmentDataType.java 3597 2012-02-20 15:13:03Z cpn $
 */
@XmlEnum
public enum AdornmentDataType {

	/** The INTEGER. */
	@XmlEnumValue("integer")
	INTEGER("Integer") {
		@Override
		public boolean validate(final String value,
				final AdornmentEnumRange adaptiveAdornmentEnumRange) {
			try {
				Integer.parseInt(value);
			} catch (final NumberFormatException nfe) {
				AdornmentDataType.LOGGER.severe("Error: " + nfe);
				return false;
			}
			return true;
		}
	},

	/** The STRING. */
	@XmlEnumValue("string")
	STRING("String") {
		@Override
		public boolean validate(final String value,
				final AdornmentEnumRange adaptiveAdornmentEnumRange) {
			/* there are no invalid values for a String */
			return true;
		}
	},

	/** The STRING. */
	@XmlEnumValue("textblock")
	TEXTBLOCK("Textblock") { /* equal to String, but greater display area */
		@Override
		public boolean validate(final String value,
				final AdornmentEnumRange adaptiveAdornmentEnumRange) {
			/* there are no invalid values for a String */
			return true;
		}
	},

	/** The ENUM. */
	@XmlEnumValue("enum")
	ENUM("Enum") { /* ENUM contains also Boolean */
		@Override
		public boolean validate(final String value,
				final AdornmentEnumRange adaptiveAdornmentEnumRange) {
			if (adaptiveAdornmentEnumRange.exists(value))
				return true;
			AdornmentDataType.LOGGER.warning("Found NO range match for " + value);
			return false;
		}
	},

	/** The TIMESTAMP. */
	@XmlEnumValue("timestamp")
	TIMESTAMP("Timestamp") {
		@Override
		public boolean validate(final String value,
				final AdornmentEnumRange adaptiveAdornmentEnumRange) {
			// date validation using SimpleDateFormat:
			// it will take a string and make sure it's in the proper format as
			// defined by
			// you, and it will also make sure that it's a legal date
			// SOURCE:
			// http://www.dreamincode.net/forums/topic/14886-date-validation-using-simpledateformat/

			// set date format, this can be changed to whatever format
			// you want, MM-dd-yyyy, MM.dd.yyyy, dd.MM.yyyy etc.
			// you can read more about it here:
			// http://java.sun.com/j2se/1.4.2/docs/api/index.html

			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd.MM.yyyy HH:mm");

			// declare and initialize testDate variable, this is what will hold
			// our converted string

			Date testDate = null;

			// we will now try to parse the string into date form
			try {
				testDate = sdf.parse(value);
			}

			// if the format of the string provided doesn't match the format we
			// declared in SimpleDateFormat() we will get an exception

			catch (final ParseException e) {
				// errorMessage =
				// "the date you provided is in an invalid date format.";
				AdornmentDataType.LOGGER
						.warning("the date you provided is in an invalid date format.");
				return false;
			}

			// dateformat.parse will accept any date as long as it's in the
			// format
			// you defined, it simply rolls dates over, for example, december 32
			// becomes jan 1 and december 0 becomes november 30
			// This statement will make sure that once the string
			// has been checked for proper formatting that the date is still the
			// date that was entered, if it's not, we assume that the date is
			// invalid

			if (!sdf.format(testDate).equals(value)) {
				// errorMessage = "The date that you provided is invalid.";
				AdornmentDataType.LOGGER
						.warning("The date that you provided is invalid.");
				return false;
			}

			// if we make it to here without getting an error it is assumed that
			// the date was a valid one and that it's in the proper format

			return true;

		}
	};

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdornmentDataType.class.getName());

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new adaptive adornment data type.
	 * 
	 * @param v
	 *            the v
	 */
	AdornmentDataType(final String v) {
		this.value = v;
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
	 * @param v
	 *            the v
	 * @return the adaptive adornment data type
	 */
	public static AdornmentDataType fromValue(final String v) {
		for (final AdornmentDataType c : AdornmentDataType.values()) {
			if (c.value.equalsIgnoreCase(v))
				return c;
		}
		throw new IllegalArgumentException(v.toString());
	}

	/**
	 * Validate.
	 *
	 * @param value
	 *            the value
	 * @param adaptiveAdornmentEnumRange
	 *            the adaptive adornment enum range
	 * @return true, if successful
	 */
	public abstract boolean validate(String value,
			AdornmentEnumRange adaptiveAdornmentEnumRange);

}
