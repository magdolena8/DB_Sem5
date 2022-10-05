package com.begdev.lab_6_contacts;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {
    public String name;
    public String surname;
    public String phone;
    public Date bday;

    public Person (String name, String surname,String phone, Date bday){
        this.name = name;
        this.surname = surname;
        this.bday = bday;
        this.phone = phone;
    }


    public String getName(){return name;}
    public String getSurname(){return surname;}
    public Date getBday(){return bday;}
    public String getPhone(){return phone;}

    public boolean setName(String name){
        this.name = name;
        return true;
    }
    public boolean setSurname(String surname){
        this.surname = surname;
        return true;
    }
    public boolean setBday(Date bday){
        this.bday = bday;
        return true;
    }
    public boolean setPhone(String phone){
        this.phone = phone;
        return true;
    }
}

