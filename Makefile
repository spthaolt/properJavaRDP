JAVA_HOME=/usr/java/jdk1.5.0_10/
ANT=/usr/local/bin/ant

TARGET= rdp.jar
CERT=../cert.p12
ALIAS={B6A3DBED-747C-4119-99EA-737BB908BA56}

all:	$(TARGET)
	
$(TARGET):	
	cd properJavaRDP-1.1/;$(ANT)
	cp properJavaRDP-1.1/dist/*jar rdp/
	cd rdp;current_date=`date -R`;for f in *.html ; do \
	  cat $${f} | sed "s|<COMMENT>Built on [^<]*</COMMENT>|<COMMENT>Built on $${current_date}</COMMENT>|" >> $${f}.tmp; \
	  mv $${f}.tmp $${f}; \
	done;
	cd rdp;echo "enter pass"; \
	read pass; \
	for jar in *.jar; do \
	  if test -f ../$(CERT); \
	    then $(JAVA_HOME)/bin/jarsigner -keystore ../$(CERT) -storetype PKCS12 -keypass $${pass} -storepass $${pass} $${jar} $(ALIAS); \
	  fi ; \
	done; \
	$(JAVA_HOME)/bin/jar cvf ../$(TARGET) *; cd .. ; \
	if test -f $(CERT); \
	  then $(JAVA_HOME)/bin/jarsigner -keystore $(CERT) -storetype PKCS12 -keypass $${pass} -storepass $${pass} $(TARGET) $(ALIAS); \
	fi
	

clean:	
	rm -rf rdp/*.jar
	rm -rf $(TARGET)
	cd properJavaRDP-1.1/;$(ANT) clean
		  
