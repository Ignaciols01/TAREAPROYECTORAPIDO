import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Conversor {

    public static List<Map<String, String>> leerCSV(String ruta) throws IOException {
        List<Map<String, String>> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String[] cabeceras = br.readLine().split(",");
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                Map<String, String> registro = new HashMap<>();
                for (int i = 0; i < cabeceras.length; i++) {
                    registro.put(cabeceras[i], valores[i]);
                }
                registros.add(registro);
            }
        }
        return registros;
    }

    public static List<Map<String, String>> leerJSON(String ruta) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(ruta)) {
            Type tipoLista = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> registrosGenericos = gson.fromJson(reader, tipoLista);
            
            List<Map<String, String>> registros = new ArrayList<>();
            for (Map<String, Object> registro : registrosGenericos) {
                Map<String, String> nuevoRegistro = new HashMap<>();
                for (Map.Entry<String, Object> entry : registro.entrySet()) {
                    nuevoRegistro.put(entry.getKey(), entry.getValue().toString());
                }
                registros.add(nuevoRegistro);
            }
            return registros;
        } catch (Exception e) {
            throw new IOException("Error al leer el JSON: " + e.getMessage());
        }
    }

    public static List<Map<String, String>> leerXML(String ruta) throws Exception {
        List<Map<String, String>> registros = new ArrayList<>();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(ruta));
        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) nodeList.item(i);
                Map<String, String> registro = new HashMap<>();
                NodeList hijos = elemento.getChildNodes();
                for (int j = 0; j < hijos.getLength(); j++) {
                    if (hijos.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element campo = (Element) hijos.item(j);
                        registro.put(campo.getTagName(), campo.getTextContent());
                    }
                }
                registros.add(registro);
            }
        }
        return registros;
    }

    public static void escribirCSV(String ruta, List<Map<String, String>> registros) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            if (registros.isEmpty()) return;

            Set<String> cabeceras = registros.get(0).keySet();
            bw.write(String.join(",", cabeceras) + "\n");

            for (Map<String, String> registro : registros) {
                List<String> valores = new ArrayList<>();
                for (String cabecera : cabeceras) {
                    valores.add(registro.getOrDefault(cabecera, ""));
                }
                bw.write(String.join(",", valores) + "\n");
            }
        }
    }

    public static void escribirJSON(String ruta, List<Map<String, String>> registros) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ruta)) {
            gson.toJson(registros, writer);
        }
    }

    public static void escribirXML(String ruta, List<Map<String, String>> registros) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("registros");
        doc.appendChild(rootElement);

        for (Map<String, String> registro : registros) {
            Element registroElement = doc.createElement("registro");
            rootElement.appendChild(registroElement);

            for (Map.Entry<String, String> entry : registro.entrySet()) {
                Element campo = doc.createElement(entry.getKey());
                campo.appendChild(doc.createTextNode(entry.getValue()));
                registroElement.appendChild(campo);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(ruta));
        transformer.transform(source, result);
    }
}
