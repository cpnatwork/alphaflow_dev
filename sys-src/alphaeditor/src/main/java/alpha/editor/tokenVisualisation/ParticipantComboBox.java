package alpha.editor.tokenVisualisation;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The Class ParticipantComboBox is used as a selection tool for selecting a new
 * token bearer. Therefore the actor itself is disabled and not selectable.
 */
public class ParticipantComboBox extends JComboBox {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2182291925446216886L;

	/** The disabled index. */
	private int disabledIndex;

	/** The is disable index. */
	private boolean isDisableIndex = false;

	/**
	 * Instantiates a new participant combo box.
	 */
	public ParticipantComboBox() {
		super();

		final ListCellRenderer r = getRenderer();
		setRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c;
				if (disabledIndex == index) {
					c = r.getListCellRendererComponent(list, value, index,
							false, false);
					c.setEnabled(false);
				} else {
					c = r.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
					c.setEnabled(true);
				}
				return c;
			}
		});

	}

	/**
	 * Sets the disabled index.
	 * 
	 * @param disabledIndex
	 *            the new disabled index
	 */
	public void setDisabledIndex(int disabledIndex) {
		this.disabledIndex = disabledIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComboBox#setPopupVisible(boolean)
	 */
	@Override
	public void setPopupVisible(boolean value) {
		if (!value && isDisableIndex) {
			isDisableIndex = false;
		} else {
			super.setPopupVisible(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComboBox#setSelectedIndex(int)
	 */
	@Override
	public void setSelectedIndex(int index) {
		if (disabledIndex == index) {
			isDisableIndex = true;
		} else {
			super.setSelectedIndex(index);
		}
	}

}
