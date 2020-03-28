package com.project.fit;

public class users  {
    private String Name;
    private int Age;
    private String City_state;
    private String Email;
    private double Total_steps;



    public users(){

    }

    public users(String name, int age, String city_state, String email, double total_steps) {
        Name = name;
        Age = age;
        City_state = city_state;
        Email = email;
        Total_steps = total_steps;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getCity_state() {
        return City_state;
    }

    public void setCity_state(String city_state) {
        City_state = city_state;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUser_name() {
        return Name;
    }

    public void setUser_name(String user_name) {
        this.Name = user_name;
    }

    public double getTotal_steps() {
        return Total_steps;
    }

    public void setTotal_steps(double total_steps) {
        this.Total_steps = total_steps;
    }

//    @Override
//    public int compareTo(Object o) {
//        double comparesteps=((users)o).getTotal_steps();
//        /* For Ascending order*/
//        return (int) (this.total_steps-comparesteps);
//
//        /* For Descending order do like this */
//        //return compareage-this.studentage;
//    }
}
