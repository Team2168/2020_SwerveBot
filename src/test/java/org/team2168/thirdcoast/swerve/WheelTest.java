package org.team2168.thirdcoast.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode;

import edu.wpi.first.wpilibj.AnalogInput;

import static com.ctre.phoenix.motorcontrol.ControlMode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;
import static org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode.*;

@ExtendWith(MockitoExtension.class)
class WheelTest {

  @Mock private TalonFX driveTalon;
  @Mock private TalonFX azimuthTalon;
  @Mock private AnalogInput externalEncoder;

  private static final double AZIMUTH_GEAR_RATIO = (60.0/10.0) * (45.0/15.0); // defined as module input/motor output; placeholder
  private static final int INTERNAL_ENCODER_TICKS = 2048;
  private static final double TICKS_PER_DEGREE_AZIMUTH = ((1.0/360.0) * AZIMUTH_GEAR_RATIO * INTERNAL_ENCODER_TICKS);
  private static final double INTERNAL_ENCODER_TICKS_PER_REV = 360.0 * TICKS_PER_DEGREE_AZIMUTH;

  // static Stream<Arguments> setDriveModeTestProvider() {
  //   return Stream.of(
  //       arguments(OPEN_LOOP, PercentOutput, 27.0),
  //       arguments(TELEOP, PercentOutput, 27.0),
  //       arguments(CLOSED_LOOP, Velocity, 270.0),
  //       arguments(TRAJECTORY, Velocity, 270.0),
  //       arguments(AZIMUTH, Velocity, 270.0));
  // }

  @ParameterizedTest
  @CsvFileSource(resources = "/wheel_set_cases.csv", numLinesToSkip = 1)
  void set(double startPosition, double setpoint, double endPosition, boolean isReversed) {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    int encoderStartingPosition = (int) Math.round(startPosition * INTERNAL_ENCODER_TICKS_PER_REV);
    when(azimuthTalon.getSelectedSensorPosition(0)).thenReturn((double) encoderStartingPosition);
    wheel.setDriveMode(DriveMode.TELEOP);
    wheel.set(setpoint, 1.0);

    ArgumentCaptor<Double> argument = ArgumentCaptor.forClass(Double.class);
    verify(azimuthTalon).set((ControlMode) any(), argument.capture());
    assertThat(argument.getValue()).isCloseTo(endPosition * INTERNAL_ENCODER_TICKS_PER_REV, byLessThan(1e-11));
    verify(driveTalon).set(PercentOutput, isReversed ? -1d : 1d);
  }

  @Test
  void zeroDriveLeavesAzimuthAlone() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    wheel.setDriveMode(DriveMode.TELEOP);
    wheel.set(0d, 0d);
    verify(driveTalon).set(PercentOutput, 0d);
    verify(azimuthTalon, never()).set((ControlMode) any(), anyDouble());
  }

  @Test
  void zeroDriveMovesAzimuthInTestMode() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    wheel.setDriveMode(DriveMode.MANUAL_AZIMUTH_TEST);
    wheel.set(0d, 0d);
    verify(driveTalon).set(PercentOutput, 0d);
    verify(azimuthTalon, times(1)).set((ControlMode) any(), anyDouble());
  }

  @Test
  void setAzimuthPosition() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    wheel.setAzimuthPosition(2767);
    verify(azimuthTalon).set(MotionMagic, (int) (2767/AZIMUTH_GEAR_RATIO));
  }

  @Test
  void disableAzimuth() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    wheel.disableAzimuth();
    verify(azimuthTalon).neutralOutput();
  }

  // @ParameterizedTest
  // @MethodSource("setDriveModeTestProvider")
  // void setDriveMode(SwerveDrive.DriveMode driveMode, ControlMode controlMode, double setpoint) {
  //   Wheel wheel = new Wheel(azimuthTalon, driveTalon, 10.0);
  //   wheel.setDriveMode(driveMode);
  //   wheel.set(0.0, 27.0);
  //   verify(driveTalon).set(controlMode, setpoint);
  // }

  @Test
  void stopOpenLoop() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    when(azimuthTalon.getSelectedSensorPosition(0)).thenReturn(2767.0);
    wheel.setDriveMode(OPEN_LOOP);
    wheel.stop();
    wheel.setDriveMode(CLOSED_LOOP);
    wheel.stop();

    verify(azimuthTalon, times(2)).set(MotionMagic, 2767);
    verify(driveTalon).set(PercentOutput, 0.0);
    verify(driveTalon).set(Velocity, 0.0);
  }

  @ParameterizedTest
  @CsvSource({"0, 2767, -2767"})
  void setAzimuthZero(
      int encoderPosition, int zero, int setpoint, @Mock TalonFXSensorCollection sensorCollection) {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    when(azimuthTalon.getSelectedSensorPosition(1)).thenReturn((double) encoderPosition);
    when(azimuthTalon.getSensorCollection()).thenReturn(sensorCollection);

    wheel.setAzimuthZero(zero);
    verify(azimuthTalon).setSelectedSensorPosition(setpoint, 0, 10);
  }

  /**
   * The AnalogInput channel used to wire in the external encoder should return a
   * 12 bit number (value 0 - 4095).
   */

  // @ParameterizedTest
  // @CsvSource({"0, 0, false", "2048, 2048, false", "4095, 4095, false",
  //             "0, 0, true", "2048, -2048, true", "4095, -4095, true",})
  // void getAzimuthAbsolutePosition( Integer encoderPosition, int absolutePosition,
  //     boolean inverted, @Mock TalonFXSensorCollection sensorCollection) {

  //   Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, inverted);
  //   // when(azimuthTalon.getSelectedSensorPosition()).thenReturn((double) encoderPosition);
  //   // when(externalEncoder.getValue().thenReturn(encoderPosition));
  //   when(azimuthTalon.getSensorCollection()).thenReturn(sensorCollection);
  //   assertThat(wheel.getAzimuthAbsolutePosition()).isEqualTo(absolutePosition);
  // }

  @Test
  void getAzimuthTalon() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    assertThat(wheel.getAzimuthTalon()).isSameAs(azimuthTalon);
  }

  @Test
  void getDriveTalon() {
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
    assertThat(wheel.getDriveTalon()).isSameAs(driveTalon);
  }

  // @Test
  // void getDriveSetpointMax() {
  //   Wheel wheel = new Wheel(azimuthTalon, driveTalon, externalEncoder, false);
  //   assertThat(wheel.getDriveSetpointMax()).isEqualTo(2767.0);
  // }
}