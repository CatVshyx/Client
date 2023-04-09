package com.example.client.service;

import com.example.client.model.Company;
import com.example.client.model.Product;
import com.example.client.model.User;
import javafx.application.Platform;
import java.util.Set;

public class Session {

    private static User applicationMe = null;
    private static Company myCompany = null;
    private static Thread thread;

    public Session() {
    }

    public static User getApplicationMe() {
        return applicationMe;
    }
    public static Company getMyCompany() {
        return myCompany;
    }

    public static void setSession(Company company, User me){
        // This method sets given data to all the services - discount admin, storage finance
        // if the user is a member of a company
        // and it sets session stream - another thread with Tasks (every 5 minutes sends request to update data )

        Thread.getAllStackTraces().keySet().forEach(thread1 -> System.out.println("                   -"+thread1.getName()));
        myCompany = company;
        applicationMe = me;
        if (myCompany == null || me == null){
            if(thread != null && thread.isAlive()){ thread.interrupt(); }
            return;
        }
        System.out.println("set session " + company + "  \n user" + me);
        Set<Product> productSet = myCompany.getProducts();
        DiscountService.setData(productSet);
        StorageService.setData(productSet);
        AdministrationService.setData(myCompany.getUsers());
        FinanceService.setData(myCompany.getRevenues());
        setSessionStream();
    }
    private static void setSessionStream(){
        if(thread == null || !thread.isAlive()){
            SessionStream stream = new SessionStream();
            thread = new Thread(stream, "sessionStream");
            thread.setDaemon(true);
            thread.start();
        }
    }
    public static void clearSession(){
        myCompany = null;
        applicationMe = null;
        StorageService.clearData();
        DiscountService.clearDiscountData();
        FinanceService.clearFinanceData();
        AdministrationService.clearAdministrationData();
        if (thread != null && thread.isAlive()){
            thread.interrupt();
        }
    }
    static class SessionStream implements Runnable{
        @Override
        public void run() {
            try {
                while (true){
                    System.out.println("SESSION UPDATED");
                    Thread.sleep(1000 * 60);
                    HttpClientService.setSessionWithServer();
                }
            } catch (InterruptedException e) {
                if(!LogInService.isLogout()){ Platform.runLater(LogInService::logOut); }
                System.out.println("session stream was interrupted");
            }
        }
    }
}
