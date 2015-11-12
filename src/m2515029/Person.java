package m2515029;
public class Person {

	private String name;
	private int age;

	public Person() {
		// TODO Auto-generated constructor stub
	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int value) {
		this.age = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String toString() {
		return "[ Ten: " + name + "; Tuoi: " + age + "]";
	}

}