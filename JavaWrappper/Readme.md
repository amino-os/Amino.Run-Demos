# JavaWrappper

This is a complete JavaWrapper working on top of edgeCV.

### Requirements

   * apache-tomcat-9.0.10
   * openjdk version "1.8.0_171"

### Steps to run
   * Download apache-tomcat-9.0.10 and extract it.
   * Change the directory to apache-tomcat-9.0.10/bin and start Server using the following command as shown below.
	``$ sh startup.sh``
   * Copy the JavaWrapper folder into apache-tomcat-9.0.10/webapps/
   * Go to JavaWrappper/WEB-INF/classes and run the following command to compile all the java files. While compiling servlet, classpath of "servlet-api.jar" has to be specified. Either you use "-cp" or "-classpath" option as shown below.
	``$ javac -cp "../lib/servlet-api.jar" *.java ``
   * Go to the browser and run the following URL as shown below.
	``http://localhost:8080/JavaWrappper/index.jsp``
   * To stop server go the directory apache-tomcat-9.0.10/bin and run the following command as shown below.
	``$ sh shutdown.sh``
