package mcel.tump.view.dealers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.IPAddress;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.view.BaseManagedBean;

public class IPAddressesManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(IPAddressesManagerBean.class);
	
	private DataModel ipAddressesListModel;
	private IDealerService dealerService;
	private IPAddress selectedIPAddress;
	private List<SelectItem> owningDealerOptions = new ArrayList<SelectItem>();
	private Long owningDealer;
	
	public DataModel getIpAddressesListModel() {
		return ipAddressesListModel;
	}
	public void setIpAddressesListModel(DataModel ipAddressesListModel) {
		this.ipAddressesListModel = ipAddressesListModel;
	}
	public IDealerService getDealerService() {
		return dealerService;
	}
	public void setDealerService(IDealerService dealerService) {
		this.dealerService = dealerService;
	}
	
	public IPAddress getSelectedIPAddress() {
		return selectedIPAddress;
	}
	public void setSelectedIPAddress(IPAddress selectedIPAddress) {
		this.selectedIPAddress = selectedIPAddress;
	}
	
	public List<SelectItem> getOwningDealerOptions() {
		return owningDealerOptions;
	}
	public void setOwningDealerOptions(List<SelectItem> owningDealerOptions) {
		this.owningDealerOptions = owningDealerOptions;
	}
	public Long getOwningDealer() {
		return owningDealer;
	}
	public void setOwningDealer(Long owningDealer) {
		this.owningDealer = owningDealer;
	}
	
	private void loadOwningDealerOptions() {
		owningDealerOptions = new ArrayList<SelectItem>();
		List<AbstractDealer> dealersList = getDealerService().getAllDealers();
		owningDealerOptions.add(new SelectItem(0, " "));
		for(AbstractDealer d : dealersList) {
			owningDealerOptions.add(new SelectItem(d.getId(), d.getDealerCode() + " " + d.getDealerName()));
		}
	}
	
	public String getAllIPAddresses() {
		try {
			List<IPAddress> addressList = getDealerService().getAllIPAddresses();
			ipAddressesListModel = new ListDataModel(addressList);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get IP Address list: " + e.getMessage());
		}
		return "listIPAddresses";
	}
	
	public String selectIPAddress() {
		try {
			loadOwningDealerOptions();
			selectedIPAddress = (IPAddress)ipAddressesListModel.getRowData();
			owningDealer = selectedIPAddress.getOwningShop().getId();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with the operation");
		}
		return null;
	}
	
	public String deleteIPAddress() {
		try {
			selectedIPAddress = (IPAddress)ipAddressesListModel.getRowData();
			getDealerService().deleteIPAddress(selectedIPAddress);
			addSuccessMessage("IP Address deleted successfully: " + selectedIPAddress.getIpAddress());
			selectedIPAddress = null;
			getAllIPAddresses();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Could not delete the IP Address: " + e.getMessage());
		}
		return null;
	}
	
	public String saveIPAddress() {
		try {
			getDealerService().saveIPAddress(selectedIPAddress, owningDealer);
			addSuccessMessage("IP Address saved successfully: " + selectedIPAddress.getIpAddress());
			getAllIPAddresses();
			selectedIPAddress = null;
			owningDealer = null;
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Could not save the IP Address: " + e.getMessage());
			return null;
		}
		return null;
	}
	
	public String newIPAddress() {
		loadOwningDealerOptions();
		selectedIPAddress = new IPAddress();
		owningDealer = null;
		return null;
	}
	
	public String cancelEdit() {
		selectedIPAddress = null;
		owningDealer = null;
		return null;
	}
	
}
