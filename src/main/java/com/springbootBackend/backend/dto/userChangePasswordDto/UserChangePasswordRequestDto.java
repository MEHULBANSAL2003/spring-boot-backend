package com.springbootBackend.backend.dto.userChangePasswordDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserChangePasswordRequestDto {

  @NotBlank(message = "provide the old password")
  private String oldPassword;

  @NotBlank(message = "new Password is required")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
  private String newPassword;
}
