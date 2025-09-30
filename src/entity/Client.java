package entity;

import java.util.UUID;

public record Client(UUID id, String nom, String phone) {}