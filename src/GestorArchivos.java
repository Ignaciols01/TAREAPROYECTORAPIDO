import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GestorArchivos {
    private Path carpetaSeleccionada;

    public void seleccionarCarpeta(String ruta) throws IOException {
        Path carpeta = Paths.get(ruta);
        if (Files.exists(carpeta) && Files.isDirectory(carpeta)) {
            this.carpetaSeleccionada = carpeta;
        } else {
            throw new IOException("La carpeta no existe o no es un directorio.");
        }
    }

    public List<String> listarArchivos() throws IOException {
        if (carpetaSeleccionada == null) {
            throw new IOException("No se ha seleccionado ninguna carpeta.");
        }
        List<String> archivos = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpetaSeleccionada)) {
            for (Path entry : stream) {
                archivos.add(entry.getFileName().toString());
            }
        }
        return archivos;
    }

    public Path getCarpetaSeleccionada() {
        return carpetaSeleccionada;
    }
}