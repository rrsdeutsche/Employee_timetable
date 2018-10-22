package app.springbootdemo.controller;


import app.springbootdemo.controller.mapper.EmployeeViewMapper;
import app.springbootdemo.controller.model.*;
import app.springbootdemo.database.dbmodel.Employee;
import app.springbootdemo.database.repository.EmployeeRepository;
import app.springbootdemo.reports.ReportBean;
import app.springbootdemo.reports.ReportService;
import app.springbootdemo.service.EmployeeService;
import app.springbootdemo.service.mapper.*;
import app.springbootdemo.service.model.EmployeeBO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
//@CrossOrigin(origins = "http://localhost:4200, credentials: true")

@RestController

@RequestMapping("/api")


public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ReportService reportService;

	@ApiOperation(value = "returns something")
	@GetMapping(value="/employee",  produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Employee> getAll() {
		return employeeService.getAll();
	}

	@PostMapping(value="/postemployee" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public EmployeeView postEmployee(@RequestBody EmployeeView employeeView) {
		EmployeeBO employeeBO = EmployeeBOMapper.from(employeeView);
		employeeBO = employeeService.postEmployee(employeeBO);
		EmployeeView employeeView1 = EmployeeViewMapper.from(employeeBO);
		return employeeView1;
	}

	@GetMapping(value="/findByLastName/{lastName}",  produces=MediaType.APPLICATION_JSON_VALUE)

//	public ResponseEntity <Employee> findByLastName (@PathVariable("lastName") String lastName)
//	{
//		Employee emp = EmployeeService.findByLastName(lastName);
//		if(emp==null) {
//			return ResponseEntity.notFound().build();
//
//		}
//		return ResponseEntity.ok().body(emp);
//	}
	public List<Employee> findByLastName(@PathVariable("lastName") String lastName) {
		return employeeService.findByLastName(lastName);
	}

	@DeleteMapping(value="/employee/{id}/delete")
	public void deleteEmployee(@PathVariable long id){
		employeeService.deleteEmployee(id);
	}


	@PostMapping(value="/ill" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void ill(@RequestBody IllView illView) {
		System.out.println("****************************************    " + illView.getEmpId());

//		{
//			illView.getIllFromDate();
//			illView.getIllToDate();
//
//		}

		employeeService.ill(IllBOMapper.from(illView));
	}


	@PostMapping(value="/holiday" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void holiDay(@RequestBody HoliDayView holiDayView) {
		employeeService.holiDay(HoliDayBOMapper.from(holiDayView));
	}


	@PostMapping(value="/employee/{id}/start" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void startTime(@PathVariable long id) {
		employeeService.startTime(id);
	}

	@PostMapping(value="/employee/{id}/stop" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void endTime(@PathVariable long id) {
		employeeService.endTime(id);
	}

	@PostMapping(value="/employee/startBreakTime/{id}" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void startBreakTime(@PathVariable long id) {
		employeeService.startBreakTime(id);

	}


	@PostMapping(value="/employee/stopBreakTime/{id}" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void endBreakTime(@PathVariable long id) {
		employeeService.stopBreakTime(id);
	}



	@PostMapping(value="/employee/address" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addAddress(@RequestBody AddressView addressView) {
		employeeService.addAddress(AddressBOMapper.from(addressView));
	}

	@PostMapping(value="/employee/telephone" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addContactDetails(@RequestBody TelephoneView telephoneView) {
		employeeService.addContactDetails(TelephoneBOMapper.from(telephoneView));
	}


	//@GetMapping(value="/findbyId/{id}",  produces=MediaType.APPLICATION_JSON_VALUE)
	//public Optional<Employee> findEmployeewithId(@PathVariable("id") long id) {
	//	return employeeService.findEmployeewithId(id);
	//}

	@PutMapping(value="/employee/{id}/updateemployeedetails" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateEmployeeDetails(@PathVariable("id") long id,@RequestBody EmployeeView employeeView) {
		EmployeeBO  employeeBO = EmployeeBOMapper.from(employeeView);
		employeeService.updateEmployeeDetails(employeeBO,id);
	}
/*
	@PostMapping(value="/employee/illStartTime/{id}" ,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void illStartTime(@PathVariable long id) {
		employeeService.illStartTime(id);
	}
*/
    @GetMapping(value="/report/monthview/{id}",  produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ReportBean> employeeMonthViewReport(@PathVariable("id") long id) {
	     return reportService.getReport(id);
}


}
