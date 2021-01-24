# 2020_SwerveBot
Repo for development of field oriented swerve chassis tests


## Unit Tests

Unit tests are located below the `/src/test` directory. They are used to
perform automated verifications of our codes functionality.

* To run all tests from the command line from within the project folder:
  ```
  $ ./gradlew test
  ```

  Gradle should download all required packages to support running the tests and
  display PASS/FAIL results for each test case in the terminal.

* You can also run tests in a more interactive manner from within vscode using
  the "Java Test Runner" extension.

  * Install/enable the Java Test Runner Extension ([vscjava.vscode-java-test](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test))
  * Click the "Test" flasky looking icon in the left hand pallet
    * From this plugin you can easily navigate through the tests, run tests
      against specific units, inspect test results, etc.

      ![vscode java test plugin](https://raw.githubusercontent.com/Microsoft/vscode-java-test/master/demo/demo.gif)