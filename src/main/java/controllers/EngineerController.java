package controllers;

import db.DBHelper;
import models.Department;
import models.Engineer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class EngineerController {

    public EngineerController(){
        this.setupEndPoints();
    }

    private void setupEndPoints(){


        get("/engineers", (req,res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/engineers/index.vtl");

            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("engineers", engineers);

            return new ModelAndView(model, "templates/layout.vtl");

        },  new VelocityTemplateEngine());

        get("/engineers/new", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/engineers", (req, res) -> {
            String firstName = req.queryParams("first-name");

            String lastName = req.queryParams("last-name");

            int salary = Integer.valueOf(req.queryParams("salary"));

            int departmentId = Integer.valueOf(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            Engineer newEngineer = new Engineer(firstName,lastName, salary, department);

            DBHelper.save(newEngineer);

            res.redirect("/engineers");
            return null;

        }, new VelocityTemplateEngine());


        get("/engineers/:id/edit", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);

            int engineerId = Integer.valueOf(req.params(":id"));
            Engineer engineer = DBHelper.find(engineerId, Engineer.class);
            model.put("engineer", engineer);

            model.put("template", "templates/engineers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());


        post("/engineers/:id", (req, res) -> {

            Engineer newEngineer = new Engineer();

            int Id = Integer.valueOf(req.params(":id"));

            String firstName = req.queryParams("first-name");

            String lastName = req.queryParams("last-name");

            int salary = Integer.valueOf(req.queryParams("salary"));

            int departmentId = Integer.valueOf(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            newEngineer.setId(Id);
            newEngineer.setFirstName(firstName);
            newEngineer.setLastName(lastName);
            newEngineer.setSalary(salary);
            newEngineer.setDepartment(department);


            DBHelper.save(newEngineer);

            res.redirect("/engineers");
            return null;

        }, new VelocityTemplateEngine());



        post("engineers/:id/delete", (req, res) -> {

            int engineerId = Integer.valueOf(req.params(":id"));
            Engineer engineer = DBHelper.find(engineerId, Engineer.class);


            DBHelper.delete(engineer);

            res.redirect("/engineers");
            return null;

        }, new VelocityTemplateEngine());



    }
}
