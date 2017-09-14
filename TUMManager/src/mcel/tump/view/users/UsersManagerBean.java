package mcel.tump.view.users;

import java.util.ArrayList;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.view.BaseManagedBean;
import mcel.tump.view.converter.RoleListShuttleConverter;

public class UsersManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(UsersManagerBean.class);
	
	private DataModel usersListModel;
	private ISecurityService securityService;
	private IDealerService dealerService;
	private User selectedUser = null;
	private User selectedChangePasswordUser = null;
	private User selectedChangeShopUser = null;
	private List<SelectItem> dealerShopOptions = new ArrayList<SelectItem>();
	private Long selectedDealerShop = null;
	private List<Role> selectedRoles;
	private List<Role> unselectedRoles;
	private boolean assignShopSupervisor = false;
	
	public DataModel getUsersListModel() {
		return usersListModel;
	}
	public void setUsersListModel(DataModel usersListModel) {
		this.usersListModel = usersListModel;
	}
	public ISecurityService getSecurityService() {
		return securityService;
	}
	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
	public IDealerService getDealerService() {
		return dealerService;
	}
	public void setDealerService(IDealerService dealerService) {
		this.dealerService = dealerService;
	}
	public User getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	public List<Role> getSelectedRoles() {
		return selectedRoles;
	}
	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
	public List<Role> getUnselectedRoles() {
		return unselectedRoles;
	}
	public void setUnselectedRoles(List<Role> unselectedRoles) {
		this.unselectedRoles = unselectedRoles;
	}
	
	public User getSelectedChangePasswordUser() {
		return selectedChangePasswordUser;
	}
	public void setSelectedChangePasswordUser(User selectedChangePasswordUser) {
		this.selectedChangePasswordUser = selectedChangePasswordUser;
	}
	
	public User getSelectedChangeShopUser() {
		return selectedChangeShopUser;
	}
	public void setSelectedChangeShopUser(User selectedChangeShopUser) {
		this.selectedChangeShopUser = selectedChangeShopUser;
	}
	
	public Long getSelectedDealerShop() {
		return selectedDealerShop;
	}
	public void setSelectedDealerShop(Long selectedDealerShop) {
		this.selectedDealerShop = selectedDealerShop;
	}
	public List<SelectItem> getDealerShopOptions() {
		return dealerShopOptions;
	}
	
	public boolean isAssignShopSupervisor() {
		return assignShopSupervisor;
	}
	public void setAssignShopSupervisor(boolean assignShopSupervisor) {
		this.assignShopSupervisor = assignShopSupervisor;
	}
	public Converter getRolesListShuttleConverter() {
		return new RoleListShuttleConverter(unselectedRoles, selectedRoles);
	}
	
	public String getAllUsers() {
		try {
			List<User> userList = getSecurityService().getAllUsers();
			usersListModel = new ListDataModel(userList);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get user list: " + e.getMessage());
		}
		return "listUsers";
	}
	
	public String selectUser() {
		try {
			selectedUser = (User)usersListModel.getRowData();
			getSecurityService().attach(selectedUser);
			selectedRoles = new ArrayList<Role>(selectedUser.getUserRoles());
			unselectedRoles = getSecurityService().getAllRoles();
			unselectedRoles.removeAll(selectedRoles);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedUser = null;
			selectedRoles = null;
			unselectedRoles = null;
		}
		return null;
	}
	
	public String selectChangePassword() {
		selectedChangePasswordUser = (User)usersListModel.getRowData();
		return null;
	}
	
	private void loadDealerShopOpotions() {
		List<AbstractDealer> dealers = getDealerService().getAllDealers();
		for(AbstractDealer d : dealers) {
			dealerShopOptions.add(new SelectItem(d.getId(), d.getDealerCode() + " " + d.getDealerName()));
		}
	}
	
	public String selectChangeShop() {
		try {
			selectedChangeShopUser = (User)usersListModel.getRowData();
			getSecurityService().attach(selectedChangeShopUser);
			loadDealerShopOpotions();
			if(selectedChangeShopUser.getOwningDealer() != null) selectedDealerShop = selectedChangeShopUser.getOwningDealer().getId();
			else selectedDealerShop = null;
			setAssignShopSupervisor(selectedChangeShopUser.isShopSupervisor());
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			cancelChangeShop();
		}
		return null;
	}
	
	public String newUser() {
		selectedUser = new User();
		selectedRoles = new ArrayList<Role>();
		try {
			unselectedRoles = getSecurityService().getAllRoles();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedUser = null;
			selectedRoles = null;
			unselectedRoles = null;
		}
		return null;
	}
	
	public String saveUser() {
		try {
			getSecurityService().saveUser(selectedUser, selectedRoles);
			addSuccessMessage("User has been saved succssfully: " + selectedUser.getUsername());
			selectedUser = null;
			selectedRoles = null;
			unselectedRoles = null;
			getAllUsers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listUsers";
	}
	
	public String saveUserForShop() {
		try {
			getSecurityService().createUserForDealer(selectedDealerShop, selectedUser);
			addSuccessMessage("User has been created successfully: " + selectedUser.getUsername());
			selectedUser = null;
			selectedRoles = null;
			unselectedRoles = null;
			selectedDealerShop = null;
			getAllUsers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listUsers";
	}
	
	public String changePassword() {
		try {
			getSecurityService().resetUserPassword(selectedChangePasswordUser);
			addSuccessMessage("Password has been changed succssfully: " + selectedChangePasswordUser.getUsername());
			selectedChangePasswordUser = null;
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listUsers";
	}
	
	public String changeShop() {
		try {
			getSecurityService().changeUsersShop(selectedChangeShopUser, selectedDealerShop);
			if(isAssignShopSupervisor())getSecurityService().assignAsShopSupervisor(selectedChangeShopUser);
			addSuccessMessage("User's assigned shop has been changed successfully..");
			selectedChangeShopUser = null;
			selectedDealerShop = null;
			dealerShopOptions = new ArrayList<SelectItem>();
			setAssignShopSupervisor(false);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listUsers";
	}
	
	public String deleteUser() {
		try {
			User userToDelete = (User)usersListModel.getRowData();
			getSecurityService().deleteUser(userToDelete);
			addSuccessMessage("User has been deleted successfully: " + userToDelete.getUsername());
			getAllUsers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return null;
	}
	
	public String cancelEdit() {
		selectedUser = null;
		return "listUsers";
	}
	
	public String cancelChangePassword() {
		selectedChangePasswordUser = null;
		return "listUsers";
	}
	
	public String cancelChangeShop() {
		selectedChangeShopUser = null;
		selectedDealerShop = null;
		dealerShopOptions = new ArrayList<SelectItem>();
		setAssignShopSupervisor(false);
		return "listUsers";
	}
	
	public String selectCreateUserForShop() {
		newUser();
		loadDealerShopOpotions();
		return "createShopUser";
	}
	
}
