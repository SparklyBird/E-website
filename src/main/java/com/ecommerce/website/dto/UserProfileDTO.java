package com.ecommerce.website.dto;

import com.ecommerce.website.model.user.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public UserProfileDTO(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.firstName = userProfile.getFirstName();
        this.lastName = userProfile.getLastName();
        this.email = userProfile.getEmail();
        this.phoneNumber = userProfile.getPhoneNumber();
    }

}
