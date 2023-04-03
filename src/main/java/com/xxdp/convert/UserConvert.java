package com.xxdp.convert;

import com.xxdp.dto.UserDTO;
import com.xxdp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserDTO convert(User user);
}
