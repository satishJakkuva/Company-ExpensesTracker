package companyExpensesTrack.entities;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="expenditures")
public class Expenditure {
	    @Id
	    @Column(name="Id")
	    @GeneratedValue (strategy = GenerationType.IDENTITY)
    	private Integer id;

    	@Column(name="categorycode")
        private String catCode;
    	
    	@Column(name="departmentcode")
        private String deptCode;
    	
    	@Column(name="Amount")
    	private Double expAmount;
    	
    	@Column(name="ExpenditureDate")
    	private LocalDate date;
    	
    	@Column(name="AuthorizedBy")
    	private String authorizedBy;
    	
    	@Column(name="ExpenditureDescription")
    	private String expDescript;
    	
    	@Column(name="paymentmode")
    	private String payMode;
    	
    	@Column(name="Remarks")
    	private String expRemarks;
    	
    	@ManyToOne
    	@JsonIgnore
    	@JoinColumn (name = "CategoryCode", insertable = false, updatable = false)
    	Category category;
    	
    	@ManyToOne
    	@JsonIgnore
    	@JoinColumn (name = "DepartmentCode", insertable = false, updatable = false)
    	Department department;
    	
    	
    	@ManyToOne
    	@JsonIgnore
    	@JoinColumn (name = "PaymentMode", insertable = false, updatable = false)
    	PaymentMode paymentMode1;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getCatCode() {
			return catCode;
		}

		public void setCatCode(String catCode) {
			this.catCode = catCode;
		}

		public String getDeptCode() {
			return deptCode;
		}

		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}

		public Double getExpAmount() {
			return expAmount;
		}

		public void setExpAmount(Double expAmount) {
			this.expAmount = expAmount;
		}

		
		public LocalDate getDate() {
			return date;
		}

		public void setDate(LocalDate date) {
			this.date = date;
		}

		public String getAuthorizedBy() {
			return authorizedBy;
		}

		public void setAuthorizedBy(String authorizedBy) {
			this.authorizedBy = authorizedBy;
		}

		public String getExpDescript() {
			return expDescript;
		}

		public void setExpDescript(String expDescript) {
			this.expDescript = expDescript;
		}

		public String getPayMode() {
			return payMode;
		}

		public void setPayMode(String payMode) {
			this.payMode = payMode;
		}

		public String getExpRemarks() {
			return expRemarks;
		}

		public void setExpRemarks(String expRemarks) {
			this.expRemarks = expRemarks;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public Department getDepartment() {
			return department;
		}

		public void setDepartment(Department department) {
			this.department = department;
		}

		public PaymentMode getPaymentMode1() {
			return paymentMode1;
		}

		public void setPaymentMode1(PaymentMode paymentMode1) {
			this.paymentMode1 = paymentMode1;
		}
    	
		
}
