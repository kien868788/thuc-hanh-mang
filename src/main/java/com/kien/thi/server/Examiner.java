package com.kien.thi.server;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Examiner {
    String id;
    String birthDate;
    String unit;
    String name;
    String note;

    Room room;

    public Examiner(String id, String birthDate, String unit, String name, String note) {
        this.id = id;
        this.birthDate = birthDate;
        this.unit = unit;
        this.name = name;
        this.note = note;
    }
}
