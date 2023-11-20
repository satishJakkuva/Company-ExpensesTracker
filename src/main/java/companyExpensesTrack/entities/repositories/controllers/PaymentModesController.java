package companyExpensesTrack.entities.repositories.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import companyExpensesTrack.entities.PaymentMode;
import companyExpensesTrack.entities.repositories.PaymentModesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

@RestController
public class PaymentModesController {
	@Autowired
    private PaymentModesRepository paymentModesRepo;
	@Operation(summary="list of paymentMode")
	@GetMapping("/getPaymentModes")
	public List<PaymentMode> getPaymentModes(){
		return paymentModesRepo.findAll();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary="add PaymentMode",description="This function describes adding of payment mode")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PostMapping("/addPaymentModes")
	public ResponseStatusException addPaymentModes(@RequestBody PaymentMode pm) {
		String str=pm.getPayCode();
		  if(str.isBlank()) {
				 return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid PaymentModes code");
		  }
		  else {
			  paymentModesRepo.save(pm);
			 return new ResponseStatusException(HttpStatus.OK,"added PaymentModes");
		  }
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary="updates paymentMode",description="function describes updation of paymentMode ")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="updated data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PutMapping("/updatePaymentModes/{payCode}")
	public ResponseStatusException  updatePaymentModes(@PathVariable("payCode") String payCode,@RequestParam("payName") String payName,@RequestParam("remarks") String remarks) {
		Optional<PaymentMode> op=paymentModesRepo.findById(payCode);
		if(op.isPresent()) {
			PaymentMode  pm=op.get();
			pm.setPayName(payName);
			pm.setRemarks(remarks);
			paymentModesRepo.save(pm);
			 return new ResponseStatusException(HttpStatus.OK,"updated Payment modes");
		}
		else {
			 return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid PaymentModes code");
		}
	}
	
	@Operation(summary="delete paymentMode",description="function involves deletion of paymentMode ")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="deleted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@DeleteMapping("/deletePaymentModes")
	public ResponseStatusException deletePaymentModes(@PathVariable("payCode") String payCode) {
		 Optional<PaymentMode> op=paymentModesRepo.findById(payCode);
		  if(op.isPresent()) {
			  paymentModesRepo.deleteById(payCode);
			  return new ResponseStatusException(HttpStatus.OK,"deleted  successfully");  
		  }
		  else {
			  return new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid department code");
		  }
	}
}
