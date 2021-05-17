package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import model.Event.EventType;
import model.Patient.ColorCode;

public class Simulator {
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	// modello del mondo
	private List<Patient> patients; 
	private int freeStudios; // numero studi liberi 
	
	// parametri di input
	private int totStudios = 3; //ns
	
	private int numPatients = 120; ///np
	private Duration T_ARRIVAL =Duration.ofMinutes(5);
	
	private Duration DURATION_TRIAGE= Duration.ofMinutes(5);
	private Duration DURATION_WHITE= Duration.ofMinutes(10);
	private Duration DURATION_YELLOW= Duration.ofMinutes(15);
	private Duration DURATION_RED= Duration.ofMinutes(30);
	
	private Duration TIMEOUT_WHITE= Duration.ofMinutes(60);
	private Duration TIMEOUT_YELLOW= Duration.ofMinutes(30);
	private Duration TIMEOUT_RED= Duration.ofMinutes(30);
	
	private LocalTime startTime = LocalTime.of(8,  00);
	private LocalTime endTime = LocalTime.of(20,  00);
	
	//parametri di output
	private int patientsTreated; 
	private int patientsAbandoned; 
	private int patientDead; 
	
	
	//inizializza il simulatore e crea gli eventi iniziali
	public void init() {
		this.queue = new PriorityQueue<>(); 
		
		this.patients = new ArrayList<>(); 
		this.freeStudios = this.totStudios;
		
		//inizializza parametri di output
		
		this.patientsAbandoned = 0;
		this.patientsTreated = 0; 
		this.patientDead =0 ; 
		
		
		//inietta eventi di input (ARRIVAL)
		
		LocalTime ora = this.startTime; 
		int inseriti=0; 
//		Patient.ColorCode colore = ColorCode.WHITE;
		
		while(ora.isBefore(this.endTime) && inseriti <this.numPatients) {
			Patient p = new Patient(ora, ColorCode.NEW); 
			
			Event e = new Event(ora, EventType.ARRIVAL, p); 
			
			this.queue.add(e); 
			this.patients.add(p);
			
			inseriti++;
			ora = ora.plus(T_ARRIVAL);
			

		}
		
		
	}
	
	private Patient.ColorCode ultimoColore = ColorCode.WHITE;
	private  Patient.ColorCode prossimoColore(){
		if(ultimoColore.equals(ColorCode.WHITE))
			ultimoColore = ColorCode.YELLOW;
		else if (ultimoColore.equals(ColorCode.YELLOW))
			ultimoColore = ColorCode.RED;
		else
			ultimoColore= ColorCode.WHITE;
	return ultimoColore;
	}	
	
	//simulazione vera e propria
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e); 
			processEvent(e); 
			
		}
		
	}

	private void processEvent(Event e) {
		
		Patient p = e.getPatient(); 
		LocalTime ora = e.getTime(); 
		Patient.ColorCode colore = p.getColor(); 
				
		switch(e.getType()) {
		case ARRIVAL: 
			this.queue.add(new Event(ora.plus(DURATION_TRIAGE), EventType.TRIAGE, p));
			break;		
		case TRIAGE: 
			p.setColor(prossimoColore());
			if(p.getColor().equals(Patient.ColorCode.WHITE))
				this.queue.add(new Event(ora.plus(TIMEOUT_WHITE),EventType.TIMEOUT,p));
			if(p.getColor().equals(Patient.ColorCode.YELLOW))
				this.queue.add(new Event(ora.plus(TIMEOUT_YELLOW),EventType.TIMEOUT,p));
			if(p.getColor().equals(Patient.ColorCode.RED))
				this.queue.add(new Event(ora.plus(TIMEOUT_RED),EventType.TIMEOUT,p));
			break; 	
		case FREE_STUDIO:
			break; 
		case TIMEOUT:
			break;
		case TREATED:
			break; 
			
		
		}
		
	}
	public void setTotStudios(int totStudios) {
		this.totStudios = totStudios;
	}

	public void setNumPatients(int numPatients) {
		this.numPatients = numPatients;
	}

	public void setT_ARRIVAL(Duration t_ARRIVAL) {
		T_ARRIVAL = t_ARRIVAL;
	}

	public void setDURATION_TRIAGE(Duration dURATION_TRIAGE) {
		DURATION_TRIAGE = dURATION_TRIAGE;
	}

	public void setDURATION_WHITE(Duration dURATION_WHITE) {
		DURATION_WHITE = dURATION_WHITE;
	}

	public void setDURATION_YELLOW(Duration dURATION_YELLOW) {
		DURATION_YELLOW = dURATION_YELLOW;
	}

	public void setDURATION_RED(Duration dURATION_RED) {
		DURATION_RED = dURATION_RED;
	}

	public void setTIMEOUT_WHITE(Duration tIMEOUT_WHITE) {
		TIMEOUT_WHITE = tIMEOUT_WHITE;
	}

	public void setTIMEOUT_YELLOW(Duration tIMEOUT_YELLOW) {
		TIMEOUT_YELLOW = tIMEOUT_YELLOW;
	}

	public void setTIMEOUT_RED(Duration tIMEOUT_RED) {
		TIMEOUT_RED = tIMEOUT_RED;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getPatientsTreated() {
		return patientsTreated;
	}

	public int getPatientsAbandoned() {
		return patientsAbandoned;
	}

	public int getPatientDead() {
		return patientDead;
	}
	
	
}
