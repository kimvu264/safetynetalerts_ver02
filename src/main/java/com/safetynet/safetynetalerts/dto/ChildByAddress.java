package com.safetynet.safetynetalerts.dto;

import com.safetynet.safetynetalerts.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class ChildByAddress {
	private List<PersonFullNameWithAge> children;
	private List<Person> adults;
}
