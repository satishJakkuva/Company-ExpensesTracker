package companyExpensesTrack.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import companyExpensesTrack.entities.Category;
import companyExpensesTrack.entities.repositories.controllers.Dto.CategoryDto;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String>  {
    
	@Query("select e.catCode as catCode,e.category.catName as catName,sum(e.expAmount) as totalAmount from Expenditure e group by e.catCode,e.category.catName ")
	List<CategoryDto> groupByCategory();
	
}
