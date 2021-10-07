# 2020_SwerveBot

Repo for development of code for a field oriented swerve chassis.
This repo name is a bit of a misnomer - it's our [2020 Robot code](https://github.com/Team2168/2020_Main_Robot) + the code for a swerve chassis.

![image](https://user-images.githubusercontent.com/1295877/129284494-767e60de-8bb3-4bbc-adeb-453a15b0275b.png)

Our swerve code was largely lifted [from 2767](https://github.com/strykeforce), don't consider it a reference implementation. We are still learning :)

## Steps for zeroing cancoders

Currently loading cancoder offset zeros from a hard coded array inside `Drivetrain.java`
TODO: clean this up so it's more reliable/robust

1. PHysically orient the swerve modules to their zero positions (_bevel to battery_)
2. In Phoenix Tuner, set the zero offset for the cancoder to 0 and save
3. Query the Absolute Position (in degrees) by performing a self test on the cancoder
4. Copy the Absolute Position angle (not negated) into the `ABSOLUTE_ENCODER_OFFSET` array in `Drivetrain.java`
5. Deploy the code to the robot.
   _Note: This will set the zero offset on the cancoder, but will not yet cause the corrected zero position
   to be reflected in the position reported to the TalonFX._
6. Restart the robot.
7. Enable the robot code and verify the module zero position is correct.

## Subsystems

These are mostly the same as the 2020 robot, see the [2020 repo](https://github.com/Team2168/2020_Main_Robot#robot-subsystems) for a breakdown of subsystem capabilities until we get better documentation added here.

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
