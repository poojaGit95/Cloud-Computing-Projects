There are 3 ways to run MyCityApp application:

1. Simplest way using shell file
    -> Run the execute.sh file and pass a city name
        Eg: 
	./execute.sh LONDON

2. Running jar file:
  -> Navigate to target folder which contains the jar file (jar file with dependencies) and execute command to run jar file
      Steps:
	cd Program2/MyCityApp/target
	java -jar MyCityApp-1.0-SNAPSHOT-jar-with-dependencies.jar  “new york”


3. Running using maven
 -> Prerequisite: Should have maven installed
     Steps:
     Navigate to src folder:
     	cd Program2/MyCityApp
     Run following maven command to compile files
          mvn clean install
     Run following command to execute the program
	mvn exec:java -Dexec.mainClass=com.cloudcomputing.project.execute.Main -Dexec.arguments=“New York”

          

      


