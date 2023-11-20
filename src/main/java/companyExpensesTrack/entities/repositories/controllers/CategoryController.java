package companyExpensesTrack.entities.repositories.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import companyExpensesTrack.entities.Category;
import companyExpensesTrack.entities.repositories.CategoryRepository;
import companyExpensesTrack.entities.repositories.controllers.Dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class CategoryController {
	@Autowired
	private CategoryRepository categoryRepo;

    // 4
	@Operation(summary="add category",description="This function involves adding category which consists of catCode and catName ")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addCategory")
	public ResponseStatusException addCategory(@RequestBody Category cat) {
		String cat1 = cat.getCatCode();
		if (cat1.isBlank()) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid category code");
		} else {
			categoryRepo.save(cat);
			return new ResponseStatusException(HttpStatus.OK, "added category ");
		}
	}
// 13.List all categories
	@GetMapping("/getCategory")
	@Operation(
	        summary = "Get all categories",
	        description = "Retrieves a list of all categories."
	)
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Returns a list of Category objects representing all categories.")
	})
	public List<Category> getAllCategories() {
	    return categoryRepo.findAll();
	}

	
	@Operation(summary="update category",description="This function  updates category by catCode")
	@ApiResponses(value= {
			@ApiResponse(responseCode="200",description="updated data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatecategory/{catCode}")
	public ResponseStatusException updateCategoryById(@PathVariable("catCode") String catCode,
			@RequestParam("catName") String catName) {
		Optional<Category> cat = categoryRepo.findById(catCode);
		if (cat.isPresent()) {
			Category op = cat.get();
			op.setCatName(catName);
			categoryRepo.save(op);
			return new ResponseStatusException(HttpStatus.OK, "updated sucessfully");
		} else {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid catcode");
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@Operation(summary="delete category from categories",description="this function deletes category by catCode")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted data successfully"),
			@ApiResponse(responseCode = "404", description = "check url properly"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error"),
			@ApiResponse(responseCode = "401", description = "authentication failed"),
			@ApiResponse(responseCode = "403", description = "authorization failed")})
	@DeleteMapping("deleteCategory/{catCode}")
	public ResponseStatusException deleteCategoryById(  @PathVariable("catCode") String catCode) {
		Optional<Category> op = categoryRepo.findById(catCode);
		if (op.isPresent()) {
			categoryRepo.deleteById(catCode);
			return new ResponseStatusException(HttpStatus.OK, "deleted category succesfully");
		} else {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid catcode");
		}
	}
//	15.List category and total amount spent in the category
	@Operation(summary="retrieves category list and total amount spent in the category")
	@GetMapping("/getTotalAmountAndCategory/{catCode}")
	public ResponseEntity<?> getTotalAmountSpentOnCategory(@PathVariable("catCode") String catCode){
	    List<CategoryDto> result = categoryRepo.groupByCategory();
	    if (result.isEmpty()) {
	        return ResponseEntity.ok("Expenditure table is empty");
	    }
	    return ResponseEntity.ok(result);
	}
	

}
