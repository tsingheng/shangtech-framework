package net.shangtech.framework.web.validation;

import net.shangtech.framework.web.AjaxResponse;

public class RequestInvalidResponse extends AjaxResponse {

    private static final long serialVersionUID = 815400474712551239L;

    private Boolean hasErrors = false;

	public Boolean getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(Boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
}
