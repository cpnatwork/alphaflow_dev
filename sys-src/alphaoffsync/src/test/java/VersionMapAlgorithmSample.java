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
 * $Id: VersionMapAlgorithmSample.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
import java.io.File;

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;
import alpha.offsync.time.VersionMapHistoryUtility;
import alpha.vvs.WorkspaceImpl;

/**
 * Provides a worst-case scenario to demonstrate the.
 * 
 * {@link VersionMapHistoryUtility}.
 */
public class VersionMapAlgorithmSample {

	/**
	 * Execute sample scenario.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String args[]) {
		VersionMapHistoryUtility vvu = null;

		final String testPath = ".";
		final File testDirectory = new File(testPath);
		final boolean mkdir = testDirectory.mkdirs();
		System.out.println("mkdirs: " + mkdir);
		VersionMapAlgorithmSample.deleteDirectory(new File(testDirectory,
				".hydra"));
		vvu = new VersionMapHistoryUtility(new WorkspaceImpl(testDirectory),
				true);

		final AlphaCardDescriptor acd0 = new AlphaCardDescriptor();
		acd0.setId(new AlphaCardID("test", "1"));
		acd0.setVersionMap(new VersionMap("B", 1L));

		final PrototypedAdornment ad0 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad0.setValue("txt");
		acd0.updateOrCreateAdornment(ad0);
		final Payload p0 = new Payload();
		final byte[] test = "0".getBytes();
		p0.setContent(test);

		final AlphaCardDescriptor acd1 = new AlphaCardDescriptor();
		acd1.setId(new AlphaCardID("test", "1"));
		acd1.setVersionMap(new VersionMap("B", 5L));

		final PrototypedAdornment ad1 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad1.setValue("txt");
		acd1.updateOrCreateAdornment(ad1);
		final Payload p1 = new Payload();
		p1.setContent("1".getBytes());

		final AlphaCardDescriptor acd2 = new AlphaCardDescriptor();
		acd2.setId(new AlphaCardID("test", "1"));
		acd2.setVersionMap(new VersionMap("B", 3L));

		final PrototypedAdornment ad2 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad2.setValue("txt");
		acd2.updateOrCreateAdornment(ad2);
		final Payload p2 = new Payload();
		p2.setContent("2".getBytes());

		final AlphaCardDescriptor acd3 = new AlphaCardDescriptor();
		acd3.setId(new AlphaCardID("test", "1"));
		acd3.setVersionMap(new VersionMap("B", 2L));
		// acd3.getVersionVector().putEntry("B", 1L);

		final PrototypedAdornment ad3 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad3.setValue("txt");
		acd3.updateOrCreateAdornment(ad3);
		final Payload p3 = new Payload();
		p3.setContent("3".getBytes());

		final AlphaCardDescriptor acd4 = new AlphaCardDescriptor();
		acd4.setId(new AlphaCardID("test", "1"));
		acd4.setVersionMap(new VersionMap("C", 2L));
		acd4.getVersionMap().putEntry("B", 1L);

		final PrototypedAdornment ad4 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad4.setValue("txt");
		acd4.updateOrCreateAdornment(ad4);
		final Payload p4 = new Payload();
		p4.setContent("4".getBytes());

		// AlphaCardDescriptor acd5 = new AlphaCardDescriptor();
		// acd5.setId(new AlphaCardID("test", "1"));
		// acd5.setVersionVector(new VersionVector("C", 3L));
		// acd5.change = 5;

		final AlphaCardDescriptor acd5 = new AlphaCardDescriptor();
		acd5.setId(new AlphaCardID("test", "1"));
		acd5.setVersionMap(new VersionMap("A", 2L));

		final PrototypedAdornment ad5 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad5.setValue("txt");
		acd5.updateOrCreateAdornment(ad5);
		final Payload p5 = new Payload();
		p5.setContent("5".getBytes());

		final AlphaCardDescriptor acd6 = new AlphaCardDescriptor();
		acd6.setId(new AlphaCardID("test", "1"));
		acd6.setVersionMap(new VersionMap("C", 1L));

		final PrototypedAdornment ad6 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad6.setValue("txt");
		acd6.updateOrCreateAdornment(ad6);
		final Payload p6 = new Payload();
		p6.setContent("6".getBytes());

		final AlphaCardDescriptor acd7 = new AlphaCardDescriptor();
		acd7.setId(new AlphaCardID("test", "1"));
		acd7.setVersionMap(new VersionMap("D", 2L));

		final PrototypedAdornment ad7 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad7.setValue("txt");
		acd7.updateOrCreateAdornment(ad7);
		final Payload p7 = new Payload();
		p7.setContent("7".getBytes());

		final AlphaCardDescriptor acd8 = new AlphaCardDescriptor();
		acd8.setId(new AlphaCardID("test", "1"));
		acd8.setVersionMap(new VersionMap("D", 1L));

		final PrototypedAdornment ad8 = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		ad8.setValue("txt");
		acd8.updateOrCreateAdornment(ad8);
		final Payload p8 = new Payload();
		p8.setContent("8".getBytes());

		// vvu.hvs.deleteLogicalUnit("1");
		// File file = new
		// File("E:/Users/sianwahl/Documents/test/1/desc/Descriptor.xml");
		// file.delete();
		try {
			vvu.insertIntoHistory(acd0, p0);
			vvu.insertIntoHistory(acd1, p1);
			vvu.insertIntoHistory(acd2, p2);
			vvu.insertIntoHistory(acd3, p3);
			//
			vvu.insertIntoHistory(acd4, p4);
			vvu.insertIntoHistory(acd5, p5);
			//
			vvu.insertIntoHistory(acd6, p6);
			vvu.insertIntoHistory(acd7, p7);
			vvu.insertIntoHistory(acd8, p8);

		} catch (final Exception e) {
			e.printStackTrace();
		}
		// System.out.println(vvu.hist.getHeadVersion(acd8).getSystemPathPosition()+"("+vvu.hist.getHeadVersion(acd8).getValidPathPosition()+")");
		// Version version = vvu.hist.getHeadVersion(acd8);
		// version = version.findPreviousValidVersion();
		// vvu.hist.revertToVersion(acd8, version);
		// System.out.println(vvu.hist.getCurrentVersion(acd8).getSystemPathPosition());

		System.out.println("sysLog--------------------------");
		System.out.println(vvu.getHist().getHistoryLog(acd0, true));
		System.out.println("valLog--------------------------");
		System.out.println(vvu.getHist().getHistoryLog(acd0, false));

	}

	/**
	 * Recursively deletes a directory and all its contents.
	 * 
	 * @param targetDirectory
	 *            the directory to be deleted
	 */
	public static void deleteDirectory(final File targetDirectory) {
		if (targetDirectory.isDirectory()) {
			for (final File child : targetDirectory.listFiles()) {
				VersionMapAlgorithmSample.deleteDirectory(child);
			}
		}
		if (!targetDirectory.delete()) {
			System.out.println("Could not clean workspace.");
		}
	}
}
