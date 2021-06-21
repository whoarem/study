package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.User;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Header<UserApiResponse> create(Header<UserApiRequest> request) {
    UserApiRequest userApiRequest = request.getData();

    User user = User.builder()
        .account(userApiRequest.getAccount())
        .password(userApiRequest.getPassword())
        .status("REGISTERED")
        .phoneNumber(userApiRequest.getPhoneNumber())
        .email(userApiRequest.getEmail())
        .registeredAt(LocalDateTime.now())
        .build();

    User newUser = userRepository.save(user);

    return response(newUser);
  }

  @Override
  public Header<UserApiResponse> read(Long id) {
    return userRepository.findById(id)
        .map(user -> response(user))
        .orElseGet(
            () -> Header.ERROR("데이터 없음")
        );
  }

  @Override
  public Header<UserApiResponse> update(Header<UserApiRequest> request) {
    UserApiRequest userApiRequest = request.getData();

    Optional<User> optional = userRepository.findById(userApiRequest.getId());

    return optional.map(user -> {
      user.setAccount(userApiRequest.getAccount())
          .setPassword(userApiRequest.getPassword())
          .setPhoneNumber(userApiRequest.getPhoneNumber())
          .setEmail(userApiRequest.getEmail())
          .setRegisteredAt(userApiRequest.getRegisteredAt())
          .setUnregisteredAt(userApiRequest.getUnregisteredAt());
      return user;
    })
    .map(user -> userRepository.save(user))
    .map(user -> response(user))
    .orElseGet(() -> Header.ERROR("데이터 없음"));

  }

  @Override
  public Header delete(Long id) {
    return null;
  }

  private Header<UserApiResponse> response(User user) {
    UserApiResponse userApiResponse = UserApiResponse.builder()
        .id(user.getId())
        .account(user.getAccount())
        .password(user.getPassword())
        .email(user.getEmail())
        .status(user.getStatus())
        .registeredAt(user.getRegisteredAt())
        .unregisteredAt(user.getUnregisteredAt())
        .build();

    return Header.OK(userApiResponse);
  }
}
