package demo;

class Rectangle {
	double itsWidth;
	double itsHeight;

	public void setWidth(double w) {
		itsWidth = w;
	}

	public void setHeight(double h) {
		itsHeight = h;
	}

	public double area() {
		return itsWidth * itsHeight;
	}
}

class Square extends Rectangle {
	public void setWidth(double w) {
		super.setWidth(w);
		super.setHeight(w);
	}

	public void setHeight(double h) {
		super.setWidth(h);
		super.setHeight(h);
	}
}

public class Client {

	public static void main(String[] args) {
		Rectangle r = new Square();
		r.setWidth(5.0);
		r.setHeight(4.0);
		double a = r.area();
		System.out.println(a);
	}
}
