package be.hogent.springbook.security.entity;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private ApplicationUser applicationUser;
    private List<String> favoriteBookIds;

    public SecurityUser(ApplicationUser applicationUser) {
        System.out.println("User " + applicationUser.getEmail()+ " with role " + applicationUser.getRole().name() + " has logged in.");

        this.applicationUser = applicationUser;
        this.favoriteBookIds = applicationUser.getFavoriteBooks().stream().map(Book::getBookId).toList();
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

    public boolean isAdmin() {
        return applicationUser.getRole().equals(UserRole.ADMIN);
    }

    public String getUserId() {
        return applicationUser.getUserId();
    }

    public List<String> getFavoriteBookIds() {
        return this.favoriteBookIds;
    }

    public void setFavoriteBookIds(List<String> ids) {
        this.favoriteBookIds = ids;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }
}
