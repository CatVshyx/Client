package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.RegistrationRequest;
import java.io.IOException;

public final class LogInService {
    public static boolean logout;
    public static void login(String email, String password){
        try {
            Response response =  HttpClientService.login(email,password);
            if (response.getHttpCode() != 200){
                HelloApplication.getLoginController().failedOnLogin(response.getDescription().toString());
                return;
            }
            HttpClientService.setSessionWithServer();
            while (true){
                Thread.sleep(200);

                if (Session.getApplicationMe() != null) break;
            }
            logout = false;
            HelloApplication.getLoginController().successfulLogin();
        }catch (IOException e){
            HelloApplication.getLoginController().failedOnLogin("Error during connection");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public static Response joinCompany(String invitationCode){
        try {
            return HttpClientService.joinByCode(invitationCode);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }
    public static Response register(RegistrationRequest request){
        try {
             return HttpClientService.register(request);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }


    }
    public static void logOut(){
        logout = true;
        Session.clearSession();
        HttpClientService.logout();
        HelloApplication.loadLoginFrame();

    }
    public static Response createCompany(String name){
        try {
            return HttpClientService.createCompany(name);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }

    public static Response forgotPassword(String email) {
        try {
            return HttpClientService.forgotPassword(email);
        } catch (IOException e) {
            return new Response("Something went wrong",403);
        }
    }

    public static Response resetPasswordByCode(String code) {
        try {
            return HttpClientService.sendCode(code);
        } catch (IOException e) {
            return new Response("Something went wrong",403);
        }
    }

    public static boolean isLogout() {
        return logout;
    }
}
