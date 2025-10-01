package entity;

import entity.enums.OperationTypeEnum;

import java.time.LocalDateTime;

public record OperationCarte(String id, LocalDateTime date, double montant, OperationTypeEnum type,String lieu,String idCarte) { }
