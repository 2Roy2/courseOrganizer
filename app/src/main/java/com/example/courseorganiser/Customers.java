package com.example.courseorganiser;

import java.util.ArrayList;

public class Customers {
    ArrayList<Customer> customers;

    public Customers(){
        customers= new ArrayList<Customer>();
    }
    public void addCustomer(String name){
        Customer customer = new Customer(name,false);
        customers.add(customer);
    }

}
