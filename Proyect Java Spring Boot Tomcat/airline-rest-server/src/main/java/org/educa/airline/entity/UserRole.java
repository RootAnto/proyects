package org.educa.airline.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements GrantedAuthority{
    private String role;
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return role;
    }
}
