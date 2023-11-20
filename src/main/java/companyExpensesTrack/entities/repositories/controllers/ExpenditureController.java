package companyExpensesTrack.entities.repositories.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import companyExpensesTrack.entities.Expenditure;
import companyExpensesTrack.entities.repositories.ExpenditureRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class ExpenditureController {
	@Autowired 
	ExpenditureRepository expenditureRepo;
	
	@Operation(summary="list of expenditure")
	@GetMapping("/getexpenditures")
	public List<Expenditure> getExpenditures() {
		return expenditureRepo.findAll();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary="add Expenditure",description="function describes adding of expenditures")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PostMapping("/addExpenditure")
	public ResponseStatusException addExpenditure(@Valid @RequestBody Expenditure exp) {
			  expenditureRepo.save(exp);
			 return new ResponseStatusException(HttpStatus.OK,"added expenditure");
	 }
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary="update Expenditure",description="this function describes updation of expenditures  ")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="updated data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PutMapping("/updateExpenditure/{id}")
	public ResponseStatusException updateExpendituresById( @PathVariable("id") Integer id,@RequestParam("expRemarks") String expRemarks) {
		 Optional<Expenditure> op= expenditureRepo.findById(id);
		 if(op.isPresent()) {
			 Expenditure exp=op.get();
			 exp.setExpRemarks(expRemarks);
			 expenditureRepo.save(exp);
			 return new ResponseStatusException(HttpStatus.OK,"updated expenditure");
		 }else {
			 return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid expenditure id");
		 }
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@Operation(summary="delete Expenditure",description="This function involves deletion of expenditure ")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="deleted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@DeleteMapping("/deleteExpenditure/{id}")
	public ResponseStatusException deleteExpenditure(@PathVariable("id") Integer id) {
		Optional<Expenditure> exp=expenditureRepo.findById(id);
		if(exp.isPresent()) {
			expenditureRepo.deleteById(id);
			  return new ResponseStatusException(HttpStatus.OK,"deleted expenditure successfully");  
		}
		else {
			 return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid expenditure id");
		}
	}
//	5.List all expenses by category with pagination and sort by id
	@Operation(summary="Expenditure list by category",description="list of all expenditures by category with pagination and sort by id")
	@GetMapping("/listExpensesByCategory/{catCode}/{pageNumber}/{pageSize}")
	public List<Expenditure> displayPage(@PathVariable("catCode") String catCode,@PathVariable("pageNumber") int pageNumber,@PathVariable("pageSize") int pageSize){
	    Sort sort=Sort.by("id")	;
		PageRequest pg=PageRequest.of(pageNumber,pageSize,sort);
		var page=expenditureRepo.findByCatCode(catCode,pg);
		List<Expenditure> expList=new ArrayList<>();
		for(var p:page) {
			expList.add(p);
		}
		return expList;
	}
//	6List all expenditures by payment mode with pagination and sort by id
	@Operation(summary="Expenditure list by paymentMode",description="list of all expenditures by payment mode with pagination and sort by id")
	@GetMapping("/pageExpensesByPaymentMode/{payCode}/{pageNumber}/{pageSize}")
	public Page<Expenditure> displayExpensesByPaymentMode(@PathVariable("payCode") String payCode,@PathVariable("pageNumber") int pageNumber,@PathVariable("pageSize") int pageSize){
		Pageable pageable=PageRequest.of(pageNumber,pageSize,Sort.by("id").ascending());
		return expenditureRepo.findByPayMode(payCode,pageable);		
	}
//	7List all expenses between two given dates with pagination and sorted by date in descending order
	@Operation(summary="expenditure list between dates",description="List of all expenses between two given dates with pagination and sorted by date in descending order")
	@GetMapping("/listExpensesBetweenDate/{startDate}/{endDate}/{pageNumber}/{pageSize}")
	public ResponseEntity<?> displayExpensesBetweenDates(
			@PathVariable("startDate") String startDateInput,
			@PathVariable("endDate") String endDateInput,
			@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize
			) {
		LocalDate startDate;
		LocalDate endDate;
		try {
		    startDate=LocalDate.parse(startDateInput);
		}
		catch(DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid startDate format. Please use yyyy-MM-dd.");
		}
		try {
		   endDate=LocalDate.parse(endDateInput);
		}
		catch(DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid end format. Please use yyyy-MM-dd.");
		}
		if (startDate.isAfter(endDate)) {
			return ResponseEntity.badRequest().body("Start date cannot be after end date.");
		}
		var page=expenditureRepo.findAll(PageRequest.of(pageNumber, pageSize,Sort.by("date").descending()));
		List<Expenditure> explist=new ArrayList<>();
		for(var p:page) {
			explist.add(p);
		}
		return ResponseEntity.ok(explist);		
	}
//	8.List all expenses summary(total amount) for each category in a given month)
	@Operation(summary="expenditures summary for each category in a given month")
	@GetMapping("/expensessummary/{month}")
	public ResponseEntity<?> getSummary(@PathVariable("month") Integer month) {

		if (month < 1 || month > 12) {
			return ResponseEntity.badRequest().body("Month should be between 1 and 12.");
		}
		var result = expenditureRepo.summary(month);
		if (result.isEmpty()) {
			return ResponseEntity.ok("no data found in that month");
		} else {
			return ResponseEntity.ok(result);
		}
	}
//	9.List expenses of a department between dates
	@Operation(summary="expenditure list of a department between dates")
	@GetMapping("/deptExpensesBetweenDates/{deptCode}/{startDate}/{endDate}")
	public ResponseEntity<?> listExpensesOfDepartmentBetweenDates(
			@PathVariable("deptCode") String deptCode,
			@PathVariable("startDate") String startDateString,
			@PathVariable("endDate") String endDateString
			){
		
		if(expenditureRepo.findByDeptCode(deptCode).isEmpty()) {
			return ResponseEntity.ok("no department present in departments tables with that department name");
		}
		LocalDate startDate;
		LocalDate endDate;
		try {
		startDate=LocalDate.parse(startDateString);
		}
		catch(DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid startDate format. Please use yyyy-MM-dd");
		}
		try {
		endDate=LocalDate.parse(endDateString);
		}
		catch(DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid  end date format. Please use yyyy-MM-dd");
		}
		var list=expenditureRepo.findExpenditureOfADepartmentBetweenTwoDates(deptCode,startDate,endDate);
		List<Expenditure> explist=new ArrayList<>();
        for(var p:list) {
        	explist.add(p);
        	}
        return ResponseEntity.ok(explist);
		
		}	
//	10.List all expenses authorizedBy an employee
	@Operation(summary="expenditure list authorizedBy employee")
	@GetMapping("/getExpendituresAuthorizedBy/{authorizedBy}")
	public ResponseEntity<?> displayExpensesAuthorizedBy(@PathVariable("authorizedBy") String name	){
		Optional<Expenditure> op=expenditureRepo.findByAuthorizedBy(name);
		if(!op.isPresent()) {
			return ResponseEntity.badRequest().body("invalid employee name  from database");
		}
		
		if (name == null || name.isBlank()) {
			return ResponseEntity.badRequest().body("Name parameter is required.");
		} else {
			List<Expenditure> expenditures = expenditureRepo.findAllByauthorizedBy(name);
			
			return ResponseEntity.ok(expenditures);
		}
	}
//	11.Lit all expenses where description contains given string
	@Operation(summary="Expenditure list where description contains given string")
	@GetMapping("/getExpenditureByDescript/{expDescript}")
	public ResponseEntity<?> getExpenditureByDescription(@PathVariable("expDescript") String name){
		if(name==null || name.isEmpty())
		{
			return ResponseEntity.badRequest().body("name parameter is required");
		}
		else
		{
			List<Expenditure> expenditures=expenditureRepo.findAllByExpDescriptContaining(name);
			return ResponseEntity.ok(expenditures);
		}
	}
//	12.List all expenses where amount is between given min and max values
	@Operation(summary="Expenditure list where amount between min and max values")
	@GetMapping("/listExpBetweenRange/{min}/{max}")
	public ResponseEntity<?> getExpenditureBetweenAmountRange(@PathVariable("min") Double min,@PathVariable("max") Double max){
		if(min<max && min>0 &&  max>0) {
			List<Expenditure> expenditures=expenditureRepo.findAllByExpAmountBetween(min,max);
			return ResponseEntity.ok(expenditures);
		}
		else {
			return ResponseEntity.badRequest().body("enter valid min value and max value");
		}
	}
	
}	