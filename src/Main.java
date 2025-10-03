import dao.impl.AlerteDao;
import dao.impl.CarteDao;
import dao.impl.ClientDao;
import dao.impl.OperationDao;
import service.impl.CarteService;
import service.impl.ClientService;
import service.impl.FraudeService;
import service.impl.OperationService;
import ui.menu.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        ClientDao clientDao = new ClientDao();
        CarteDao carteDao = new CarteDao();
        OperationDao operationDao = new OperationDao();
        AlerteDao alerteDao = new AlerteDao();

        ClientService clientService = new ClientService(clientDao);
        CarteService carteService = new CarteService(carteDao);
        OperationService operationService = new OperationService(operationDao);
        FraudeService fraudeService = new FraudeService(alerteDao, operationDao, carteDao);

        MenuPrincipal menuPrincipal = new MenuPrincipal(
            clientService, 
            carteService, 
            operationService, 
            fraudeService
        );

        menuPrincipal.demarrer();
    }

}
