package mcel.tump.view.dealers;

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.reference.domain.BlacklistItem;
import mcel.tump.reference.service.IParametersService;
import mcel.tump.view.BaseManagedBean;

public class BlacklistManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(BlacklistManagerBean.class);
	
	private DataModel blacklistedDealersModel;
	private IParametersService parametersService;

	public DataModel getBlacklistedDealersModel() {
		return blacklistedDealersModel;
	}

	public void setBlacklistedDealersModel(DataModel blacklistedDealersModel) {
		this.blacklistedDealersModel = blacklistedDealersModel;
	}

	public IParametersService getParametersService() {
		return parametersService;
	}

	public void setParametersService(IParametersService parametersService) {
		this.parametersService = parametersService;
	}
	
	public String getAllBlacklistedItems() {
		try {
			List<BlacklistItem> blacklist = getParametersService().getBlacklist();
			blacklistedDealersModel = new ListDataModel(blacklist);
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get Blacklisted items: " + e.getMessage());
		}
		return "listBlacklistedItems";
	}
	
	
}
