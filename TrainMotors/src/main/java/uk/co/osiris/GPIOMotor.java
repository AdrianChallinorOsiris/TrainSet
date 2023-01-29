package uk.co.osiris;

import org.springframework.scheduling.annotation.Async;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.MotorMsg;

@Data
@RequiredArgsConstructor
@Slf4j
public class GPIOMotor {
	@NonNull private String id;
	@NonNull private GpioPinDigitalOutput enable;
	@NonNull private GpioPinDigitalOutput direction;
	@NonNull private String section;
	private int currentSpeed = 0;
	private int requestedSpeed = 0;
	private int acceleration = 10;
	private boolean forward = true;
	private boolean moving = false;
	private final static int CYCLE = 100;

	public void immediateStop() {
		requestedSpeed = 0;
		currentSpeed = 0;
		moving = false;
		updateGPIO();
	}

	public void move(int speed, int accelation, boolean forward) {
		this.forward = forward;
		this.requestedSpeed = speed;
		this.acceleration = accelation;
		moving = true;
		checkRequestedSpeed();
		updateGPIO();
	}

	private void checkRequestedSpeed() {
		if (requestedSpeed > 100)
			requestedSpeed = 100;
		if (requestedSpeed < 0)
			requestedSpeed = 0;
		moving = (requestedSpeed > 0);
	}

	@Async
	private void updateGPIO() {
		try {
			do {
				checkRequestedSpeed();
				if (!moving) {
					enable.setState(false);
					direction.setState(false);
				} else {
					direction.setState(forward);
					int pulse = currentSpeed / CYCLE;
					int delay = CYCLE - pulse;
					enable.setState(true);
					Thread.sleep(pulse);
					if (delay > 0) {
						enable.setState(false);
						Thread.sleep(delay);
					}

					if (requestedSpeed > currentSpeed)
						currentSpeed += acceleration;
				}

			} while (moving);
		} catch (InterruptedException e) {
			log.error("Timer was interrupted. Motor {} - {}", id, e.getMessage());
			immediateStop();
		}
	}

	public MotorMsg getStatus() {
		return new MotorMsg(id,section,currentSpeed,requestedSpeed,forward);
	}

}
