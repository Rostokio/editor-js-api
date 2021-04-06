package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    @Key
    private String id;

    @Key
    private String displayName;

    @Key
    private String firstName;

    @Key
    private String lastName;

    @Key
    private String password;

    @Key
    private String email;


}
