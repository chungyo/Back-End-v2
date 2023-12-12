package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.dto.request.SignUpRequestDto;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.FriendRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mmos.mmos.config.HttpResponseStatus.*;
import static com.mmos.mmos.utils.SHA256.encrypt;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MajorService majorService;
    private final FriendRepository friendRepository;

    public User findUserByIdx(Long userIdx) throws BaseException {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public User findUserById(String id) throws BaseException {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public User findUserByIdAndPwd(Long idx, String pwd) throws BaseException {
        return userRepository.findUserByUserIndexAndUserPassword(idx, pwd)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public boolean isExistById(String id) {
        return userRepository.existsUserByUserId(id);
    }

    @Transactional
    public User getUser(Long userIdx) throws BaseException {
        try {
            return findUserByIdx(userIdx);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public User saveUser(SignUpRequestDto requestDto) throws BaseException {
        try {
            if(userRepository.findUserByUserEmail(requestDto.getEmail()).isPresent() ||
                    userRepository.findUserByUserId(requestDto.getId()).isPresent())
                throw new DuplicateRequestException(USER_DUPLICATE_SAVE);

            Major major = majorService.getMajor(requestDto.getMajorIdx());
            requestDto.encryptPwd(encrypt(requestDto.getPwd()));

            User user = new User(requestDto, major);
            major.addUser(user);

            return userRepository.save(user);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateId(User user, String id) throws BaseException {
        try {
            user.updateId(id);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateName(User user, String name) throws BaseException {
        try {
            user.updateName(name);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updatePwd(User user, String pwd) throws BaseException {
        try {
            user.updatePwd(pwd);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateIsVisible(User user) throws BaseException {
        try {
            user.updateIsPlannerVisible(!user.getIsPlannerVisible());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteUser(User user) throws BaseException {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
