package companyExpensesTrack.entities.repositories.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import companyExpensesTrack.entities.Department;
import companyExpensesTrack.entities.repositories.DepartmentRepository;
import companyExpensesTrack.entities.repositories.controllers.Dto.DepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class DeptController {
	  @Autowired
      private DepartmentRepository dr;
	  
	  @Operation(summary="list of department",description="retrieves list of department")
	  @CrossOrigin 
	  @GetMapping("/getDepartment")
	  public List<Department> getDepartment(){
		 return dr.findAll();
	  }
	  
	  @Operation(summary="adding department",description="this function involves adding of department ")
	  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
				@ApiResponse(responseCode = "404", description = "check url properly"),
				@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
				@ApiResponse(responseCode = "500", description = "Internal server error"),
				@ApiResponse(responseCode = "401", description = "authentication failed"),
				@ApiResponse(responseCode = "403", description = "authorization failed")})
	  @PreAuthorize("hasRole('ADMIN')")
	  @PostMapping("/addDepartment")
	  public ResponseStatusException addDepartment( @RequestBody Department dept) {
		  String str=dept.getDeptCode();
		  if(str.isBlank()) {
				 return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid department code");
		  }
		  else {
			  dr.save(dept);
			 return new ResponseStatusException(HttpStatus.OK,"added department");
		  }
	  }
	  
	  @PreAuthorize("hasRole('ADMIN')")
	  @Operation(summary="deleting department",description="this function  involves deletion of department")
	  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "deleted data successfully"),
				@ApiResponse(responseCode = "404", description = "check url properly"),
				@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
				@ApiResponse(responseCode = "500", description = "Internal server error"),
				@ApiResponse(responseCode = "401", description = "authentication failed"),
				@ApiResponse(responseCode = "403", description = "authorization failed")})
	  @Transactional
	  @DeleteMapping("/deleteDepartment/{deptCode}")
	  public ResponseStatusException deleteDepartment( @PathVariable("deptCode") String deptCode) {
		  Optional<Department> op=dr.findById(deptCode);
		  if(op.isPresent()) {
			  dr.deleteById(deptCode);
			  return new ResponseStatusException(HttpStatus.OK,"deleted department successfully");  
		  }
		  else {
			  return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid department code");
		  }
	  }
	  
	  @PreAuthorize("hasRole('ADMIN')")
	  @Operation(summary="updates department ",description="updates department by department code")
	  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "updated data successfully"),
				@ApiResponse(responseCode = "404", description = "check url properly"),
				@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
				@ApiResponse(responseCode = "500", description = "Internal server error"),
				@ApiResponse(responseCode = "401", description = "authentication failed"),
				@ApiResponse(responseCode = "403", description = "authorization failed")})
	  @PutMapping("/updateDepartment/{deptCode}")
	  public ResponseStatusException updateDepartment
	  ( @PathVariable("deptCode") String deptCode,@RequestParam("deptName") String deptName) {
		  Optional<Department> op=dr.findById(deptCode);
		  if(op.isPresent()) {
			  Department dep=op.get();
			 dep.setDeptName(deptName);
			  dr.save(dep);
			   return new ResponseStatusException(HttpStatus.OK,"updated department sucessfully");
		  }
		  else {
				 return new ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid deptCode");
		  }
	  }
//	  14.List department and total amount spent on department
	 @Operation(summary="List of department and total amount spent on department")
	 @GetMapping("/getTotalAmountByDepartment/{deptCode}") 
	 public ResponseEntity<?> getTotalAmountByDepartment(@PathVariable("deptCode") String deptCode){
		 List<DepartmentDto> result=dr.groupByDeptCode();
		 if (result.isEmpty()) {
				return ResponseEntity.ok("expenditure table is empty");
			}
			return ResponseEntity.ok(result);
	 }
	  
	  
	  
	  
}
