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
 * $Id: ParticipantTableModel.java 3994 2012-09-13 07:34:30Z uj32uvac $
 *************************************************************************/
package alpha.editor;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.springframework.stereotype.Component;

import alpha.model.cra.Participant;
import alpha.model.cra.Token;

/**
 * The Class ParticipantTableModel holds the list of participants and is used as
 * model for the table view in the editor as well as model for the combobox in
 * the tokenDialog holding all participants to forward the token to.
 * 
 * @author cpn
 * @version $Id: ParticipantTableModel.java 3994 2012-09-13 07:34:30Z uj32uvac $
 */
@Component
public class ParticipantTableModel implements TableModel, ComboBoxModel {

	/** The listeners. */
	private final Vector<TableModelListener> listeners = new Vector<TableModelListener>();

	/** The participants. */
	private Vector<Participant> participants = new Vector<Participant>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	/** {@inheritDoc} */
	@Override
	public void addTableModelListener(final TableModelListener l) {
		this.listeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	/** {@inheritDoc} */
	@Override
	public Class<String> getColumnClass(final int arg0) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	/** {@inheritDoc} */
	@Override
	public int getColumnCount() {
		return 7;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	/** {@inheritDoc} */
	@Override
	public String getColumnName(final int column) {
		final String[] header = { "Actor", "Role", "Institution", "Email",
				"Node", "Port", "Tokens" };
		return header[column];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	/** {@inheritDoc} */
	@Override
	public int getRowCount() {
		return this.participants.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	/** {@inheritDoc} */
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		//returns the correct values for each column, used for table view
		final Participant participant = this.participants.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return participant.getContributor().getActor();
		case 1:
			return participant.getContributor().getRole();
		case 2:
			return participant.getContributor().getInstitution();
		case 3:
			return participant.getNode().getEmailAddress();
		case 4:
			return participant.getNode().getHost();
		case 5:
			return participant.getNode().getPort();
		case 6:
			return participant.readTokens();
		default:
			return "";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean isCellEditable(final int arg0, final int arg1) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	/** {@inheritDoc} */
	@Override
	public void removeTableModelListener(final TableModelListener l) {
		this.listeners.remove(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	/** {@inheritDoc} */
	@Override
	public void setValueAt(final Object arg0, final int arg1, final int arg2) {
		//no set for table needed
	}

	/**
	 * Update data.
	 * 
	 * @param participantSet
	 *            the participant set
	 */
	public void updateData(final Set<Participant> participantSet) {
		this.participants = new Vector<Participant>(participantSet);

		// create table changed event
		final TableModelEvent e = new TableModelEvent(this);

		// send it
		for (int i = 0, n = this.listeners.size(); i < n; i++) {
			this.listeners.get(i).tableChanged(e);
		}

	}

	/** The current participant. */
	private Object currentParticipant;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	/** {@inheritDoc} */
	@Override
	public int getSize() {
		return this.participants.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxModel#getSelectedItem()
	 */
	/** {@inheritDoc} */
	@Override
	public Object getSelectedItem() {
		return this.currentParticipant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public void setSelectedItem(final Object selectedParticipant) {
		this.currentParticipant = selectedParticipant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener
	 * )
	 */
	/** {@inheritDoc} */
	@Override
	public void addListDataListener(final ListDataListener l) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	/** {@inheritDoc} */
	@Override
	public Object getElementAt(final int rowIndex) {
		final Participant participant = this.participants.get(rowIndex);
		return participant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.
	 * ListDataListener)
	 */
	/** {@inheritDoc} */
	@Override
	public void removeListDataListener(final ListDataListener l) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the tokens of the selected participant.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @return the token
	 */
	public List<Token> getToken(final int rowIndex) {
		final Participant participant = this.participants.get(rowIndex);
		return participant.readTokens();
	}

	/**
	 * Returns the index of the participant (used in Combobox)
	 * @param participant participant to question
	 * @return the index of the selected participant
	 */
	public int getIndexOfParticipant(Participant participant) {
		return participants.indexOf(participant);
	}

}
