package companyExpensesTrack.entities.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import companyExpensesTrack.entities.Expenditure;
import companyExpensesTrack.entities.repositories.controllers.Dto.CategoryDto;
@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,Integer> {
    //5
    List<Expenditure> findByCatCode(String catCode, PageRequest pg);
//    6
	Page<Expenditure> findByPayMode(String payCode, Pageable pageable);
//	8
	@Query("select e.catCode as catCode,e.category.catName as catName,sum(e.expAmount)  totalAmount from Expenditure e where month(e.date)=:month group by e.catCode,e.category.catName")
	List<CategoryDto> summary(@Param("month") int month);
//	9
	List<Expenditure> findByDeptCode(String deptCode);
	
	
	@Query("select e from Expenditure e where e.deptCode=:deptCode and (e.date between :startDate and :endDate)")
	List<Expenditure> findExpenditureOfADepartmentBetweenTwoDates(@Param("deptCode") String deptCode, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//	10
	List<Expenditure> findAllByauthorizedBy(String name);
//	11
	List<Expenditure> findAllByExpDescriptContaining(String name);
//	12
	List<Expenditure> findAllByExpAmountBetween(Double min, Double max);
	Optional<Expenditure> findByAuthorizedBy(String name);

  
	
	
}
