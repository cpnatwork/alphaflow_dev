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
 * $Id: AlphaDoyenUtility.java 3994 2012-09-13 07:34:30Z uj32uvac $
 *************************************************************************/
package alpha.doyen;

import org.springframework.stereotype.Service;

import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdornmentEnumRange;
import alpha.model.adornment.ConsensusScope;
import alpha.model.cra.Doyen;
import alpha.model.cra.ImpactScope;
import alpha.model.cra.Initiator;
import alpha.model.cra.Participant;
import alpha.model.cra.PatientContact;
import alpha.model.cra.StandardTokens;
import alpha.model.cra.Token;

/**
 * The Class AlphaDoyenUtility used for creating tokens of newly generated
 * participants.
 */
@Service
public class AlphaDoyenUtility {

	/**
	 * Changes the tokens of the given participant to initial values of the
	 * process initiator, which has the tokens initiator and doyen at the
	 * beginning
	 * 
	 * @param participant
	 *            participant to be changed
	 * @return the changed participant
	 */
	public static Participant changeToInitialParticipant(Participant participant) {
		participant.readToken(StandardTokens.INITIATOR.value()).setValue(
				Initiator.INITIATOR.value());
		participant.readToken(StandardTokens.DOYEN.value()).setValue(
				Doyen.DOYEN.value());

		return participant;
	}

	/**
	 * Returns a normal participant with standard token values
	 * 
	 * @return the participant
	 */
	public static Participant getNormalParticipant() {
		final Participant participant = new Participant();

		Token token = new Token(StandardTokens.DOYEN.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM,
				ImpactScope.EXTRINSIC);
		token.setEnumRange(new AdornmentEnumRange(Doyen.stringValues()));
		participant.updateOrCreateToken(token);
		participant.readToken(StandardTokens.DOYEN.value()).setValue(
				Doyen.NON_DOYEN.value());

		token = new Token(StandardTokens.INITIATOR.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM,
				ImpactScope.INTRINSIC);
		token.setEnumRange(new AdornmentEnumRange(Initiator.stringValues()));
		participant.updateOrCreateToken(token);
		participant.readToken(StandardTokens.INITIATOR.value()).setValue(
				Initiator.NON_INITIATOR.value());

		token = new Token(StandardTokens.PATIENT_CONTACT.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM,
				ImpactScope.INTRINSIC);
		token.setEnumRange(new AdornmentEnumRange(PatientContact.stringValues()));
		participant.updateOrCreateToken(token);
		participant.readToken(StandardTokens.PATIENT_CONTACT.value()).setValue(
				PatientContact.PATIENT_CONTACT.value());

		return participant;
	}

	/**
	 * Check whether Participant owns doyen token or not.
	 * 
	 * @param participant
	 *            the participant to check
	 * @return the result of the check
	 */
	public static boolean checkDoyen(Participant participant) {
		boolean doyen = false;
		if (participant.readToken(StandardTokens.DOYEN.value()).getValue()
				.equals(Doyen.DOYEN.value())) {
			doyen = true;
		}
		return doyen;
	}

}
