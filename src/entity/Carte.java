package entity;

import entity.enums.StatutEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public sealed class Carte permits CarteDebit, CarteCredit , CartePrepayee  {
    private UUID id;
    private String numiro;
    private LocalDateTime dateExpiration;
    private StatutEnum status;
    private UUID idClient;
}
