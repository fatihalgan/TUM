package mcel.tump.view.dealers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.account.service.IAccountService;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.domain.DealerTypes;
import mcel.tump.dealer.domain.EDGEDealer;
import mcel.tump.dealer.domain.ExternalDealer;
import mcel.tump.dealer.domain.PersonalDealer;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.util.value.Money;
import mcel.tump.view.BaseManagedBean;

public class DealersManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(DealersManagerBean.class);
	
	private DataModel dealersListModel;
	private IDealerService dealerService;
	private IAccountService accountService;
	private DealerTypes selectedDealerType;
	private AbstractDealer selectedDealer;
	private Long parentDealer;
	private List<SelectItem> parentDealerOptions = new ArrayList<SelectItem>();
	private boolean isEdit = false;
	
	private BigDecimal creditAmount;
	private String invoiceNo;
	private String shipmentNumber;
	private String creditingDealerCode;
	
	public DataModel getDealersListModel() {
		return dealersListModel;
	}
	public void setDealersListModel(DataModel dealersListModel) {
		this.dealersListModel = dealersListModel;
	}
	public IDealerService getDealerService() {
		return dealerService;
	}
	public void setDealerService(IDealerService dealerService) {
		this.dealerService = dealerService;
	}
	
	public IAccountService getAccountService() {
		return accountService;
	}
	
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	
	public DealerTypes getSelectedDealerType() {
		return selectedDealerType;
	}
	public void setSelectedDealerType(DealerTypes selectedDealerType) {
		this.selectedDealerType = selectedDealerType;
	}
	
	public AbstractDealer getSelectedDealer() {
		return selectedDealer;
	}
	public void setSelectedDealer(AbstractDealer selectedDealer) {
		this.selectedDealer = selectedDealer;
	}
	
	public Long getParentDealer() {
		return parentDealer;
	}
	public void setParentDealer(Long parentDealer) {
		this.parentDealer = parentDealer;
	}
	
	public List<SelectItem> getParentDealerOptions() {
		return parentDealerOptions;
	}
	
	public void setParentDealerOptions(List<SelectItem> parentDealerOptions) {
		this.parentDealerOptions = parentDealerOptions;
	}
	
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public String getShipmentNumber() {
		return shipmentNumber;
	}
	
	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}
	
	public String getCreditingDealerCode() {
		return creditingDealerCode;
	}
	public void setCreditingDealerCode(String creditingDealerCode) {
		this.creditingDealerCode = creditingDealerCode;
	}
	
	public String getAllDealers() {
		try {
			List<AbstractDealer> dealersList = getDealerService().getAllDealers();
			dealersListModel = new ListDataModel(dealersList);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get dealer list: " + e.getMessage());
		}
		return "listDealers";
	}
	
	public String newDealer() {
		try {
			loadParentDealerOptions();
			parentDealer = null;
			if(selectedDealerType.equals(DealerTypes.DealerShop)) selectedDealer = new DealerShop();
			if(selectedDealerType.equals(DealerTypes.EDGEDealer)) selectedDealer = new EDGEDealer();
			if(selectedDealerType.equals(DealerTypes.ExternalDealer)) selectedDealer = new ExternalDealer();
			if(selectedDealerType.equals(DealerTypes.PersonalDealer)) selectedDealer = new PersonalDealer();
			isEdit = false;
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with the operation");
			return null;
		}
		return "editDealer";
	}
	
	public String selectDealer() {
		try {
			loadParentDealerOptions();
			selectedDealer = (AbstractDealer)dealersListModel.getRowData();
			if(selectedDealer.getParentDealer() != null) parentDealer = selectedDealer.getParentDealer().getId();
			else parentDealer = 0l;
			selectedDealerType = selectedDealer.getDealerType();
			isEdit = true;
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with theoperation");
			return null;
		}
		return "editDealer";
	}
	
	public String selectCredit() {
		selectedDealer = (AbstractDealer)dealersListModel.getRowData();
		return "creditDealer";
	}
	
	public String saveDealer() {
		try {
			if(!isEdit) getDealerService().createDealer(selectedDealer, parentDealer);
			else getDealerService().saveDealer(selectedDealer, parentDealer);
			addSuccessMessage("Dealer successfully saved: " + selectedDealer.getDealerCode() + " " + selectedDealer.getDealerName());
			selectedDealer = null;
			parentDealer = null;
			parentDealerOptions = new ArrayList<SelectItem>();
			getAllDealers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Dealer cannot be saved: " + e.getMessage());
			return null;
		}
		return "listDealers";
	}
	
	public String deleteDealer() {
		try {
			AbstractDealer dealerToDelete = (AbstractDealer)dealersListModel.getRowData();
			getDealerService().deleteDealer(dealerToDelete);
			addSuccessMessage("Dealer has been deleted successfully: " + dealerToDelete.getDealerCode() + " " + dealerToDelete.getDealerName());
			getAllDealers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e);
		}
		return null;
	}
	
	public String creditDealer() {
		try {
			getAccountService().creditAmount(new Money(creditAmount), creditingDealerCode, invoiceNo, shipmentNumber);
			addSuccessMessage("The amount has been credited successfully to Dealer: " + selectedDealer.getDealerCode() + " " + selectedDealer.getDealerName());
			creditAmount = null;
			invoiceNo = null;
			shipmentNumber = null;
			creditingDealerCode = null;
			selectedDealer = null;
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Dealer could not be credited: " + e.getMessage());
			return null;
		}
		getAllDealers();
		return "listDealers";
	}
	
	public String cancelEdit() {
		selectedDealer = null;
		parentDealer = null;
		parentDealerOptions = new ArrayList<SelectItem>();
		isEdit = false;
		return "listDealers";
	}
	
	public String cancelCredit() {
		selectedDealer = null;
		creditAmount = null;
		invoiceNo = null;
		shipmentNumber = null;
		creditingDealerCode = null;
		selectedDealer = null;
		return "listDealers";
	}
	
	private void loadParentDealerOptions() {
		parentDealerOptions = new ArrayList<SelectItem>();
		List<AbstractDealer> dealersList = getDealerService().getAllDealers();
		parentDealerOptions.add(new SelectItem(0, " "));
		for(AbstractDealer d : dealersList) {
			parentDealerOptions.add(new SelectItem(d.getId(), d.getDealerCode() + " " + d.getDealerName()));
		}
	}
}
