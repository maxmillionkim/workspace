package part1;

import part1.car;

public class Sonata extends car {
	int wheelNum = 4;
	int speed = 0;
	String carColor = "빨강";

	void move(int i) {
		speed = speed +1;
		System.out.println("지역변수 i는" +i);
	}
	public static void main(String[] args) {
		Sonata gnomCar = new Sonata();
		System.out.println(gnomCar.wheelNum);

	}

}