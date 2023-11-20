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
@Table(name = "paymentModes")
public class PaymentMode {
	@Id
	@Column(name = "PaymentCode")
	private String payCode;
	
	@Column(name = "PaymentName")
	private String payName;
	
	@Column(name = "Remarks")
	private String remarks;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentMode1")
	private List<Expenditure> expenditures = new ArrayList<>();

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Expenditure> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(List<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}

	
}