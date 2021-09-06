## Gradle plugin demo where the functional tests have to access to the plugin src code or then testing project src code.

### Functionality description:
This plugin accepts a configuration of the form and echoes the contents of the file in the Standard 
output.
```groovy
echofiletask{
    filePath = "echo.me"
}
```

### Build plugin:
./gradlew build

### Structure details:

#### Folder `plugin`
* The `plugin` folder contains the implementation logic of the plugin.
* The  `main` folder contains hte main implementation logic
* The `test` folder contains Unit tests for the various modules of the plugin
* The `functionalTest` folder contains tests that invoke a GradleRunner and test the total 
result of the plugin application.

###### Notes

1. It is important to note that in the `test` folder we cannot instantiate the Tasks that we create 
and therefore we can only test logic that is connected to the project structure, i.e., whatever 
one can define withing the files `build.gradle` and `settings.gradle`. If the plugin task interacts 
with some project files (of the testing project), we can test such interactions with the 
*functional tests*.

2.Given the current setup, it is not possible for the functional tests to have access to the
classpath of the `main` or the `test` folder. That is to say, the *functional tests* observe only
the results of the build that is run by the GradeRunner

#### Folder `testingProject`
This folder contains a separate and independent project that is copied during the functional tests 
to a temporary folder and `GradleRunner` runs on this temporary folder. 
It is necessary to inject a `settings.gradle` file and it is possible to inject any other file
that is necessary for the functional tests.

###### Notes

1. No other module or code in any of the `plugin` folders have access to the classpath of the 
`testingProject`. Dependency to the `testingProject`'s classpath seems very difficult to accomplish
without having synchronization problems.
2. The `testingProject` is connected to the plugin through the configuration of its `settings.file`
and can be run normally as an independent project, `gradle build` is run inside the `testingProject`


#### General Notes:
This implementation is based on the default Gradle plugin setup.

