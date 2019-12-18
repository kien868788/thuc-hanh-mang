package com.kien.thi.server;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Examiner {
    String name;

    String note;

    Room room;

    public Examiner(String name, String note) {
        this.name = name;
        this.note = note;
    }
}
