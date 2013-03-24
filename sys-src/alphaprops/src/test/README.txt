TESTING

StartAndKeepAliveDroolsTest.java and StartAndKeepAliveDroolsTest2.java are designed to simulate a Drools instance, which after initialization is kept alive in order to run the EditorTest*.java tests.

Adjust local configuration of the pom.xml, in order to be able to run the tests.

Upon starting a new Drools instance, the following order of method calls must be kept:
1. alphaPropsFacade.setVerVarStore(vvs);
2. alphaPropsFacade.initializeConfig(listeningPort);
3. alphaPropsFacade.addObservers(observerAlphaCard, observerRest);
4. alphaPropsFacade.initializeModel(doc);
