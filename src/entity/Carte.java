package entity;

import entity.enums.CarteEnum;
import entity.enums.StatutEnum;

import java.time.LocalDateTime;

public sealed class Carte permits CarteDebit, CarteCredit , CartePrepayee  {
    private String id;
    private String numiro;
    private LocalDateTime dateExpiration;
    private StatutEnum status;
    private String idClient;
    private CarteEnum typeCarte;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumiro() {
        return numiro;
    }

    public void setNumiro(String numiro) {
        this.numiro = numiro;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public StatutEnum getStatus() {
        return status;
    }

    public void setStatus(StatutEnum status) {
        this.status = status;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public CarteEnum getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(CarteEnum typeCarte) {
        this.typeCarte = typeCarte;
    }
}
