package ch.sofa.lodo.data.services.geo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DistanceCalculatorTest {

	private final double lat1;
	private final double lon1;
	private final double lat2;
	private final double lon2;
	private final double distance;

	public DistanceCalculatorTest(double lat1, double lon1, double lat2, double lon2, double distance) {
		super();
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
		this.distance = distance;
	}

	@Parameterized.Parameters(name = "{index}: Test with X={0}, Y={1}, Z= {2}, X= {3}, distance: {4}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				// TODO: take tests from here: http://devserver:8090/display/LoDo/Tests
				// store places as variables
				{45.79415d, 15.84557d, 45.799015d, 15.872945d, 2.19d},
				{45.79415d, 15.84557d, 45.797266d, 15.857949d, 1.02d},
				{45.79415d, 15.84557d, 45.796011d, 15.999374d, 11.93d},
				{45.79415d, 15.84557d, 45.770565d, 15.886305d, 4.11d},
				{45.79415d, 15.84557d, 45.797002d, 15.858006d, 1.01d},
				{45.79415d, 15.84557d, 45.699015d, 15.902662d, 11.47d},
		});
	}

	// result deviation ( delta ) < 50 meters
	@Test
	public void distanceCalculationTest() {

		DistanceCalculator calcImpl = new DistanceCalculator();

		System.out.println(" calc dist ? " + calcImpl.distanceMath(lat1, lon1, lat2, lon2) / 1000);
		assertEquals(distance, calcImpl.distanceMath(lat1, lon1, lat2, lon2) / 1000, 0.05d);
	}
}
