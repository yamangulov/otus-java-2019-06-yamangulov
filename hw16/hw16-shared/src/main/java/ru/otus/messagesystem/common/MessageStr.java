package ru.otus.messagesystem.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.api.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageStr {
    @JsonProperty("messageStr")
    private User userFromMessage;
}
