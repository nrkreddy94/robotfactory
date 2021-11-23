package de.tech26.robotfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * At Robot Factory Inc. we sell configurable Robots, You just let us know
 * configurations and we will manufacture it for you.
 * 
 * When ordering a robot, a customer must configure the following parts: - Face
 * (Humanoid, LCD or Steampunk) - Material (Bioplastic or Metallic) - Arms
 * (Hands or Grippers) - Mobility (Wheels, Legs or Tracks)
 * 
 * @author Jagadheeswar Reddy
 *
 */
@SpringBootApplication
public class RobotFactoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobotFactoryApplication.class, args);
	}

}
