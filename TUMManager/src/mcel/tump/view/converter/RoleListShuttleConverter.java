package mcel.tump.view.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;

public class RoleListShuttleConverter implements Converter {

	private List<Role> roles = new ArrayList<Role>();
	
	public RoleListShuttleConverter(List<Role> unselectedRoles, List<Role> selectedRoles) {
		super();
		roles.addAll(unselectedRoles);
		roles.addAll(selectedRoles);
	}

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		StringTokenizer tok = new StringTokenizer(value, ":");
		Role role = new Role();
		role.setId(Long.parseLong(tok.nextToken()));
		role.setRoleName(tok.nextToken());
		role.setDescription(tok.nextToken());
		for(Role item : this.roles) {
			if(item.equals(role)) return item;
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object object) {
		Role role = (Role)object;
		StringBuffer buf = new StringBuffer();
		buf.append(role.getId() + ":");
		buf.append(role.getRoleName() + ":");
		buf.append(role.getDescription());
		return buf.toString();
	}
}
