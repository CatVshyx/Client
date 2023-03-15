package com.example.client.service;

import com.example.client.additional.AuthenticationResponse;
import com.example.client.additional.Response;
import com.example.client.model.Company;
import com.example.client.model.Product;
import com.example.client.model.RegistrationRequest;
import com.example.client.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class HttpClientService {
    private static final String standartURL = "https://test-back-ncm8.onrender.com/";
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
            User me;
            if (code != 200) {
                System.out.println(code+" "+readValue(connection.getErrorStream()));
            }
            ObjectMapper mapper = new ObjectMapper();
            me = mapper.readValue(connection.getInputStream(), User.class);
            Company company = getData();
            Session.setSession(company,me);
//            System.out.println(Session.getApplicationMe().toString() );
//            System.out.println(Session.getMyCompany().toString());
        }catch (IOException e){
            e.printStackTrace();
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

//        String obj  = new ObjectMapper().writeValueAsString(product);
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

    public void getGreeting() throws IOException {
        HttpURLConnection connection = makeRequest("auth/","GET");
        StringBuffer responseDescription = new StringBuffer();

        try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
            int c;
            while ((c = bis.read()) != -1) {
                responseDescription.append((char) c);
            }
            System.out.println(responseDescription);
        }
        connection.disconnect();

    }

    public static File getUserPhoto(String userEmail) throws IOException {
        HttpURLConnection connection = makeRequest("company/loadUserImage" + "?email=%s".formatted(userEmail),"GET");
        connection.setRequestProperty("Authorization", "Bearer " + tokens.getAccess_token());

        File file = new File("src/main/resources/templates/"+userEmail+"_photo.png");
        if (!file.exists()) {file.createNewFile();}
        else {return file;}

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

        }
        System.out.println("wrote");
        Response response = getResponse(connection);
        System.out.println(response);
        if (response.getHttpCode() != 200){
            file.delete();
            return null;
        }
        return file;
    }


    public static Response uploadNewPhoto(String filePath) throws IOException {
        URL url = new URL(standartURL + "user/uploadUserPhoto");
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
        if(code != 200 && code != 403)
            description = readValue(connection.getErrorStream());
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
                    System.out.println("th");
                    Thread.sleep(1000 * 60 * 60);
                    HttpClientService.refreshToken();
                } catch (InterruptedException | IOException e) {
                    break;
                }
            }
        }
    }


}
