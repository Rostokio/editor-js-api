package com.example.demo.model;


import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class PeopleList {

    @Key
    private List<PeopleEntry> list;
}
