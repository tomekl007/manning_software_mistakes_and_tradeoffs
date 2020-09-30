package com.tomekl007.CH02.services.separate.person;

public class PersonService {
	public PersonDto getById(String id) {
		return new PersonDto("John", "Doe", id);
	}
}
