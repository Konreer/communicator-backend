package com.app.communicator.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String text;
    private LocalDateTime date;
    private Long messageId;
    private Long onwerId;
    private Long conversationId;
}
