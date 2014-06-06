package edu.tn.xds.metadata.editor.shared.model.testEditor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Person implements Serializable {
	private String name;
	private String lastName;
	private String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", lastName=" + lastName + ", age=" + age + "]";
	}

}
