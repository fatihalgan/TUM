package mcel.tump.view.parameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.reference.domain.Parameters;
import mcel.tump.reference.service.IParametersService;
import mcel.tump.view.BaseManagedBean;

public class ParametersManagerBean extends BaseManagedBean {

	private static final Log logger = LogFactory.getLog(ParametersManagerBean.class);
	
	private Parameters parameters;
	private IParametersService parametersService;
	
	public IParametersService getParametersService() {
		return parametersService;
	}

	public void setParametersService(IParametersService parametersService) {
		this.parametersService = parametersService;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	public String loadParameters() {
		try {
			parameters  = getParametersService().getParameters();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Cannot get parameters: " + e.getMessage());
		}
		return "listParameters";
	}
	
	public String saveParameters() {
		try {
			getParametersService().saveParameters(parameters);
			addSuccessMessage("Parameters have been saved successfully.");
			loadParameters();
		} catch(Exception e) {
			logger.error(e);
			addErrorMessage("Error in saving parameters: " + e.getMessage());
		}
		return null;
	}
}
