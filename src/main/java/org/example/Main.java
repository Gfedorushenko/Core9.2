package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String url = "https://api.nasa.gov/planetary/apod?api_key=lJuXLlKIbfgv8IWzscpoJWlDRf4nae6GKOzrePyR";
        String newUrl = "";
        String mediaType = "";
        String text = "";
        while (!mediaType.equals("image")) {
            text = httpToSting(url);
            Post post = mapper.readValue(text, Post.class);
            mediaType = post.getMediaType();
            newUrl = post.getUrl();
        }

        String[] pats = newUrl.split("/");
        String fileName = pats[pats.length - 1];

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(newUrl);
        InputStream stream = httpClient.execute(request).getEntity().getContent();

        FileOutputStream fos = new FileOutputStream(fileName);
        byte date[] = new byte[1024];
        int count;
        while ((count = stream.read(date, 0, 1024)) != -1) {
            fos.write(date, 0, count);
            fos.flush();
        }
        stream.close();
        httpClient.close();
    }

    public static String httpToSting(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntity entity = httpClient.execute(request).getEntity();
        String text = EntityUtils.toString(entity);
        httpClient.close();
        return text;
    }
}
