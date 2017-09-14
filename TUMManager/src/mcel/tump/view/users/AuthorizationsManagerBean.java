package mcel.tump.view.users;

import java.util.ArrayList;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.view.BaseManagedBean;
import mcel.tump.view.converter.RoleListShuttleConverter;

public class AuthorizationsManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(AuthorizationsManagerBean.class);
	
	private DataModel authListModel;
	private ISecurityService securityService;
	private Authorization selectedAuthorization;
	private List<Role> selectedRoles;
	private List<Role> unselectedRoles;
	
	public DataModel getAuthListModel() {
		return authListModel;
	}
	public void setAuthListModel(DataModel authListModel) {
		this.authListModel = authListModel;
	}
	public ISecurityService getSecurityService() {
		return securityService;
	}
	
	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	public Authorization getSelectedAuthorization() {
		return selectedAuthorization;
	}
	public void setSelectedAuthorization(Authorization selectedAuthorization) {
		this.selectedAuthorization = selectedAuthorization;
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
	
	public Converter getRolesListShuttleConverter() {
		return new RoleListShuttleConverter(unselectedRoles, selectedRoles);
	}
	
	public String getAllAuthorizations() {
		try {
			List<Authorization> authorizationsList = getSecurityService().getAllAuthorizations();
			authListModel = new ListDataModel(authorizationsList);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get authorization list: " + e.getMessage());
		}
		return "listAuthorizations";
	}
	
	public String selectAuthorization() {
		try {
			selectedAuthorization = (Authorization)authListModel.getRowData();
			getSecurityService().attach(selectedAuthorization);
			selectedRoles = new ArrayList<Role>(selectedAuthorization.getAuthorizedRoles());
			unselectedRoles = getSecurityService().getAllRoles();
			unselectedRoles.removeAll(selectedRoles);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedAuthorization = null;
			selectedRoles = null;
			unselectedRoles = null;
		}
		return null;
	}
	
	public String newAuthorization() {
		selectedAuthorization = new Authorization();
		selectedRoles = new ArrayList<Role>();
		try {
			unselectedRoles = getSecurityService().getAllRoles();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot proceed with operation: " + e.getMessage());
			selectedAuthorization = null;
			selectedRoles = null;
			unselectedRoles = null;
		}
		return null;
	}
	
	public String saveAuthorization() {
		try {
			getSecurityService().saveAuthorization(selectedAuthorization, selectedRoles);
			addSuccessMessage("Authorization has been saved succssfully: " + selectedAuthorization.getAuthorizationName());
			selectedAuthorization = null;
			selectedRoles = null;
			unselectedRoles = null;
			getAllAuthorizations();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return "listAuthorizations";
	}
	
	public String deleteAuthorization() {
		try {
			Authorization authToDelete = (Authorization)authListModel.getRowData();
			getSecurityService().deleteAuthorization(authToDelete);
			addSuccessMessage("Authorization has been deleted successfully: " + authToDelete.getAuthorizationName());
			getAllAuthorizations();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error: " + e.getMessage());
		}
		return null;
	}
	
	public String cancelEdit() {
		selectedAuthorization = null;
		return "listAuthorizations";
	}
	
}
