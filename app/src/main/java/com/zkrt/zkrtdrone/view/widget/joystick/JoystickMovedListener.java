/**
 * Copied from https://code.google.com/p/mobile-anarchy-widgets/
 */
package com.zkrt.zkrtdrone.view.widget.joystick;

public interface JoystickMovedListener {
	public void OnMoved(double pan, double tilt);

	public void OnReleased();  //anxia

	public void OnReturnedToCenter();
}
