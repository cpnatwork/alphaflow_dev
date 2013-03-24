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
 * $Id: InsertionUtility.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor.insertionutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.drools.runtime.StatefulKnowledgeSession;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.apa.Priority;
import alpha.model.cra.ContributorID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.overnet.event.ParallelJoinCallback;
import alpha.overnet.event.ParallelJoinSynchronisation;
import alpha.overnet.event.ParticipantJoinEvent;
import alpha.overnet.event.SequentialJoinCallback;
import alpha.overnet.event.SequentialJoinSynchronisation;
import alpha.util.StringWrapper;
import alpha.util.XmlBinder;

/**
 * Shows a gui dialog for inserting serialized objects into the local drools.
 * 
 * {@link StatefulKnowledgeSession}.
 */
public class InsertionUtility {

	/** The classes. */
	Class[] classes = new Class[] { AlphaCardDescriptor.class,
			AddAlphaCardEvent.class, ParticipantJoinEvent.class,
			ChangeAlphaCardDescriptorEvent.class, ChangePayloadEvent.class,
			Participant.class, AlphaCardID.class, AlphaCardRelationship.class,
			Payload.class, Priority.class, ContributorID.class,
			ObjectUnderConsideration.class, StringWrapper.class,
			SequentialJoinCallback.class, SequentialJoinSynchronisation.class,
			ParallelJoinCallback.class, ParallelJoinSynchronisation.class,
			APAPayload.class, PSAPayload.class };

	/** The parent. */
	private final JPanel parent;

	/** The apf. */
	private final AlphaPropsFacade apf;

	/**
	 * Instantiates a new {@link InsertionUtility}.
	 * 
	 * @param parent
	 *            the parent gui window
	 * @param apf
	 *            the global {@link AlphaPropsFacade}
	 */
	public InsertionUtility(final JPanel parent, final AlphaPropsFacade apf) {
		this.parent = parent;
		this.apf = apf;
	}

	/**
	 * Shows the insertion dialog.
	 */
	public void showInsertionDialog() {
		final JFileChooser fc = new JFileChooser(new File("."));
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new InsertionFileFilter());

		final int ret = fc.showOpenDialog(this.parent);

		if (ret == JFileChooser.APPROVE_OPTION) {
			final XmlBinder binder = new XmlBinder();
			try {

				final Object obj = binder
						.load(new FileInputStream(fc.getSelectedFile()),
								this.classes);
				this.apf.insertIntoDrools(obj);
			} catch (final FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
