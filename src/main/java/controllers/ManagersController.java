package controllers;

import db.DBHelper;
import models.Department;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {
    
    public ManagersController(){
        this.setupEndPoints();
    }
    
    private void setupEndPoints(){


        get("/managers", (req,res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/managers/index.vtl");

            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);

            return new ModelAndView(model, "templates/layout.vtl");

        },  new VelocityTemplateEngine());

        get("/managers/new", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/managers", (req, res) -> {
            String firstName = req.queryParams("first-name");

            String lastName = req.queryParams("last-name");

            int salary = Integer.valueOf(req.queryParams("salary"));

            double budget = Double.valueOf(req.queryParams("budget"));

            int departmentId = Integer.valueOf(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            Manager newManager = new Manager(firstName,lastName, salary, department, budget );

            DBHelper.save(newManager);

            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());


        get("/managers/:id/edit", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);

            int managerId = Integer.valueOf(req.params(":id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            model.put("manager", manager);

            model.put("template", "templates/managers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());


        post("/managers/:id", (req, res) -> {

            Manager newManager = new Manager();

            int Id = Integer.valueOf(req.params(":id"));

            String firstName = req.queryParams("first-name");

            String lastName = req.queryParams("last-name");

            int salary = Integer.valueOf(req.queryParams("salary"));

            double budget = Double.valueOf(req.queryParams("budget"));

            int departmentId = Integer.valueOf(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            newManager.setId(Id);
            newManager.setFirstName(firstName);
            newManager.setLastName(lastName);
            newManager.setSalary(salary);
            newManager.setDepartment(department);
            newManager.setBudget(budget);


            DBHelper.save(newManager);

            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());
        


        post("managers/:id/delete", (req, res) -> {

            int managerId = Integer.valueOf(req.params(":id"));
            Manager manager = DBHelper.find(managerId, Manager.class);


            DBHelper.delete(manager);

            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());



    }
}
