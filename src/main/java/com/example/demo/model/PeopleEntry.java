package com.example.demo.model;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PeopleEntry extends Entry {
    @Key
    private People entry;
}
