package org.educa.api;

import org.educa.execption.NotFoundException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Connection {

    public String doGet(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()){
            HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (respuesta.statusCode() == 200) {
                return respuesta.body();
            } else if (respuesta.statusCode() == 404) {
                throw new NotFoundException("Dont flyght for id");
            } else {
                throw new Exception("Error: " + respuesta.statusCode());
            }

        }

    }

    public void doPost(String body, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        try (HttpClient client = HttpClient.newHttpClient()){
            HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(respuesta.body());

        }
    }

    public String doUpdate(String body, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        try (HttpClient client = HttpClient.newHttpClient()){
            HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(respuesta.body());

            if (respuesta.statusCode() == 200) {
                return respuesta.body();
            } else if (respuesta.statusCode() == 404) {
                throw new NotFoundException("..");
            } else {
                throw new Exception("Error: " + respuesta.statusCode());
            }
        }

    }

    public String doDelete(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()){
            HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(respuesta.body());

            if (respuesta.statusCode() == 200) {
                return respuesta.body();
            } else if (respuesta.statusCode() == 404) {
                throw new NotFoundException(",,,");
            } else {
                throw new Exception("Error: " + respuesta.statusCode());
            }
        }

    }
}