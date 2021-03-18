package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.team2168.subsystems.Drivetrain;
import org.team2168.thirdcoast.motion.PathController;

public class PathCommand extends Command {

  private static final Drivetrain DRIVE = Drivetrain.getInstance();
  private final PathController pathController;

  public PathCommand(String name, double targetYaw) {
    this(name, targetYaw, true);
  }

  public PathCommand(String name, double targetYaw, boolean isDriftOut) {
    pathController = new PathController(name, targetYaw, isDriftOut);
    requires(DRIVE);
  }

  @Override
  protected void initialize() {
    pathController.start();
  }

  @Override
  protected boolean isFinished() {
    return pathController.isFinished();
  }

  @Override
  protected void interrupted() {
    pathController.interrupt();
  }
}