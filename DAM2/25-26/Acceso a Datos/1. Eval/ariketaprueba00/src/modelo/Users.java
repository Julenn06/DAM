package modelo;

public class Users {

	private String name = "";
	private int age = 0;
	private double salary = 0;

	public String getName() {
		return name;
	}

	public double getSalary() {
		return salary;
	}

	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Users [name=" + name + ", age=" + age + ", salary=" + salary + "]";
	}
}
