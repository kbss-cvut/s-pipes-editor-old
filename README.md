# SPEd
Semantic Pipeline Editor
#### Build
Due to modular nature of the JDK9, the `java.xml.bind`  module has to be added to classpath in order to build  the project. It can be achieved with the compiler argument `--add-modules java.xml.bind`.
##### IntelliJ IDEA
Go to Settings -> Build, Execution, Deployment -> Compiler and add `--add-modules java.xml.bind` to Shared build process VM options.