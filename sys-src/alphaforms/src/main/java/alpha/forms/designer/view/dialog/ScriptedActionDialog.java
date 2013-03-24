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
package alpha.forms.designer.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import alpha.forms.form.event.ActionFactory;
import alpha.forms.form.event.ScriptedAction;

/**
 * The Class ScriptedActionDialog.
 */
public class ScriptedActionDialog extends JDialog implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5913221713200413285L;

	/** The action. */
	private ScriptedAction action;

	/** The code. */
	private final JSEditorPane code;

	/** The save. */
	private final JButton save;

	/** The cancel. */
	private final JButton cancel;

	/** The was cancled. */
	private boolean wasCancled;

	/** The was saved. */
	private boolean wasSaved;

	/**
	 * Creates the.
	 * 
	 * @param action
	 *            the action
	 * @return the scripted action dialog
	 */
	public static ScriptedActionDialog create(final ScriptedAction action) {
		final ScriptedActionDialog dialog = new ScriptedActionDialog();
		dialog.setAction(action);
		return dialog;
	}

	/**
	 * Instantiates a new scripted action dialog.
	 */
	public ScriptedActionDialog() {
		super((Frame) null, "Script Action:", true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setResizable(true);

		this.code = new JSEditorPane();
		this.code.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.code.setSize(200, 300);

		final JPanel buttonPanel = new JPanel(new FlowLayout());

		this.save = new JButton("Save");
		this.save.addActionListener(this);
		this.save.setActionCommand("save");
		this.cancel = new JButton("Cancel");
		this.cancel.addActionListener(this);
		this.cancel.setActionCommand("cancel");

		buttonPanel.add(this.save);
		buttonPanel.add(this.cancel);

		this.add(new JScrollPane(this.code), BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(this.save);
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(final ScriptedAction action) {
		this.action = action;
		this.code.setText(action.getScriptCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean flag) {
		if (flag == true) {
			this.pack();
			this.wasCancled = false;
			this.wasSaved = false;
		}
		super.setVisible(flag);
	}

	/**
	 * Was cancled.
	 * 
	 * @return true, if successful
	 */
	public boolean wasCancled() {
		return this.wasCancled;
	}

	/**
	 * Was saved.
	 * 
	 * @return true, if successful
	 */
	public boolean wasSaved() {
		return this.wasSaved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			final String source = this.code.getText();
			final ScriptEngine engine = ActionFactory.getInstance()
					.getScriptEngine();
			try {
				engine.eval("function stest(widget, form) {" + source + "}");
				this.setVisible(false);
				this.wasSaved = true;
				this.action.setScriptCode(this.code.getText());
			} catch (final ScriptException ex) {
				JOptionPane.showMessageDialog(
						null,
						"There is an error in the script on line "
								+ ex.getLineNumber() + ":\n" + ex.getMessage(),
						"Script Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals("cancel")) {
			this.setVisible(false);
			this.wasCancled = true;
		}
	}
}
