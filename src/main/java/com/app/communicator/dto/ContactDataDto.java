package com.app.communicator.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDataDto {
    private String contactName;
    private String contactSurname;
    private Long contactId;
    private String avatarUrl;
}
