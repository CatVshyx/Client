package com.example.client.service;

import com.example.client.additional.AuthenticationResponse;
import com.example.client.additional.Response;
import com.example.client.model.Company;
import com.example.client.model.Product;
import com.example.client.model.RegistrationRequest;
import com.example.client.model.User;
import com.example.client.util.PropertyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class HttpClientService {
//    "https://test-server-spring.onrender.com/"
//    https://test-back-ncm8.onrender.com/
//    https://localhost:8080/
    private static final String standartURL = PropertyUtil.read("server.link");
    private static AuthenticationResponse tokens;
    public static void logout() {
        tokens = null;
    }
    public static Response changeUserCredentials(HashMap<String, String> map) throws IOException {
        HttpURLConnection connection = makeRequest("user/changeUserSettings","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        JSONObject object = new JSONObject(map);

        writeRequest(connection,object.toString());

        return getResponse(connection);
    }
    public static void setSessionWithServer() {
        try{
            URL url = new URL(standartURL + "user/getMe");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            System.out.println("code " +code);
            if (code != 200) {
                System.out.println(code+" "+readValue(connection.getErrorStream()));
            }

            User me = new ObjectMapper().readValue(connection.getInputStream(), User.class);
            Company company = getData();
            Session.setSession(company,me);
        }catch (IOException ignored){
            ignored.printStackTrace();
        }
    }

    public static Response deleteUser(User user) throws IOException {
        HttpURLConnection connection = makeRequest("company/deleteUser","DELETE");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        JSONObject object = new JSONObject();
        object.put("email",user.getEmail());
        writeRequest(connection,object.toString());
        return getResponse(connection);
    }
    public static Response changePassword(String currPassword, String newPassword) throws IOException {
        HttpURLConnection connection = makeRequest("user/changePassword","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        JSONObject object = new JSONObject();
        object.put("current_password",currPassword);
        object.put("new_password",newPassword);
        writeRequest(connection,object.toString());

        return getResponse(connection);
    }

    public static Response createCompany(String name) throws IOException {
        HttpURLConnection connection = makeRequest("company/createCompany","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());


        JSONObject object =new JSONObject();
        object.put("name",name);
        writeRequest(connection,object.toString());

        return getResponse(connection);
    }

    public static Response addNewProduct(Product product) throws IOException {
        HttpURLConnection connection = makeRequest("res/addProduct","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        String obj  = new ObjectMapper().writeValueAsString(product);
        writeRequest(connection,obj);

        return getResponse(connection);
    }
    public static Response deleteProduct(Product product) throws IOException{
        HttpURLConnection connection = makeRequest("res/deleteProduct","DELETE");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        JSONObject object = new JSONObject();
        object.put("id",product.getId());

        writeRequest(connection,object.toString());

        return getResponse(connection);
    }

    public static Response editProduct(Product product) throws IOException {
        HttpURLConnection connection = makeRequest("res/editProduct","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        String obj  = new ObjectMapper().writeValueAsString(product);
        writeRequest(connection,obj);

        return getResponse(connection);
    }

    public static Response sendInvitation(String request) throws IOException {
        HttpURLConnection connection = makeRequest("company/inviteBySpecification","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        writeRequest(connection,request);

        return getResponse(connection);
    }

    public static Response changeAllows(String request) throws IOException {
        HttpURLConnection connection = makeRequest("company/changeAllows","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());
        writeRequest(connection,request);

        return getResponse(connection);
    }

    public static Response addDiscount(String request) throws IOException {
        HttpURLConnection connection = makeRequest("res/addDiscount","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        writeRequest(connection,request);

        return getResponse(connection);
    }

    public static Response forgotPassword(String text) throws IOException {
        HttpURLConnection connection = makeRequest("auth/forgotPassword","POST");
        JSONObject object = new JSONObject();
        object.put("email",text);
        writeRequest(connection,object.toString());

        return getResponse(connection);
    }

    public static Response sendCode(String request) throws IOException {
        HttpURLConnection connection = makeRequest("auth/resetPassword","POST");

        writeRequest(connection,request);

        return getResponse(connection);

    }

    public static Response leaveCompany() throws IOException {
        HttpURLConnection connection = makeRequest("company/leaveCompany","GET");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        return getResponse(connection);
    }

    public static Response sellProduct(String request) throws IOException {
        HttpURLConnection connection = makeRequest("res/sellProduct","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        writeRequest(connection,request);
        return getResponse(connection);
    }
    public static void refreshToken() throws IOException {
        if (tokens == null) throw new IOException();
        HttpURLConnection connection = makeRequest("auth/token/refresh","POST");
        System.out.println(" old "+tokens);
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getRefresh_token());
        Response response = getResponse(connection);
        if (response.getHttpCode() != 200) {
            throw new IOException();
        }else{
            ObjectMapper mapper = new ObjectMapper();
            tokens = mapper.readValue(response.getDescription().toString(), AuthenticationResponse.class);
        }
    }
    public static Response editDiscount(String request) throws IOException {
        HttpURLConnection connection = makeRequest("res/editDiscount","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        writeRequest(connection,request);
        return getResponse(connection);
    }

    public static Response deleteDiscount(String request) throws IOException {
        HttpURLConnection connection = makeRequest("res/deleteDiscount","DELETE");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        writeRequest(connection,request);
        return getResponse(connection);
    }
    public static InputStream getUserPhoto(String id) throws IOException {
        HttpURLConnection connection = makeRequest("auth/media/" + id,"GET");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());
        System.out.println(id);
        int code = connection.getResponseCode();
        if (code != 200) {
            getResponse(connection);
        }
        return connection.getInputStream();
    }


    public static Response uploadNewPhoto(String filePath) throws IOException {
        URL url = new URL(standartURL + "user/uploadPhoto");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println(filePath);
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----Boundary");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("file doesnt exist");
            return null;
        };

        byte[] arr = Files.readAllBytes(f.toPath());
        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

        String boundary = "----Boundary";
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + f.getName() + "\"\r\n");
        dos.writeBytes("\r\n");

        dos.write(arr);
        dos.writeBytes("\r\n");
        dos.writeBytes("--" + boundary + "--\r\n");
        dos.flush();
        dos.close();
        Response response = getResponse(connection);
        System.out.println(response + " response from uploading");
        System.out.println("++++");
        return response;
    }

    public static Company getData() throws IOException {
        HttpURLConnection connection = makeRequest("company/getData","GET");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        int code = connection.getResponseCode();
        String description = (code != 200) ? readValue(connection.getErrorStream()) : readValue(connection.getInputStream());
        System.out.println(code+" "+description);
        System.out.println("here");
        if (code == 200){
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(description, Company.class);
        }

        return null;
    }

    public static Response register(RegistrationRequest user) throws IOException {
        HttpURLConnection connection = makeRequest("auth/register","POST");

        writeRequest(connection, new ObjectMapper().writeValueAsString(user));

        return getResponse(connection);
    }
    public static Response joinByCode(String code) throws IOException {
        HttpURLConnection connection = makeRequest("company/joinByCode","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        JSONObject object =new JSONObject();
        object.put("code",code);
        writeRequest(connection,object.toString());

        return getResponse(connection);
    }
    public static Response sendHelp(HashMap<String,String> map) throws IOException {
        HttpURLConnection connection = makeRequest("help/sendQuestion","POST");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());
        JSONObject object = new JSONObject(map);

        writeRequest(connection,object.toString());

       return getResponse(connection);
    }
    public static Response login(String email, String password) throws IOException {
        HttpURLConnection connection = makeRequest("auth/login","POST");

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        writeRequest(connection, new ObjectMapper().writeValueAsString(map));

        int code = connection.getResponseCode();
        String description = "Something went wrong";

        if (code != 200) {
            if (connection.getErrorStream() != null)
                description = readValue(connection.getErrorStream());
        }else{
            ObjectMapper mapper = new ObjectMapper();
            tokens = mapper.readValue(connection.getInputStream(), AuthenticationResponse.class);
            setTokenUpdateThread();

        }
        return new Response(description, code);
    }

    private static void writeRequest(HttpURLConnection connection, String str) throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = str.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }

    private static String readValue(InputStream errorStream) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                builder.append(responseLine);
            }
            return builder.toString();
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }
    private static HttpURLConnection makeRequest(String path, String request) throws IOException {
        URL url = new URL(standartURL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(request);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }
    private static Response getResponse(HttpURLConnection connection) throws IOException {

        int code = connection.getResponseCode();
        String description;
        if(code == 503){description = "Service is unavailable";}
        else if(code != 200 && code != 403){ description = readValue(connection.getErrorStream());}
        else {
            InputStream is = connection.getInputStream();
            System.out.println(connection.getResponseMessage());
            description = is == null ? "Ok" : readValue(connection.getInputStream());
        }
        return new Response(description,code);

    }

    private static void setTokenUpdateThread(){
        TokenUpdate tokenUpdate = new TokenUpdate();
        Thread thread = new Thread(tokenUpdate,"refresh_token");
        thread.setDaemon(true);
        thread.start();
    }
    static class TokenUpdate implements Runnable {
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1000 * 40);
                    HttpClientService.refreshToken();
                } catch (InterruptedException | IOException e) {
                    if(!LogInService.isLogout()){ Platform.runLater(LogInService::logOut); }
                    System.out.println("refresh token stream interrupted");
                    break;
                }
            }
        }
    }


}
