package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.friend.FriendResponseDto;
import com.mmos.mmos.src.domain.entity.Friend;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.FriendRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElse(null);
    }

    public Friend findFriendByUserIdxAndFriendIdx(Long userIdx, Long friendIdx) {
        return friendRepository.findFriendByUser_UserIndexAndFriendUserIndex(userIdx, friendIdx)
                .orElse(null);
    }

    @Transactional
    public FriendResponseDto requestFriend(Long userIdx1, Long userIdx2) {
        // 친추하려는 유저가 존재하는지 확인
        User receiveUser = findUserByIdx(userIdx2);
        if (receiveUser == null)
            return new FriendResponseDto(INVALID_USER);

        // 이미 친추 돼있는지 확인
        User sendUser = findUserByIdx(userIdx1);
        if (friendRepository.findFriendByUser_UserIndexAndFriendUserIndex(userIdx1, userIdx2).isPresent())
            return new FriendResponseDto(FRIEND_COMPLETE_REQUEST);

        Friend sendFriend = new Friend(2, receiveUser.getUserIndex(), sendUser);
        Friend receiveFriend = new Friend(3, sendUser.getUserIndex(), receiveUser);

        friendRepository.save(sendFriend);
        friendRepository.save(receiveFriend);
        sendUser.addFriend(receiveFriend);
        receiveUser.addFriend(sendFriend);

        return new FriendResponseDto(receiveFriend, SUCCESS);
    }

    @Transactional
    public FriendResponseDto acceptRequest(Long userIdx2, Long userIdx1) {
        Friend receiveFriend = findFriendByUserIdxAndFriendIdx(userIdx2, userIdx1);
        Friend sendFriend = findFriendByUserIdxAndFriendIdx(userIdx1, userIdx2);

        if (receiveFriend.getFriendStatus() == 1 || sendFriend.getFriendStatus() == 1)
            return new FriendResponseDto(FRIEND_COMPLETE_REQUEST);

        receiveFriend.updateStatus(1);
        sendFriend.updateStatus(1);

        return new FriendResponseDto(receiveFriend, SUCCESS);
    }

    @Transactional
    public Long rejectRequest(Long userIdx2, Long userIdx1) {
        Friend receiveFriend = findFriendByUserIdxAndFriendIdx(userIdx2, userIdx1);
        Friend sendFriend = findFriendByUserIdxAndFriendIdx(userIdx1, userIdx2);

        if (receiveFriend == null || sendFriend == null)
            return null;

        friendRepository.delete(receiveFriend);
        friendRepository.delete(sendFriend);

        return receiveFriend.getFriendIndex();
    }

    @Transactional
    public FriendResponseDto updateFixedFriend(Long userIdx1, Long userIdx2) {
        Friend friend = findFriendByUserIdxAndFriendIdx(userIdx1, userIdx2);

        if (!friend.getFriendIsFixed())
            friend.updateIsFixedToTrue();
        else
            friend.updateIsFixedToFalse();

        return new FriendResponseDto(friend, SUCCESS);
    }

    @Transactional
    public Page<FriendResponseDto> getFriends(Long userIdx, Integer friendStatus, Pageable pageable) {
        List<Friend> friendList = friendRepository.findFriendsByUser_UserIndexAndFriendStatus(userIdx, friendStatus);
        List<FriendResponseDto> friendResponseDtoList = new ArrayList<>();
        if (friendStatus == 1) {
            // 고정 친구 먼저
            for (Friend friend : friendList) {
                if(friend.getFriendIsFixed()) {
                    User user = findUserByIdx(friend.getFriendUserIndex());
                    friendResponseDtoList.add(new FriendResponseDto(friend, user));
                }
            }
            // 그 다음 고정 친구 아닌 유저
            for (Friend friend : friendList) {
                if(!friend.getFriendIsFixed()) {
                    User user = findUserByIdx(friend.getFriendUserIndex());
                    friendResponseDtoList.add(new FriendResponseDto(friend, user));
                }
            }
        }
        else {
            for (Friend friend : friendList) {
                User user = findUserByIdx(friend.getFriendUserIndex());
                friendResponseDtoList.add(new FriendResponseDto(friend, user));
            }
        }

        return new PageImpl<>(friendResponseDtoList, pageable, friendResponseDtoList.size());
    }
}
