package com.moddy.server.controller.user.dto.response;

import com.moddy.server.domain.user.Role;

public record UserDetailResponseDto(Long id, String profileImgUrl, String name, Role role) {
}
