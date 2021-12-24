package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController
{
	private static final Logger logger = LogManager.getLogger("MedicalRecordController");

	@Autowired
	private MedicalRecordService medicalRecordService;

	public MedicalRecordController(MedicalRecordService medicalRecordService)
	{
		this.medicalRecordService = medicalRecordService;
	}

	@GetMapping("/list")
	public Iterable<MedicalRecord> list()
	{
		return medicalRecordService.list();
	}

	// Ajouter un dossier médical
	@PostMapping("/add")
	public ResponseEntity<Object> addDossier(@RequestBody MedicalRecord medicalRecord)
	{
		MedicalRecord mediRecord = medicalRecordService.getMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());
		if(mediRecord == null)
		{
			logger.info("Dossier is added successfully");
			return new ResponseEntity<>(medicalRecordService.addMedicalRecord(medicalRecord), HttpStatus.OK);
		}
		logger.error("Error: Dossier exist already");
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	// Mettre à jour un dossier existant
	@PutMapping("/updateDossier/")
	public ResponseEntity<Object> updateDossier(@RequestBody MedicalRecord medicalRecord)
	{
		try
		{
			MedicalRecord medRecord = medicalRecordService.getMedicalRecordById(medicalRecord.getId());
			medicalRecord.setId(medRecord.getId());
			medicalRecord.setFirstName(medRecord.getFirstName());
			medicalRecord.setLastName(medRecord.getLastName());
			logger.info("Update dossier successfully");
			return new ResponseEntity<>(medicalRecordService.updateMedicalRecord(medicalRecord), HttpStatus.OK);
		}
		catch(EntityNotFoundException e)
		{
			logger.error("Error: Id is not valid");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// Delete un dossier par nom et prénom
	//TODO: Fix bug here
	@DeleteMapping("/deleteDossier/{firstName}/{lastName}")
	public void deleteDossier(@PathVariable String firstName, @PathVariable String lastName)
	{
		MedicalRecord medRecord = medicalRecordService.getMedicalRecord(firstName, lastName);
		if(medRecord == null)
		{
			logger.error("Error: firstName or lastName is not valid");
		}
		medicalRecordService.deleteMedicalRecord(firstName, lastName);
	}
}
