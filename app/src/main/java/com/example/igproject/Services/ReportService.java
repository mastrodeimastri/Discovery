package com.example.igproject.Services;


import com.example.igproject.Models.Report;
import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReportService  {

    public static ReportService service;

    private static FirebaseFirestore firestoreClient;

    private static FirebaseAuth authClient;

    public ReportService getService() {
        if(service == null) {
            service = new ReportService();
            firestoreClient = FirebaseFirestore.getInstance();
        }

        return service;
    }

    // funzione per recuperare tutti i report presenti in firestore
    public List<Report> getReports() {

        String uid = authClient.getCurrentUser().getUid();

        //firestoreClient.collection("Users").document(uid).collection("Reports").;
        return null;
    }


}
