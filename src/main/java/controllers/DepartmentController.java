package controllers;

import db.DBHelper;
import models.Department;
import models.Employee;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class DepartmentController {

    public DepartmentController() {
        this.setupEndPoints();
    }

    private void setupEndPoints() {


        get("/departments", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/departments/index.vtl");

            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);

            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        get("/departments/new", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();
            List<Employee> employees = DBHelper.getAll(Employee.class);
            model.put("employees", employees);
            model.put("template", "templates/departments/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/departments", (req, res) -> {
            String title = req.queryParams("title");

            Department newDepartment = new Department(title);

            DBHelper.save(newDepartment);

            res.redirect("/departments");
            return null;

        }, new VelocityTemplateEngine());



        get("/departments/:id/edit", (req, res) -> {

            HashMap<String, Object> model = new HashMap<>();

            int departmentId = Integer.valueOf(req.params(":id"));
            Department department = DBHelper.find(departmentId, Department.class);
            model.put("department", department);

            model.put("template", "templates/departments/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        post("/departments/:id", (req, res) -> {

            Department newDeparment = new Department();

            int Id = Integer.valueOf(req.params(":id"));

            String title = req.queryParams("title");

            newDeparment.setId(Id);
            newDeparment.setTitle(title);

            DBHelper.save(newDeparment);

            res.redirect("/departments");
            return null;

        }, new VelocityTemplateEngine());




        post("departments/:id/delete", (req, res) -> {

            int departmentId = Integer.valueOf(req.params(":id"));
            Department department = DBHelper.find(departmentId, Department.class);


            DBHelper.delete(department);

            res.redirect("/departments");
            return null;

        }, new VelocityTemplateEngine());


    }
}
