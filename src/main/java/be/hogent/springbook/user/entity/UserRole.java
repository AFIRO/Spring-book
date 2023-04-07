package be.hogent.springbook.user.entity;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum UserRole {
    @FieldNameConstants.Include ADMIN,
    @FieldNameConstants.Include USER
}
