package alpha.props.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

/**
 * The Class DroolsTest. Checks whether the drools rule file can be compiled.
 * Saves a lot of time while coding as not the whole application has to be
 * compiled.
 */
public class DroolsTest {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KnowledgeBuilderConfiguration conf = KnowledgeBuilderFactory
				.newKnowledgeBuilderConfiguration();
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder(conf);

		File drlFile = new File("src/main/resources/rules/alphaprops-rules.drl");
		FileInputStream fis;
		try {
			fis = new FileInputStream(drlFile);
			kbuilder.add(ResourceFactory.newInputStreamResource(fis),
					ResourceType.DRL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (kbuilder.hasErrors()) {
			System.err.println(kbuilder.getErrors().toString());
		}

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		//StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

	}

}
