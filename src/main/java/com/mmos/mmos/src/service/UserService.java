package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.dto.request.SignUpRequestDto;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MajorService majorService;

    public User findUserByIdx(Long userIdx) throws BaseException {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    @Transactional
    public User getUser(Long userIdx) throws BaseException {
        try {
            return findUserByIdx(userIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(EMPTY_USER);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public User saveUser(SignUpRequestDto requestDto) throws BaseException {
        try {
            if(userRepository.findUserByUserEmail(requestDto.getEmail()).isPresent() ||
                    userRepository.findUserByUserNickname(requestDto.getNickname()).isPresent() ||
                    userRepository.findUserByUserId(requestDto.getId()).isPresent())
                throw new DuplicateRequestException(USER_DUPLICATE_SAVE);

            Major major = majorService.getMajor(requestDto.getMajorIdx());
            User user = new User(requestDto, major);
            major.addUser(user);

            return userRepository.save(user);
        } catch (DuplicateRequestException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
