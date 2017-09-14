package mcel.tump.util.persistence;

import mcel.tump.security.domain.UserStatus;

public class UserStatusUserType extends EnumUserType<UserStatus> {

    public UserStatusUserType() {
        super(UserStatus.class);
    }
}
