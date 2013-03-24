/*
 * 
 */
package alpha.templates.gui.popup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import alpha.templates.gui.AdornmentNamesPresenter;

/**
 * The Class AdornmentPresenter.
 */
public class PopupAdornmentNamesPresenter implements AdornmentNamesPresenter {

	/**
	 * shows a popup and creates a String -> Boolean hashmap : adornment ->
	 * isSelected.
	 * 
	 * @param adornmentAskList
	 *            the adornment ask list
	 * @param output
	 *            the output
	 * @return adornment checkbox list
	 */
	public Map<String, Boolean> createAdornmentMap(
			List<String> adornmentAskList, String output) {
		// the list of adornment checkboxes, where user may select adornment
		// names
		ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

		for (String adornmentName : adornmentAskList) {
			checkBoxes.add(new JCheckBox(adornmentName, true));
		}

		JOptionPane.showConfirmDialog(new JFrame(), checkBoxes.toArray(),
				output, JOptionPane.OK_CANCEL_OPTION);

		HashMap<String, Boolean> adornmentMap = new HashMap<String, Boolean>();

		for (int i = 0; i < checkBoxes.size(); i++) {
			adornmentMap.put(checkBoxes.get(i).getText(), checkBoxes.get(i)
					.isSelected());
		}

		return adornmentMap;
	}

}
