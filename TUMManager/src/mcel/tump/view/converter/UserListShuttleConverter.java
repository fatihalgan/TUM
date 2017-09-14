package mcel.tump.view.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import mcel.tump.security.domain.User;

public class UserListShuttleConverter implements Converter {

	private List<User> users = new ArrayList<User>();
	
	public UserListShuttleConverter(List<User> unselectedUsers, List<User> selectedUsers) {
		super();
		users.addAll(unselectedUsers);
		users.addAll(selectedUsers);
	}

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		StringTokenizer tok = new StringTokenizer(value, ":");
		User user = new User();
		user.setId(Long.parseLong(tok.nextToken()));
		user.setUsername(tok.nextToken());
		user.setEmail(tok.nextToken());
		for(User item : this.users) {
			if(item.equals(user)) return item;
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object object) {
		User user = (User)object;
		StringBuffer buf = new StringBuffer();
		buf.append(user.getId() + ":");
		buf.append(user.getUsername() + ":");
		buf.append(user.getEmail());
		return buf.toString();
	}
}
