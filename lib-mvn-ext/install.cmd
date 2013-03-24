cmd /D/C mvn install:install-file -Dfile=proguard/proguard.jar -DartifactId=proguard -DgroupId=net.sf.proguard -Dpackaging=jar -Dversion=4.6

cmd /D/C mvn install:install-file -Dfile=bouncycastle/bcprov.jar -DartifactId=bcprov -DgroupId=org.bouncycastle -Dpackaging=jar -Dversion=1.47
cmd /D/C mvn install:install-file -Dfile=bouncycastle/bcpg.jar -DartifactId=bcpg -DgroupId=org.bouncycastle -Dpackaging=jar -Dversion=1.47

cmd /D/C mvn install:install-file -Dfile=javamail/mail.jar -DartifactId=mail -DgroupId=javax.mail -Dpackaging=jar -Dversion=1.44

GOTO :eof

; HOW TO MAKE AN IMPORTING POM FROM THESE LINES?
; Search:  -DartifactId=([a-zA-Z0-9.]*) -DgroupId=([a-zA-Z0-9.]*) -Dversion=([a-zA-Z0-9.]*)
; Replace: <dependency><groupId>\2</groupId><artifactId>\1</artifactId><version>\3</version></dependency>
