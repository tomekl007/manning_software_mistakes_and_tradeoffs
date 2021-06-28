## The structure of the code
Each chapter has a dedicated folder for its code. 
In the [src directory](src/main/java/com/tomekl007) you will find the production code grouped per chatper.
In the [test directory](src/test/java/com/tomekl007) you will find the java test code. 

Some chapters are using code written in Scala language (i.e. chapter 05), you will find it in the dedicated [scala_project](scala_project).
You can import the scala_project directly to your IDE, or run the scala test, in the context of this project:
```
cd scala_project
mvn clean test
mvn gatling:test
``` 

## How to run the code
Once you do a git clone on the repository, you can build the project by using [mvn](http://maven.apache.org/install.html).
To compile the project, run the `mvn compile`. To run all tests, run `mvn test` command. 

You can also import your code into IDE. In the IntelliJ IDEA, you need to [open the project](https://www.jetbrains.com/help/idea/maven-support.html#maven_import_project_start). 
It will automatically import it as a maven project and fetch all needed dependencies.

## Code formatting

This project uses automatic code and XML format plugins to keep the consistent code style.
To format the Java code, run:

```
mvn fmt:format
```     

To format XML files (mainly pom.xml):

```
mvn au.com.acegi:xml-format-maven-plugin:3.1.1:xml-format
```
  
