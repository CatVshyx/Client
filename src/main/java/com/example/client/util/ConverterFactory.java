package com.example.client.util;

import com.example.client.model.Currency;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConverterFactory {
    private static final String BASE_CURRENCY = "USD";
    private static final ArrayList<Currency> currencies = new ArrayList<>(Arrays.asList(Objects.requireNonNull(findCurrencies("UAH", "EUR", "USD"))));
    private static  final String WEBSITE = "https://api.apilayer.com/fixer";
    private static final String GET_EXCHANGE = "/latest?base=%s&symbols=%s";
    public static Currency getCurrency(String name){
        Currency found = currencies.stream().filter(local -> local.getName().equals(name))
                .findAny()
                .orElse(null);
        if (found == null){
            found = findCurrencies(name)[0];
        }
        return found;
    }
    public static void addCurrencyToList(Currency currency){
        currencies.add(currency);
    }
    public static Currency[] findCurrencies(String... additional)  {
        try {
            String property = Arrays.stream(additional).collect(Collectors.joining(",","",""));
            HttpURLConnection connection = getConnection(GET_EXCHANGE.formatted(BASE_CURRENCY,property));
            ArrayList<Currency> currencies1 = new ArrayList<>();
            String rates = new JSONObject( readISAsString(connection.getInputStream())).get("rates").toString();
            System.out.println(rates);
            JSONObject object = new JSONObject(rates);

            object.keySet().forEach(key -> {
                double number = object.getDouble(key);
                currencies1.add(new Currency(key,formatDoubleAsFloat(number)));
            });
            System.out.println(currencies1);
            return currencies1.toArray(Currency[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (JSONException  e){
            System.out.println(e.getMessage());
            return null;
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }
    private static float formatDoubleAsFloat(double number){
        return Float.parseFloat(new DecimalFormat("##.##").format(number));
    }
    private static String readISAsString(InputStream is){
        try(BufferedInputStream bis = new BufferedInputStream(is)){
            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = bis.read())  != -1){
                builder.append((char) c);
            }
            System.out.println(builder);
            return builder.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection getConnection(String endpoint)  {
        try {
            URL url = new URL(WEBSITE + endpoint);
            System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey","N4b1XMEBLhtzrmLkX9pCZmbkyYVwX68I");
            return connection;
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }
}
