/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.form.validation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import alpha.forms.memento.model.ValidatorMemento;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.TextField;

/**
 * The Class NumberValidator.
 */
public class NumberValidator implements Validator {

	/** The error message. */
	protected final String errorMessage = "'%s' is not a number.";

	/** The number type. */
	private final List<NumberType> numberType = new ArrayList<NumberType>();

	/** The error. */
	private String error = "";

	/**
	 * Instantiates a new number validator.
	 */
	public NumberValidator() {
		super();
		for (final NumberType t : NumberType.values()) {
			this.numberType.add(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#validate(alpha.forms.form.validation
	 * .ValidationContext, java.lang.Object)
	 */
	@Override
	public boolean validate(final ValidationContext ctx, final Object value) {
		boolean validates = false;
		if (this.numberType.contains(NumberType.ntInteger)) {
			try {
				Integer.parseInt((String) value);
				validates = true;
			} catch (final Exception e) {
			}
		}
		if (this.numberType.contains(NumberType.ntDouble)) {
			try {
				Double.parseDouble((String) value);
				validates = true;
			} catch (final Exception e) {
			}
		}
		if (this.numberType.contains(NumberType.ntFloat)) {
			try {
				Float.parseFloat((String) value);
				validates = true;
			} catch (final Exception e) {
			}
		}
		if (!validates) {
			this.error = String.format(this.errorMessage, value == null ? null
					: value.toString());
		}
		return validates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getOptionUI()
	 */
	@Override
	public JPanel getOptionUI() {
		final JPanel panel = new JPanel(new FlowLayout());
		for (final NumberType t : NumberType.values()) {
			final JCheckBox cb = new JCheckBox();
			cb.setText(t.getLabel());
			cb.setSelected(this.numberType.contains(t));
			cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ev) {
					if (cb.isSelected()) {
						NumberValidator.this.numberType.add(t);
					} else {
						if (NumberValidator.this.numberType.size() == 1) {
							cb.setSelected(true);
							return;
						}
						if (NumberValidator.this.numberType.contains(t)) {
							NumberValidator.this.numberType.remove(t);
						}
					}
				}
			});
			panel.add(cb);
		}
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getSettingsMap()
	 */
	@Override
	public Map<String, String> getSettingsMap() {
		final HashMap<String, String> settings = new HashMap<String, String>();
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final NumberType t : this.numberType) {
			if (!first) {
				sb.append(",");
			}
			sb.append(t.getValue());
			first = false;
		}
		settings.put("numberType", sb.toString());
		return settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#isCompatibleWith(java.lang.Class)
	 */
	@Override
	public boolean isCompatibleWith(final Class<? extends FormWidget> widgetType) {
		return widgetType.equals(TextField.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getHandle()
	 */
	@Override
	public String getHandle() {
		return "number";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Validator clone() {
		return new NumberValidator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getHandle();
	}

	/**
	 * The Enum NumberType.
	 */
	protected enum NumberType {

		/** The nt double. */
		ntDouble("Double"),

		/** The nt float. */
		ntFloat("Float"),

		/** The nt integer. */
		ntInteger("Integer");

		/** The label. */
		private String label;

		/**
		 * Instantiates a new number type.
		 * 
		 * @param label
		 *            the label
		 */
		NumberType(final String label) {
			this.label = label;
		}

		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return this.label;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public String getValue() {
			return this.name();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#createMemento()
	 */
	@Override
	public ValidatorMemento createMemento() {
		final ValidatorMemento m = new ValidatorMemento();
		m.setHandle(this.getHandle());
		m.addAttribute("numberType", this.numberType);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#setMemento(alpha.forms.memento.
	 * model.ValidatorMemento)
	 */
	@Override
	public void setMemento(final ValidatorMemento m) {
		this.numberType.clear();
		System.out.println("setMemento");
		String valueString = m.getAttributes().get("numberType").toString();
		if ((valueString != null) && (valueString.length() > 2)) {
			valueString = valueString.substring(1, valueString.length() - 1);
			final String[] values = valueString.split(",");
			for (final String value : values) {
				this.numberType.add(NumberType.valueOf(value));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getError()
	 */
	@Override
	public String getError() {
		return this.error;
	}

}
