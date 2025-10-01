package entity;

import entity.enums.StatutEnum;

import java.time.LocalDateTime;

public sealed class Carte permits CarteDebit, CarteCredit , CartePrepayee  {
    private String id;
    private String numiro;
    private LocalDateTime dateExpiration;
    private StatutEnum status;
    private String idClient;
}
