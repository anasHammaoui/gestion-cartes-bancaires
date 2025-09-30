package entity;

import entity.enums.AlertEnum;

import java.util.UUID;

public record AlerteFraude(UUID id, String description, AlertEnum niveau, UUID idCarte) { }
