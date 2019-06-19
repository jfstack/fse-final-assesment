package com.jfstack.fse.projtracker.be.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Jackson serialize/deserialize LocalDate property for all fields like below.
 * "publishDate": {
     "year": 2018,
     "month": "MARCH",
     "era": "CE",
     "dayOfMonth": 21,
     "dayOfWeek": "WEDNESDAY",
     "dayOfYear": 80,
     "leapYear": false,
     "monthValue": 3,
     "chronology": {
     "id": "ISO",
     "calendarType": "iso8601"
     }
 * I want to tell jackson how to serialize LocalDate property e.g. YYYY-MM-DD
 */

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
