package com.github.ludmylla.userapi.domain.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoleIdInput {

    @NotNull
    private Long id;
}
