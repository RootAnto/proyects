package com.rootanto.airline.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class main {
    public static void main(String[] args) {
        methotDate("02/10/1990");
    }

    private static void methotDate(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date parseDate = formatter.parse(date);
            System.out.println("The date is: " + parseDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
