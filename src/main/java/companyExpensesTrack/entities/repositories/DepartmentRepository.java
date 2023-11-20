package companyExpensesTrack.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import companyExpensesTrack.entities.Department;
import companyExpensesTrack.entities.repositories.controllers.Dto.DepartmentDto;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,String>{

	
	@Query("select e.department.deptName as deptName,e.deptCode as deptCode,sum(e.expAmount) as totalAmount from Expenditure e group by e.deptCode,e.department.deptName")
	List<DepartmentDto> groupByDeptCode();

}
