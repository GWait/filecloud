package ru.geekbrains.filecloud.payload;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class AuthenticationPayload implements Serializable {
    private final String login;
    private final String password;
    private String token;
}
