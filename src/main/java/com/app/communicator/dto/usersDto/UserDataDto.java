package com.app.communicator.dto.usersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataDto {
    private String name;
    private String surname;
    private Long id;
    private String avatarUrl;
}
