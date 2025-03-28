import java.io.*;
import java.util.*;

public class Menu {
    private Scanner scanner;
    private GestorArchivos gestorArchivos;
    private List<Map<String, String>> registros;
    private String archivoSeleccionado;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.gestorArchivos = new GestorArchivos();
        this.registros = new ArrayList<>();
        this.archivoSeleccionado = null;
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Seleccionar carpeta");
            System.out.println("2. Listar archivos");
            System.out.println("3. Leer archivo");
            System.out.println("4. Convertir archivo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        seleccionarCarpeta();
                        break;
                    case 2:
                        listarArchivos();
                        break;
                    case 3:
                        
                        break;
                    case 4:
                        
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void seleccionarCarpeta() throws IOException {
        System.out.print("Ingrese la ruta de la carpeta: ");
        String ruta = scanner.nextLine();
        gestorArchivos.seleccionarCarpeta(ruta);
        System.out.println("Carpeta seleccionada: " + gestorArchivos.getCarpetaSeleccionada());
    }

    private void listarArchivos() throws IOException {
        List<String> archivos = gestorArchivos.listarArchivos();
        System.out.println("Archivos disponibles: " + archivos);
    }

}