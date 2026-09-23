package Src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Incidente {

    //Nome em ingles para ficar facil de ver os atributos do csv
    private int id;
    private LocalDate incidentDate; // Tipo data
    private String aircraftModel;
    private String aircraftRegistration; // tamanho fixo: 10 caracteres
    private String aircraftOperator;
    private String aircraftNature;
    private String incidentCategory;
    private String incidentCauses; // lista separada por ';'
    private String incidentLocation;
    private String aircraftDamageType;
    private String date;
    private String time;
    private String arit;
    private String aircraftEngines;
    private int onboardCrew;
    private int onboardPassengers;
    private int onboardTotal;
    private int fatalities; //tipo inteiro
    private String aircraftFirstFlight;
    private String aircraftPhase;
    private String departureAirport;
    private String destinationAirport;
    private int groundCasualties;
    private int collisionCasualties;

    // Construtor vazio pra não dar merda la na frente
    public Incidente() {
        this.id = -1;
        this.incidentDate = null;
        this.aircraftModel = "";
        this.aircraftRegistration = "N/A       ";
        this.aircraftOperator = "";
        this.aircraftNature = "";
        this.incidentCategory = "";
        this.incidentCauses = "";
        this.incidentLocation = "";
        this.aircraftDamageType = "";
        this.date = "";
        this.time = "";
        this.arit = "";
        this.aircraftEngines = "";
        this.onboardCrew = 0;
        this.onboardPassengers = 0;
        this.onboardTotal = 0;
        this.fatalities = 0;
        this.aircraftFirstFlight = "";
        this.aircraftPhase = "";
        this.departureAirport = "";
        this.destinationAirport = "";
        this.groundCasualties = 0;
        this.collisionCasualties = 0;
    }

    public Incidente(int id, LocalDate incidentDate, String aircraftModel, String aircraftRegistration,
            String aircraftOperator, String aircraftNature, String incidentCategory, String incidentCauses,
            String incidentLocation, String aircraftDamageType, String date, String time, String arit,
            String aircraftEngines, int onboardCrew, int onboardPassengers, int onboardTotal,
            int fatalities, String aircraftFirstFlight, String aircraftPhase, String departureAirport,
            String destinationAirport, int groundCasualties, int collisionCasualties) {
        this.id = id;
        this.incidentDate = incidentDate;
        this.aircraftModel = aircraftModel;
        setAircraftRegistration(aircraftRegistration);
        this.aircraftOperator = aircraftOperator;
        this.aircraftNature = aircraftNature;
        this.incidentCategory = incidentCategory;
        this.incidentCauses = incidentCauses;
        this.incidentLocation = incidentLocation;
        this.aircraftDamageType = aircraftDamageType;
        this.date = date;
        this.time = time;
        this.arit = arit;
        this.aircraftEngines = aircraftEngines;
        this.onboardCrew = onboardCrew;
        this.onboardPassengers = onboardPassengers;
        this.onboardTotal = onboardTotal;
        this.fatalities = fatalities;
        this.aircraftFirstFlight = aircraftFirstFlight;
        this.aircraftPhase = aircraftPhase;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.groundCasualties = groundCasualties;
        this.collisionCasualties = collisionCasualties;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    // Para garantir que o tamanho seja sempre 10 no campo que o bd tem tamanho 10
    public void setAircraftRegistration(String aircraftRegistration) {
        if (aircraftRegistration == null || aircraftRegistration.trim().isEmpty()) {
            this.aircraftRegistration = "N/A       ";
        } else if (aircraftRegistration.length() > 10) {
            this.aircraftRegistration = aircraftRegistration.substring(0, 10);
        } else {
            this.aircraftRegistration = String.format("%-10s", aircraftRegistration);
        }
    }

    public String getAircraftOperator() {
        return aircraftOperator;
    }

    public void setAircraftOperator(String aircraftOperator) {
        this.aircraftOperator = aircraftOperator;
    }

    public String getAircraftNature() {
        return aircraftNature;
    }

    public void setAircraftNature(String aircraftNature) {
        this.aircraftNature = aircraftNature;
    }

    public String getIncidentCategory() {
        return incidentCategory;
    }

    public void setIncidentCategory(String incidentCategory) {
        this.incidentCategory = incidentCategory;
    }

    public String getIncidentCauses() {
        return incidentCauses;
    }

    public void setIncidentCauses(String incidentCauses) {
        this.incidentCauses = incidentCauses;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getAircraftDamageType() {
        return aircraftDamageType;
    }

    public void setAircraftDamageType(String aircraftDamageType) {
        this.aircraftDamageType = aircraftDamageType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArit() {
        return arit;
    }

    public void setArit(String arit) {
        this.arit = arit;
    }

    public String getAircraftEngines() {
        return aircraftEngines;
    }

    public void setAircraftEngines(String aircraftEngines) {
        this.aircraftEngines = aircraftEngines;
    }

    public int getOnboardCrew() {
        return onboardCrew;
    }

    public void setOnboardCrew(int onboardCrew) {
        this.onboardCrew = onboardCrew;
    }

    public int getOnboardPassengers() {
        return onboardPassengers;
    }

    public void setOnboardPassengers(int onboardPassengers) {
        this.onboardPassengers = onboardPassengers;
    }

    public int getOnboardTotal() {
        return onboardTotal;
    }

    public void setOnboardTotal(int onboardTotal) {
        this.onboardTotal = onboardTotal;
    }

    public int getFatalities() {
        return fatalities;
    }

    public void setFatalities(int fatalities) {
        this.fatalities = fatalities;
    }

    public String getAircraftFirstFlight() {
        return aircraftFirstFlight;
    }

    public void setAircraftFirstFlight(String aircraftFirstFlight) {
        this.aircraftFirstFlight = aircraftFirstFlight;
    }

    public String getAircraftPhase() {
        return aircraftPhase;
    }

    public void setAircraftPhase(String aircraftPhase) {
        this.aircraftPhase = aircraftPhase;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public int getGroundCasualties() {
        return groundCasualties;
    }

    public void setGroundCasualties(int groundCasualties) {
        this.groundCasualties = groundCasualties;
    }

    public int getCollisionCasualties() {
        return collisionCasualties;
    }

    public void setCollisionCasualties(int collisionCasualties) {
        this.collisionCasualties = collisionCasualties;
    }

    // Por questão de implementação optamos colocar N/A como padrão em campos nulos e o primeiro dia do ano como padrão em datas nulas
    public byte[] arrEmBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        if (this.incidentDate != null) {
            dos.writeLong(this.incidentDate.toEpochDay());
        } else {
            LocalDate primeiroDiaDoAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            dos.writeLong(primeiroDiaDoAno.toEpochDay());
        }

        dos.writeUTF(this.aircraftRegistration != null ? this.aircraftRegistration : "N/A       ");
        escritaEmBytesString(dos, this.aircraftModel);
        escritaEmBytesString(dos, this.aircraftOperator);
        escritaEmBytesString(dos, this.aircraftNature);
        escritaEmBytesString(dos, this.incidentCategory);
        escritaEmBytesString(dos, this.incidentCauses);
        escritaEmBytesString(dos, this.incidentLocation);
        escritaEmBytesString(dos, this.aircraftDamageType);
        escritaEmBytesString(dos, this.date);
        escritaEmBytesString(dos, this.time);
        escritaEmBytesString(dos, this.arit);
        escritaEmBytesString(dos, this.aircraftEngines);
        dos.writeInt(this.onboardCrew);
        dos.writeInt(this.onboardPassengers);
        dos.writeInt(this.onboardTotal);
        dos.writeInt(this.fatalities);
        escritaEmBytesString(dos, this.aircraftFirstFlight);
        escritaEmBytesString(dos, this.aircraftPhase);
        escritaEmBytesString(dos, this.departureAirport);
        escritaEmBytesString(dos, this.destinationAirport);
        dos.writeInt(this.groundCasualties);
        dos.writeInt(this.collisionCasualties);

        dos.flush();
        return baos.toByteArray();
    }

    // Le em bytes e preenche o objeto
    public void bytesEmArr(byte[] ba) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(ba);
                DataInputStream dis = new DataInputStream(bais)) {

            this.id = dis.readInt();
            long epochDay = dis.readLong();
            this.incidentDate = LocalDate.ofEpochDay(epochDay);
            this.aircraftRegistration = dis.readUTF();
            this.aircraftModel = dis.readUTF();
            this.aircraftOperator = dis.readUTF();
            this.aircraftNature = dis.readUTF();
            this.incidentCategory = dis.readUTF();
            this.incidentCauses = dis.readUTF();
            this.incidentLocation = dis.readUTF();
            this.aircraftDamageType = dis.readUTF();
            this.date = dis.readUTF();
            this.time = dis.readUTF();
            this.arit = dis.readUTF();
            this.aircraftEngines = dis.readUTF();
            this.onboardCrew = dis.readInt();
            this.onboardPassengers = dis.readInt();
            this.onboardTotal = dis.readInt();
            this.fatalities = dis.readInt();
            this.aircraftFirstFlight = dis.readUTF();
            this.aircraftPhase = dis.readUTF();
            this.departureAirport = dis.readUTF();
            this.destinationAirport = dis.readUTF();
            this.groundCasualties = dis.readInt();
            this.collisionCasualties = dis.readInt();
        }
    }

    private void escritaEmBytesString(DataOutputStream dos, String valor) throws IOException {
        dos.writeUTF(valor != null && !valor.isBlank() ? valor : "N/A");
    }

    public String imprimir() {
        return "ID: " + id +
                " | Incident Date: " + incidentDate +
                " | Aircraft Registration: " + aircraftRegistration +
                " | Aircraft Model: " + aircraftModel +
                " | Fatalities: " + fatalities +
                " | Incident Causes: " + incidentCauses;
    }

    public String imprimirDetalhado() {
        return "\nID: " + id +
                "\nIncident Date: " + incidentDate +
                "\nAircraft Model: " + aircraftModel +
                "\nAircraft Registration: " + aircraftRegistration +
                "\nAircraft Operator: " + aircraftOperator +
                "\nAircraft Nature: " + aircraftNature +
                "\nIncident Category: " + incidentCategory +
                "\nIncident Causes: " + incidentCauses +
                "\nIncident Location: " + incidentLocation +
                "\nAircraft Damage Type: " + aircraftDamageType +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nArit: " + arit +
                "\nAircraft Engines: " + aircraftEngines +
                "\nOnboard Crew: " + onboardCrew +
                "\nOnboard Passengers: " + onboardPassengers +
                "\nOnboard Total: " + onboardTotal +
                "\nFatalities: " + fatalities +
                "\nAircraft First Flight: " + aircraftFirstFlight +
                "\nAircraft Phase: " + aircraftPhase +
                "\nDeparture Airport: " + departureAirport +
                "\nDestination Airport: " + destinationAirport +
                "\nGround Casualties: " + groundCasualties +
                "\nCollision Casualties: " + collisionCasualties;
    }
}
