mvn install:install-file -Dfile=proguard/proguard.jar -DartifactId=proguard -DgroupId=net.sf.proguard -Dpackaging=jar -Dversion=4.6
mvn install:install-file -Dfile=bouncycastle/bcprov.jar -DartifactId=bcprov -DgroupId=org.bouncycastle -Dpackaging=jar -Dversion=1.47
mvn install:install-file -Dfile=bouncycastle/bcpg.jar -DartifactId=bcpg -DgroupId=org.bouncycastle -Dpackaging=jar -Dversion=1.47
mvn install:install-file -Dfile=javamail/mail.jar -DartifactId=mail -DgroupId=javax.mail -Dpackaging=jar -Dversion=1.44

