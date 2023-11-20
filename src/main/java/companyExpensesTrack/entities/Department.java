package companyExpensesTrack.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {
	@Id
	@Column(name = "departmentcode")
	private String deptCode;
	
	@Column(name = "DepartmentName")
	private String deptName;
	
	@Column(name = "HOD")
	private String hod;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
	private List<Expenditure> expenditures = new ArrayList<>();

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getHod() {
		return hod;
	}

	public void setHod(String hod) {
		this.hod = hod;
	}

	public List<Expenditure> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(List<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}

	@Override
	public String toString() {
		return "Department [deptCode=" + deptCode + ", deptName=" + deptName + ", hod=" + hod + ", expenditures="
				+ expenditures + "]";
	}

	
}
