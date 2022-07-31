package com.jcoding.zenithanalysis;

import com.jcoding.zenithanalysis.dto.RegisterUser;
import com.jcoding.zenithanalysis.entity.*;
import com.jcoding.zenithanalysis.repository.RolesRepo;
import com.jcoding.zenithanalysis.services.AdminServices;
import com.jcoding.zenithanalysis.services.AppUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class ZenithanalysisApplication implements CommandLineRunner {

	@Autowired
	AdminServices adminServices;

	@Autowired
	AppUserServices appUserServices;

	@Autowired
	RolesRepo rolesRepo;

	public static void main(String[] args) {
		SpringApplication.run(ZenithanalysisApplication.class, args);
	}

	@Override
	public void run(String...args){

		Roles userRole = new Roles("USER");
		Roles adminRole = new Roles("ADMIN");

		userRole = rolesRepo.save(userRole);
		adminRole = rolesRepo.save(adminRole);


//		AppUser gabriel = new AppUser("gabriel@gmail.com","Gabriel Petr", "11111111",userRole);
//		AppUser paul = new AppUser("paul@gmail.com","Paul Petr", "11111111",userRole );
//		AppUser john = new AppUser("john@gmail.com","John Petr", "11111111",userRole );
//		AppUser racky = new AppUser("racky@gmail.com","Racky Petr", "11111111",userRole );
//		AppUser king = new AppUser("king@gmail.com","King Petr", "11111111",userRole );
//
//
//		Course course1 = new Course("Admin","2000","Administration course");
//		Course course2 = new Course("Marketing","550","Marketing course");
//		Course course3 = new Course("Analysts","1120","Analysts course");
//
//		course1 = adminServices.addCourse(course1);
//		course2 = adminServices.addCourse(course2);
//		course3 = adminServices.addCourse(course3);
//
//		Assignment assignment1 = new Assignment("Factor of Business","Get the needed part of the factor of business",course2);
//		Assignment assignment2 = new Assignment("Factor of Business","Get the needed part of the factor of business",course2);
//		Assignment assignment3 = new Assignment("Factor of Business","Get the needed part of the factor of business",course2);
//
//		adminServices.addAssignment(assignment1);
//		adminServices.addAssignment(assignment2);
//		adminServices.addAssignment(assignment3);
//
//		gabriel = appUserServices.createUser(new RegisterUser(gabriel.getName(), gabriel.getEmail(), gabriel.getPassword(), gabriel.getPassword()));
//		paul = appUserServices.createUser(new RegisterUser(paul.getName(), paul.getEmail(), paul.getPassword(), paul.getPassword()));
//		john = appUserServices.createUser(new RegisterUser(john.getName(), john.getEmail(), john.getPassword(), john.getPassword()));
//		racky = appUserServices.createUser(new RegisterUser(racky.getName(), racky.getEmail(), racky.getPassword(), racky.getPassword()));
//		king = appUserServices.createUser(new RegisterUser(king.getName(), king.getEmail(), king.getPassword(), king.getPassword()));
//
//
//		RegisterCourse registerCourse1 = new RegisterCourse(gabriel,course1, LocalDate.now().toString());
//		RegisterCourse registerCourse2 = new RegisterCourse(paul,course1, LocalDate.now().toString());
//		RegisterCourse registerCourse3 = new RegisterCourse(john,course1, LocalDate.now().toString());
//		RegisterCourse registerCourse4 = new RegisterCourse(racky,course1, LocalDate.now().toString());
//		RegisterCourse registerCourse5 = new RegisterCourse(king,course1, LocalDate.now().toString());
//		RegisterCourse registerCourse6 = new RegisterCourse(gabriel,course2, LocalDate.now().toString());
//		RegisterCourse registerCourse7 = new RegisterCourse(paul,course2, LocalDate.now().toString());
//		RegisterCourse registerCourse8 = new RegisterCourse(john,course2, LocalDate.now().toString());
//		RegisterCourse registerCourse9 = new RegisterCourse(gabriel,course3, LocalDate.now().toString());
//		RegisterCourse registerCourse10 = new RegisterCourse(racky,course3, LocalDate.now().toString());
//
//		adminServices.addCourseRegister(registerCourse1);
//		adminServices.addCourseRegister(registerCourse2);
//		adminServices.addCourseRegister(registerCourse3);
//		adminServices.addCourseRegister(registerCourse4);
//		adminServices.addCourseRegister(registerCourse5);
//		adminServices.addCourseRegister(registerCourse6);
//		adminServices.addCourseRegister(registerCourse7);
//		adminServices.addCourseRegister(registerCourse8);
//		adminServices.addCourseRegister(registerCourse9);
//		adminServices.addCourseRegister(registerCourse10);

	}

	@Bean
	public PasswordEncoder encode(){
		return new BCryptPasswordEncoder();
	}


}
