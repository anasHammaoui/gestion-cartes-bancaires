package service.impl;

import dao.impl.AlerteDao;
import dao.impl.CarteDao;
import dao.impl.OperationDao;
import entity.AlerteFraude;
import entity.Carte;
import entity.CarteCredit;
import entity.CarteDebit;
import entity.OperationCarte;
import entity.enums.AlertEnum;
import exception.DaoException;
import exception.ServiceException;
import service.FraudeServiceInterface;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FraudeService implements FraudeServiceInterface {
    private final AlerteDao alerteDao;
    private final OperationDao operationDao;
    private final CarteDao carteDao;

    // Seuils par défaut pour la détection de fraude
    private static final double SEUIL_MONTANT_ELEVE = 1000.0;
    private static final int MINUTES_OPERATIONS_RAPPROCHEES = 10;

    public FraudeService(AlerteDao alerteDao, OperationDao operationDao, CarteDao carteDao) {
        this.alerteDao = alerteDao;
        this.operationDao = operationDao;
        this.carteDao = carteDao;
    }

    @Override
    public void createAlerte(AlerteFraude alerte) throws ServiceException {
        if (alerte == null) {
            throw new IllegalArgumentException("Alerte is required");
        }
        try {
            alerteDao.createAlerte(alerte);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<AlerteFraude> readAlerte(String id) throws ServiceException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Alerte ID is required");
        }
        try {
            return alerteDao.readAlerte(id);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<AlerteFraude> updateAlerte(AlerteFraude alerte) throws ServiceException {
        if (alerte == null) {
            throw new IllegalArgumentException("Alerte is required");
        }
        try {
            return alerteDao.updateAlerte(alerte);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteAlerte(String id) throws ServiceException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Alerte ID is required");
        }
        try {
            return alerteDao.deleteAlerte(id);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<AlerteFraude> rechercherAlertesParCarte(String cardId) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        try {
            return alerteDao.findAlertesByCard(cardId);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<AlerteFraude> rechercherAlertesParClient(String clientId) throws ServiceException {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        try {
            return alerteDao.findAlertesByClient(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void detecterAnomalies(String cardId) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }

        try {
            // 1. Vérifier le dépassement de plafond
            verifierDepassementPlafond(cardId);

            // 2. Vérifier les opérations rapprochées
            verifierOperationsRapprochees(cardId, MINUTES_OPERATIONS_RAPPROCHEES);

            // 3. Vérifier les montants élevés dans les dernières 24h
            LocalDateTime derniere24h = LocalDateTime.now().minusHours(24);
            List<OperationCarte> operationsRecentes = operationDao.findOperationsByCardAndDateRange(
                cardId, 
                derniere24h, 
                LocalDateTime.now()
            );

            for (OperationCarte operation : operationsRecentes) {
                verifierMontantEleve(operation, SEUIL_MONTANT_ELEVE);
            }

            System.out.println("Détection d'anomalies terminée pour la carte: " + cardId);

        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void verifierMontantEleve(OperationCarte operation, double seuilMontant) throws ServiceException {
        if (operation == null) {
            throw new IllegalArgumentException("Operation is required");
        }

        if (operation.montant() > seuilMontant) {
            String description = String.format(
                "Montant élevé détecté: %.2f€ (seuil: %.2f€) - %s à %s",
                operation.montant(),
                seuilMontant,
                operation.type(),
                operation.lieu()
            );

            genererAlerteFraude(operation.idCarte(), description, "AVERTISSEMENT");
            System.out.println("⚠️ ALERTE: " + description);
        }
    }

    @Override
    public void verifierOperationsRapprochees(String cardId, int minutesInterval) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }

        try {
            LocalDateTime maintenant = LocalDateTime.now();
            LocalDateTime debutIntervalle = maintenant.minusMinutes(minutesInterval);

            List<OperationCarte> operationsRecentes = operationDao.findOperationsByCardAndDateRange(
                cardId,
                debutIntervalle,
                maintenant
            );

            if (operationsRecentes.size() >= 2) {
                // Vérifier si les opérations sont dans des lieux différents
                boolean lieuxDifferents = false;
                for (int i = 0; i < operationsRecentes.size() - 1; i++) {
                    String lieu1 = operationsRecentes.get(i).lieu();
                    String lieu2 = operationsRecentes.get(i + 1).lieu();
                    
                    if (!lieu1.equalsIgnoreCase(lieu2)) {
                        lieuxDifferents = true;
                        
                        long minutesEntreOperations = ChronoUnit.MINUTES.between(
                            operationsRecentes.get(i + 1).date(),
                            operationsRecentes.get(i).date()
                        );

                        String description = String.format(
                            "Opérations rapprochées détectées: %d opérations en %d minutes dans des lieux différents (%s et %s)",
                            operationsRecentes.size(),
                            minutesEntreOperations,
                            lieu1,
                            lieu2
                        );

                        genererAlerteFraude(cardId, description, "CRITIQUE");
                        System.out.println("🚨 ALERTE CRITIQUE: " + description);
                        break;
                    }
                }

                // Si plusieurs opérations dans le même lieu en peu de temps
                if (!lieuxDifferents && operationsRecentes.size() >= 3) {
                    String description = String.format(
                        "Activité inhabituelle: %d opérations en %d minutes",
                        operationsRecentes.size(),
                        minutesInterval
                    );
                    genererAlerteFraude(cardId, description, "AVERTISSEMENT");
                    System.out.println("⚠️ ALERTE: " + description);
                }
            }

        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void verifierDepassementPlafond(String cardId) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }

        try {
            Optional<Carte> carteOpt = carteDao.readCard(cardId);
            if (carteOpt.isEmpty()) {
                throw new ServiceException("Card not found: " + cardId);
            }

            Carte carte = carteOpt.get();
            LocalDateTime maintenant = LocalDateTime.now();

            // Vérifier selon le type de carte
            switch (carte.getTypeCarte()) {
                case CARTEDEBIT:
                    CarteDebit carteDebit = (CarteDebit) carte;
                    // Vérifier le plafond journalier
                    LocalDateTime debutJour = maintenant.toLocalDate().atStartOfDay();
                    List<OperationCarte> operationsJour = operationDao.findOperationsByCardAndDateRange(
                        cardId,
                        debutJour,
                        maintenant
                    );

                    double totalJour = operationsJour.stream()
                        .mapToDouble(OperationCarte::montant)
                        .sum();

                    if (totalJour > carteDebit.getPlafond_journalier()) {
                        String description = String.format(
                            "Dépassement du plafond journalier: %.2f€ / %.2f€",
                            totalJour,
                            carteDebit.getPlafond_journalier()
                        );
                        genererAlerteFraude(cardId, description, "CRITIQUE");
                        System.out.println("🚨 ALERTE CRITIQUE: " + description);
                    } else if (totalJour > carteDebit.getPlafond_journalier() * 0.8) {
                        // Alerte préventive à 80%
                        String description = String.format(
                            "Approche du plafond journalier: %.2f€ / %.2f€ (%.0f%%)",
                            totalJour,
                            carteDebit.getPlafond_journalier(),
                            (totalJour / carteDebit.getPlafond_journalier()) * 100
                        );
                        genererAlerteFraude(cardId, description, "INFO");
                        System.out.println("ℹ️ INFO: " + description);
                    }
                    break;

                case CARTECREDIT:
                    CarteCredit carteCredit = (CarteCredit) carte;
                    // Vérifier le plafond mensuel
                    LocalDateTime debutMois = maintenant.withDayOfMonth(1).toLocalDate().atStartOfDay();
                    List<OperationCarte> operationsMois = operationDao.findOperationsByCardAndDateRange(
                        cardId,
                        debutMois,
                        maintenant
                    );

                    double totalMois = operationsMois.stream()
                        .mapToDouble(OperationCarte::montant)
                        .sum();

                    if (totalMois > carteCredit.getPlafond_mensuel()) {
                        String description = String.format(
                            "Dépassement du plafond mensuel: %.2f€ / %.2f€",
                            totalMois,
                            carteCredit.getPlafond_mensuel()
                        );
                        genererAlerteFraude(cardId, description, "CRITIQUE");
                        System.out.println("🚨 ALERTE CRITIQUE: " + description);
                    } else if (totalMois > carteCredit.getPlafond_mensuel() * 0.8) {
                        // Alerte préventive à 80%
                        String description = String.format(
                            "Approche du plafond mensuel: %.2f€ / %.2f€ (%.0f%%)",
                            totalMois,
                            carteCredit.getPlafond_mensuel(),
                            (totalMois / carteCredit.getPlafond_mensuel()) * 100
                        );
                        genererAlerteFraude(cardId, description, "AVERTISSEMENT");
                        System.out.println("⚠️ ALERTE: " + description);
                    }
                    break;

                case CARTEPREPAYEE:
                    // Les cartes prépayées n'ont pas de plafond, mais un solde
                    // Cette vérification pourrait être faite ailleurs
                    break;
            }

        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void genererAlerteFraude(String cardId, String description, String niveau) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (niveau == null || niveau.isBlank()) {
            throw new IllegalArgumentException("Niveau is required");
        }

        try {
            AlertEnum niveauEnum = AlertEnum.valueOf(niveau.toUpperCase());
            
            AlerteFraude alerte = new AlerteFraude(
                UUID.randomUUID().toString(),
                description,
                niveauEnum,
                cardId
            );

            alerteDao.createAlerte(alerte);
            System.out.println("✅ Alerte de fraude générée: " + niveau + " - " + description);

        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid alert level: " + niveau + ". Must be INFO, AVERTISSEMENT, or CRITIQUE");
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }
}