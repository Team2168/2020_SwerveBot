# Setup / Run:

  * Double click `pynetworktables2js.exe` from within this directory.
  * Double click `index.html` to open it in your web browser.

  By default the dashboard will connect to a NetworkTables data source running
  on your local machine, which is useful if you're running robot code in a
  simulation. If you're running with the real robot, you'll want to run
  pynetworktables2js with one of the following options depending on how the
  robot is configured:

    * `pynetworktables2js.exe --robot=10.21.68.2` - this will connect to a specific robot IP address
    * `pynetworktables2js.exe --dashboard` - this will attempt to connect an IP address
       provided by the driverstation application
    * `pynetworktables2js.exe --team=2168` - this will attempt to connect to the robot
       using its `roboRIO-218-FRC.local` mDNS address

# Documentation

  * For examples of how to use the frc web components: https://frc-web-components.github.io/
  * For examples of how to interact with the network tables data in javascript:
    https://robotpy.readthedocs.io/projects/pynetworktables2js/en/stable/index.html

# Background Info

This dashboard should run in any modern web browser.
It is based on [frc-web-components](https://github.com/frc-web-components/frc-web-components)
and uses [pynetworktables2js](https://github.com/robotpy/pynetworktables2js) to parse network
table data.

* The latest version of frc-web-components `js` file was downloaded from
  [releases page](https://github.com/frc-web-components/frc-web-components/releases) and placed in
  this directory. It will probably need to be updated in the future.

* The latest version of pynetworktables2js was downloaded from
  https://github.com/robotpy/pynetworktables2js/releases and placed in this directory. It will
  probably need to be updated in the future.