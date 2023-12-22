package org.example;

import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    private static List<Request> requests = new ArrayList<>();

    static void addRequest(Request r){
        requests.add(r);
    }

    static void deleteRequest(Request r){
        requests.remove(r);
    }
}
