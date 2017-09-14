package mcel.tump.view.users;

import java.util.ArrayList;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.view.BaseManagedBean;
import mcel.tump.view.converter.UserListShuttleConverter;

public class RolesManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(RolesManagerBean.class);
	
	private DataModel rolesListModel;
	private ISecurityService securityService;
	private Role selectedRole = null;
	private List<User> selectedUsers;
	private List<User> unselectedUsers;

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}

	public DataModel getRolesListModel() {
		return rolesListModel;
	}

	public void setRolesListModel(DataModel rolesListModel) {
		this.rolesListModel = rolesListModel;
	}
	
	public Role getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
	
	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<User> getUnselectedUsers() {
		return unselectedUsers;
	}

	public void setUnselectedUsers(List<User> unselectedUsers) {
		this.unselectedUsers = unselectedUsers;
	}
	
	public Converter getUserListShuttleConverter() {
		return new UserListShuttleConverter(unselectedUsers, selectedUsers);
	}

	public String getAllRoles() {
		try {
			List<Role> rolesList = getSecurityService().getAllRoles();
			rolesListModel = new ListDataModel(rolesList);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get role list: " + e.getMessage());
		}
		return "listRoles";
	}
	
	public String selectRole() {
		try {
			selectedRole = (Role)rolesListModel.getRowData();
			getSecurityService().attach(selectedRole);
			selectedUsers = new ArrayList<User>(selectedRole.getUsers());
			unselectedUsers = getSecurityService().getAllUsers();
			unselectedUsers.removeAll(selectedUsers);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedRole = null;
			selectedUsers = null;
			unselectedUsers = null;
		}
		return null;
	}
	
	public String newRole() {
		selectedRole = new Role();
		selectedUsers = new ArrayList<User>();
		try {
			unselectedUsers = getSecurityService().getAllUsers();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedRole = null;
			selectedUsers = null;
			unselectedUsers = null;
		}
		return null;
	}
	
	public String saveRole() {
		try {
			getSecurityService().saveRole(selectedRole, selectedUsers);
			addSuccessMessage("Role has been saved succssfully: " + selectedRole.getRoleName());
			selectedRole = null;
			selectedUsers = null;
			unselectedUsers = null;
			getAllRoles();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listRoles";
	}
	
	public String deleteRole() {
		try {
			Role roleToDelete = (Role)rolesListModel.getRowData();
			getSecurityService().deleteRole(roleToDelete);
			addSuccessMessage("Role has been deleted successfully: " + roleToDelete.getRoleName());
			getAllRoles();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return null;
	}
	
	public String cancelEdit() {
		selectedRole = null;
		return "listRoles";
	}
	
}
