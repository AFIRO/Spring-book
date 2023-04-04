package be.hogent.springbook.security.entity;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecurityUser implements UserDetails {
    private ApplicationUser applicationUser;

    public SecurityUser(ApplicationUser applicationUser) {
        System.out.println(applicationUser.getEmail());
        System.out.println(applicationUser.getPassword());
        this.applicationUser = applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(applicationUser.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return applicationUser.getPassword();
    }

    @Override
    public String getUsername() {
        return applicationUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {return applicationUser.getRole().equals(UserRole.ADMIN);}

    public String getUserId() {return applicationUser.getUserId();}

    public List<String> getFavoriteBookIds() {
        return applicationUser.getFavoriteBooks().stream().map(Book::getBookId).toList();
    }
}
