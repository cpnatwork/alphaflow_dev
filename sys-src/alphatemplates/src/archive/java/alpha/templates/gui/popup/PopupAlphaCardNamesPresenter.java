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

import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.templates.gui.AlphaCardNamesPresenter;

// TODO: Auto-generated Javadoc
/**
 * The Class PopupAlphaCardNamesPresenter.
 */
public class PopupAlphaCardNamesPresenter implements AlphaCardNamesPresenter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.templates.gui.AlphaCardNamesPresenter#createAlphaCardMap(java.util
	 * .List, java.lang.String)
	 */
	public Map<AlphaCardDescriptor, Boolean> createAlphaCardMap(
			List<AlphaCardDescriptor> acdlist, String output) {

		ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

		for (int i = 0; i < acdlist.size(); i++) {

			if (acdlist.get(i).readAdornment(CorpusGenericus.TITLE.value()) != null) {
				checkBoxes.add(new JCheckBox(acdlist.get(i).readAdornment(
						CorpusGenericus.TITLE.value()).getValue(), true));
			} else {
				checkBoxes.add(new JCheckBox(CorpusGenericus.TITLE.value(),
						true));
			}
		}
		JOptionPane.showConfirmDialog(new JFrame(), checkBoxes.toArray(),
				output, JOptionPane.OK_CANCEL_OPTION);

		HashMap<AlphaCardDescriptor, Boolean> hm = new HashMap<AlphaCardDescriptor, Boolean>(
				checkBoxes.size());

		for (int i = 0; i < checkBoxes.size(); i++) {
			hm.put(acdlist.get(i), checkBoxes.get(i).isSelected());
		}

		return hm;
	}

}
