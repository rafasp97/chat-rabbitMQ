package sd_ufs.chat_rabbitmQ.services;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


@Service
public class RabbitManagementService {

    private static final String URL = "http://localhost:15672/api";
    private static final String USER = "guest";
    private static final String PASSWORD = "guest";
    private final HttpClient client = HttpClient.newHttpClient();

    private String authorization() {

        String auth = USER + ":" + PASSWORD;

        return Base64.getEncoder()
                .encodeToString(
                        auth.getBytes(StandardCharsets.UTF_8)
                );
    }

    private String get(String endpoint) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(URL + endpoint))
                        .header(
                                "Authorization",
                                "Basic " + authorization()
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    public List<String> listGroups(String user) {

        List<String> groups = new ArrayList<>();

        try {

            String response = get("/queues/%2F/" + user + "/bindings");

            JSONArray bindings = new JSONArray(response);

            for (int i = 0; i < bindings.length(); i++) {

                JSONObject binding = bindings.getJSONObject(i);

                String source = binding.getString("source");

                if (!source.isBlank()) {
                    groups.add(source);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar grupos.");
            e.printStackTrace();
        }

        return groups;
    }

    public List<String> listUsers(String group) {

        List<String> users = new ArrayList<>();

        try {

            String response = get("/exchanges/%2F/" + group + "/bindings/source");

            JSONArray bindings = new JSONArray(response);

            for (int i = 0; i < bindings.length(); i++) {

                JSONObject binding = bindings.getJSONObject(i);

                String destination = binding.getString("destination");
                String destinationType = binding.getString("destination_type");

                if ("queue".equals(destinationType)) {
                    users.add(destination);
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar usuários do grupo.");
            e.printStackTrace();
        }

        return users;
    }
}