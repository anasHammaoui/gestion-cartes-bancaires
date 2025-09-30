package entity;

import entity.enums.OperationTypeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record OperationCarte(UUID id, LocalDateTime date, double montant, OperationTypeEnum type,String lieu,UUID idCarte) { }
