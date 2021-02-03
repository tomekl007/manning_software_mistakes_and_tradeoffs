## The structure of the code
Each chapter has a dedicated folder for its code. 
In the [src directory](src/main/java/com/tomekl007) you will find the production code grouped per chatper.
In the [test directory](src/test/java/com/tomekl007) you will find the java test code. 
Some chapters are using code written in Scala language (i.e. chapter 05), you will find it in the [scala test directory](src/test/scala/com/tomekl007).

## How to run the code
Once you do a git clone on the repository, you can build the project by using [mvn](http://maven.apache.org/install.html).
To compile the project, run the `mvn compile`. To run all tests, run `mvn test` command. 

You can also import your code into IDE. In the IntelliJ IDEA, you need to [open the project](https://www.jetbrains.com/help/idea/maven-support.html#maven_import_project_start). 
It will automatically import it as a maven project and fetch all needed dependencies.    
  
