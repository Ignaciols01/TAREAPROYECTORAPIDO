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
                        leerArchivo();
                        break;
                    case 4:
                        convertirArchivo();
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

    private void leerArchivo() throws Exception {
        System.out.print("Ingrese el nombre del archivo: ");
        archivoSeleccionado = scanner.nextLine();
        String rutaArchivo = gestorArchivos.getCarpetaSeleccionada() + File.separator + archivoSeleccionado;
        if (archivoSeleccionado.endsWith(".csv")) {
            registros = Conversor.leerCSV(rutaArchivo);
        } else if (archivoSeleccionado.endsWith(".json")) {
            registros = Conversor.leerJSON(rutaArchivo);
        } else if (archivoSeleccionado.endsWith(".xml")) {
            registros = Conversor.leerXML(rutaArchivo);
        } else {
            System.out.println("Formato no reconocido.");
            return;
        }
        System.out.println("Archivo cargado con éxito. Registros: " + registros.size());
    }

    private void convertirArchivo() throws Exception {
        if (registros.isEmpty()) {
            System.out.println("No hay registros cargados para convertir.");
            return;
        }
        System.out.print("Seleccione el formato de salida (csv/json/xml): ");
        String formato = scanner.nextLine();
        System.out.print("Ingrese el nombre del archivo de salida: ");
        String nombreSalida = scanner.nextLine();
        String rutaSalida = gestorArchivos.getCarpetaSeleccionada() + File.separator + nombreSalida + "." + formato;

        if ("csv".equalsIgnoreCase(formato)) {
            Conversor.escribirCSV(rutaSalida, registros);
        } else if ("json".equalsIgnoreCase(formato)) {
            Conversor.escribirJSON(rutaSalida, registros);
        } else if ("xml".equalsIgnoreCase(formato)) {
            Conversor.escribirXML(rutaSalida, registros);
        } else {
            System.out.println("Formato no válido.");
            return;
        }
        System.out.println("Archivo convertido con éxito.");
    }
}