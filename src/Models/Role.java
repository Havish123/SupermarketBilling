package Models;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private int Id;
    private String Name;

    private List<Employee> employees;

    public Role() {
        employees=new ArrayList<>();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
