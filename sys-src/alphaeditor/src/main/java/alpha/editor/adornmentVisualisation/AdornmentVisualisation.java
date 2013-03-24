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
 * $Id: AdornmentVisualisation.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.editor.adornmentVisualisation;

import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.util.logging.Logger;


import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdornmentEnumRange;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.CorpusGenericus;

import com.toedter.calendar.JDateChooser;

/**
 * The Class AdornmentVisualisation.
 */
public class AdornmentVisualisation {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdornmentVisualisation.class.getName());

	/** The Constant WIDTH. */
	private static final int WIDTH = 150;

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 20;

	/** The category visualisation. */
	private final JComboBox categoryVisualisation;

	/** The data type visualisation. */
	private final JComboBox dataTypeVisualisation;

	/** The name visualisation. */
	private final JTextField nameVisualisation;

	/** The range. */
	private HashMap<String, RangeItemVisu> range;

	/** The range visualisation. */
	private JPanel rangeVisualisation;

	/** The instance visualisation. */
	private final JCheckBox instantiateVisualisation;

	/** The state. */
	private VisualisationState state;

	/** The valueVisualisation. */
	private JComponent valueVisualisation;

	/**
	 * Instantiates a new type visu.
	 * 
	 * @param a
	 *            the a
	 * @param state
	 *            the state
	 */
	public AdornmentVisualisation(final PrototypedAdornment a,
			final VisualisationState state) {
		this.nameVisualisation = new JTextField(a.getName());
		this.nameVisualisation.setName(a.getName());
		this.nameVisualisation.setPreferredSize(new Dimension(
				AdornmentVisualisation.WIDTH, AdornmentVisualisation.HEIGHT));

		// set possible category values
		Object[] categoryValues = { ConsensusScope.GENERIC_STD };
		if (!a.getConsensusScope().equals(ConsensusScope.GENERIC_STD)) {
			// get all non-generic categories
			final ArrayList<ConsensusScope> nongenerics = new ArrayList<ConsensusScope>();
			for (final ConsensusScope c : ConsensusScope.values()) {
				if (c != ConsensusScope.GENERIC_STD) {
					nongenerics.add(c);
				}
			}
			categoryValues = nongenerics.toArray();
		}

		this.categoryVisualisation = new JComboBox(categoryValues);
		this.categoryVisualisation.setSelectedItem(a.getConsensusScope());
		this.categoryVisualisation.setName(a.getName());
		this.categoryVisualisation.setPreferredSize(new Dimension(
				AdornmentVisualisation.WIDTH, AdornmentVisualisation.HEIGHT));

		this.dataTypeVisualisation = new JComboBox(AdornmentDataType.values());
		this.dataTypeVisualisation.setSelectedItem(a.getDataType());
		this.dataTypeVisualisation.setName(a.getName());
		this.dataTypeVisualisation.setPreferredSize(new Dimension(
				AdornmentVisualisation.WIDTH, AdornmentVisualisation.HEIGHT));

		this.state = state;

		this.setValueVisualisation();

		/* initializing the range */
		if (a.getDataType().equals(AdornmentDataType.ENUM)) {
			this.range = new LinkedHashMap<String, RangeItemVisu>();
			for (final String s : a.getEnumRange().values()) {
				this.range.put(s, new RangeItemVisu(s, state));
				this.extendValueVisualisationRange(s);
			}
		}

		/* set the default value */
		this.setValue(a);

		this.instantiateVisualisation = new JCheckBox("", a.isInstance());
		this.instantiateVisualisation.setSelected(a.isInstance());
		this.instantiateVisualisation.setName(a.getName());
	}

	/**
	 * Gets the category visualisation.
	 * 
	 * @return the categoryVisualisation
	 */
	public JComboBox getCategoryVisualisation() {
		return this.categoryVisualisation;
	}

	/**
	 * Gets the data type visualisation.
	 * 
	 * @return the dataTypeVisualisation
	 */
	public JComboBox getDataTypeVisualisation() {
		return this.dataTypeVisualisation;
	}

	/**
	 * Gets the name visualisation.
	 * 
	 * @return the nameVisualisation
	 */
	public JTextField getNameVisualisation() {
		return this.nameVisualisation;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.nameVisualisation.getName();
	}

	/**
	 * Gets the range.
	 * 
	 * @return the range
	 */
	public HashMap<String, RangeItemVisu> getRange() {
		if (this.range == null) {
			this.range = new LinkedHashMap<String, RangeItemVisu>();
		}
		return this.range;
	}

	/**
	 * Gets the range visualisation.
	 * 
	 * @return the rangeVisualisation
	 */
	public JPanel getRangeVisualisation() {
		return this.rangeVisualisation;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public VisualisationState getState() {
		return this.state;
	}

	/**
	 * Sets the range.
	 * 
	 * @param range
	 *            the range to set
	 */
	public void setRange(final HashMap<String, RangeItemVisu> range) {
		this.range = range;
	}

	/**
	 * Sets the range visualisation.
	 * 
	 * @param rangeVisualisation
	 *            the rangeVisualisation to set
	 */
	public void setRangeVisualisation(final JPanel rangeVisualisation) {
		this.rangeVisualisation = rangeVisualisation;
	}

	/**
	 * Gets the instance visualisation.
	 * 
	 * @return the instance visualisation
	 */
	public JCheckBox getInstantiateVisualisation() {
		return this.instantiateVisualisation;
	}

	/**
	 * Gets the valueVisualisation.
	 * 
	 * @return the valueVisualisation
	 */
	public JComponent getValueVisualisation() {
		return this.valueVisualisation;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		final AdornmentDataType type = (AdornmentDataType) this.dataTypeVisualisation
				.getSelectedItem();
		switch (type) {
		case ENUM:
			final Object returnValue = ((JComboBox) this.valueVisualisation)
					.getSelectedItem();
			if (returnValue != null)
				return returnValue.toString();
			return null;
		case TEXTBLOCK:
			final JComponent viewPort = ((JScrollPane) this.valueVisualisation)
					.getViewport();
			return ((JTextArea) viewPort.getComponent(0)).getText();
		case TIMESTAMP:
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd.MM.yyyy HH:mm");
			final Date date = ((JDateChooser) this.valueVisualisation)
					.getDate();
			if (date != null)
				return sdf.format(date);
			return null;
		default:
			return ((JTextField) this.valueVisualisation).getText();
		}
	}

	/**
	 * Sets the value.
	 * 
	 * @param adornment
	 *            the new value
	 */
	private void setValue(final AdpAdornment adornment) {
		/* set value without the XRI/XDI characters */
		String value;
		if ((adornment.getName().equals(CorpusGenericus.ACTOR.value()))
				|| (adornment.getName().equals(CorpusGenericus.ROLE.value()))
				|| (adornment.getName().equals(CorpusGenericus.INSTITUTION
						.value()))) {
			value = adornment.getValue().substring(1);
		} else {
			value = adornment.getValue();
		}

		AdornmentVisualisation.LOGGER.finer("New value to set is '" + value
				+ "'");

		final AdornmentDataType type = (AdornmentDataType) this.dataTypeVisualisation
				.getSelectedItem();
		switch (type) {
		case ENUM:
			final JComboBox cb = (JComboBox) this.valueVisualisation;
			cb.setSelectedItem(value);
			AdornmentVisualisation.LOGGER.finer("New value is '"
					+ cb.getSelectedItem() + "'");
			break;
		case TEXTBLOCK:
			final JComponent viewPort = ((JScrollPane) this.valueVisualisation)
					.getViewport();
			final JTextArea ta = (JTextArea) viewPort.getComponent(0);
			ta.setText(value);
			AdornmentVisualisation.LOGGER.finer("New value is '" + ta.getText()
					+ "'");
			break;
		case TIMESTAMP:
			final JDateChooser dp = (JDateChooser) this.valueVisualisation;
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd.MM.yyyy HH:mm");
			try {
				if (value != null) {
					dp.setDate(sdf.parse(value));
					AdornmentVisualisation.LOGGER.finer("New value is '"
							+ dp.getDate().toString() + "'");
				}
			} catch (final ParseException e) {
				AdornmentVisualisation.LOGGER.severe(e.toString());
			}
			break;
		default:
			final JTextField tf = (JTextField) this.valueVisualisation;
			tf.setText(value);
			AdornmentVisualisation.LOGGER.finer("New value is '" + tf.getText()
					+ "'");
		}
	}

	/**
	 * Enable value visualisation.
	 * 
	 * @param enable
	 *            the enable
	 */
	public void enableValueVisualisation(final boolean enable) {
		final AdornmentDataType type = (AdornmentDataType) this.dataTypeVisualisation
				.getSelectedItem();
		switch (type) {
		case TEXTBLOCK:
			final JComponent viewPort = ((JScrollPane) this.valueVisualisation)
					.getViewport();
			((JTextArea) viewPort.getComponent(0)).setEnabled(enable);
			break;
		default:
			this.valueVisualisation.setEnabled(enable);
		}
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(final VisualisationState state) {
		this.state = state;
	}

	/**
	 * Sets the value visualisation.
	 */
	public void setValueVisualisation() {
		int heightFactor = 1;
		final AdornmentDataType type = (AdornmentDataType) this.dataTypeVisualisation
				.getSelectedItem();
		switch (type) {
		case ENUM:
			this.valueVisualisation = new JComboBox();
			break;
		case TEXTBLOCK:
			heightFactor = 4;
			final JTextArea textarea = new JTextArea();
			textarea.setLineWrap(true);
			textarea.setName(this.nameVisualisation.getName());
			final JScrollPane scrollpane = new JScrollPane(textarea,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.valueVisualisation = scrollpane;
			break;
		case TIMESTAMP:
			final JDateChooser dateTimePicker = new JDateChooser();
			dateTimePicker.setDateFormatString("dd.MM.yyyy HH:mm");
			// dateTimePicker.setTimeFormat( DateFormat.getTimeInstance(
			// DateFormat.MEDIUM ) );
			this.valueVisualisation = dateTimePicker;

			break;
		default:
			this.valueVisualisation = new JTextField();
		}
		this.valueVisualisation.setName(this.getName());
		this.valueVisualisation.setPreferredSize(new Dimension(
				AdornmentVisualisation.WIDTH, AdornmentVisualisation.HEIGHT
						* heightFactor));
	}

	/**
	 * Extend value visualisation range.
	 * 
	 * @param item
	 *            the item
	 */
	public void extendValueVisualisationRange(final Object item) {
		final JComboBox box = (JComboBox) this.valueVisualisation;
		box.addItem(item);
	}

	/**
	 * Narrow value visualisation range.
	 * 
	 * @param item
	 *            the item
	 */
	public void narrowValueVisualisationRange(final Object item) {
		final JComboBox box = (JComboBox) this.valueVisualisation;
		box.removeItem(item);
	}

	// public void updateValueVisualisationRange(Component oldItem, Component
	// newItem) {
	// JComboBox box = (JComboBox) valueVisualisation;
	// Object oldSelectedItem = box.getSelectedItem();
	// box.setSelectedItem(oldItem);
	// int index = box.getSelectedIndex();
	// box.add(newItem, index);
	// box.remove(oldItem);
	// box.setSelectedItem(oldSelectedItem);
	// }

	/**
	 * Gets the adaptive adornment.
	 * 
	 * @return the adaptive adornment
	 */
	public PrototypedAdornment getAdornment() {
		final String name = this.nameVisualisation.getText();
		AdornmentVisualisation.LOGGER
				.finer("Create an adaptive adornment with name " + name);
		final ConsensusScope category = (ConsensusScope) this.categoryVisualisation
				.getSelectedItem();
		final AdornmentDataType dataType = (AdornmentDataType) this.dataTypeVisualisation
				.getSelectedItem();
		final PrototypedAdornment a = new PrototypedAdornment(name, category,
				dataType);
		a.setInstance(this.instantiateVisualisation.isSelected());

		String value = "";
		switch (dataType) {
		case ENUM:
			final List<String> list = new ArrayList<String>();
			for (final Map.Entry<String, RangeItemVisu> e : this.range
					.entrySet()) {
				if (e.getValue().getState() != VisualisationState.DELETE) {
					list.add(e.getValue().getItem().getText());
				}
			}
			final String[] rangeItems = new String[list.size()];
			list.toArray(rangeItems);
			a.setEnumRange(new AdornmentEnumRange(rangeItems));
		default:
			value = this.getValue();
		}

		/* replenish the XRI/XDI characters to value */
		if (a.getName().equals(CorpusGenericus.ACTOR.value())) {
			value = "=" + value;
		} else if (a.getName().equals(CorpusGenericus.ROLE.value())) {
			value = "+" + value;
		} else if (a.getName().equals(CorpusGenericus.INSTITUTION.value())) {
			value = "@" + value;
		}
		a.setValue(value);
		return a;
	}

}
