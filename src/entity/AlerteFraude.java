package entity;

import entity.enums.AlertEnum;

public record AlerteFraude(String id, String description, AlertEnum niveau, String idCarte) { }
