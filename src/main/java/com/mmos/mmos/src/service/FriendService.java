package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.Friend;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    public Friend findFriendByIdx(Long friendIdx) throws BaseException {
        return friendRepository.findById(friendIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_FRIEND));
    }

    public List<Friend> findFriendsByUserIndexAndFriendStatus(Long userIdx, Integer status) throws BaseException {
        return friendRepository.findFriendsByUser_UserIndexAndFriendStatus(userIdx, status)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public Friend findFriendByUserAndFriend(User user, User friend) throws BaseException {
        return friendRepository.findFriendByUserAndFriend(user, friend)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_FRIEND));
    }

    @Transactional
    public void friendWithMe(Long userIdx) throws BaseException {
        try {
            User user = userService.getUser(userIdx);
            if(user.getUserFriends().isEmpty())
                friendRepository.save(new Friend(1, user, user));

        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Friend> getFriends(Long userIdx, Integer friendStatus) throws BaseException {
        try {
            List<Friend> friendList = findFriendsByUserIndexAndFriendStatus(userIdx, friendStatus);
            List<Friend> friendListOrdered = new ArrayList<>();
            if (friendStatus == 1) {
                // 고정 친구 먼저
                for (Friend friend : friendList) {
                    if(friend.getFriendIsFixed()) {
                        friendListOrdered.add(friend);
                    }
                }
                // 그 다음 고정 친구 아닌 유저
                for (Friend friend : friendList) {
                    if(!friend.getFriendIsFixed()) {
                        friendListOrdered.add(friend);
                    }
                }
            } else {
                for (Friend friend : friendList) {
                    friendListOrdered.add(friend);
                }
            }

            return friendListOrdered;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<User> getTop3Friends(Long userIdx, int friendStatus) throws BaseException {
        try {
            List<Friend> myFriends = findFriendsByUserIndexAndFriendStatus(userIdx, friendStatus);
            List<User> top3 = new ArrayList<>();

            if(!myFriends.isEmpty()) {
                myFriends.sort(new FriendStudyTimeComparator());

                top3.add(myFriends.get(0).getFriend());
                if(myFriends.size() > 1)
                    top3.add(myFriends.get(1).getFriend());
                if(myFriends.size() > 2)
                    top3.add(myFriends.get(2).getFriend());
            }

            return top3;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Friend sendFriendRequest(Long userIdx, String friendId) throws BaseException {
        try {
            User user = userService.getUser(userIdx);
            User friend = userService.findUserById(friendId);

            if(user.equals(friend))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            Friend response;
            try {
                // 이미 친구인지 확인
                findFriendByUserAndFriend(user, friend);
                // 이미 친구라면 throw
                throw new DuplicateRequestException(DUPLICATE_FRIEND_REQUEST);
            } catch (Exception e) {
                response = friendRepository.save(new Friend(2, user, friend)); // 보낸 사람 Friend에 생기는 객체
                user.addFriend(response);
                friend.addFriend(friendRepository.save(new Friend(3, friend, user))); // 보낸 사람 Friend에 생기는 객체

            }

            return response;
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                 BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Friend acceptFriendRequest(Long userIdx, Long friendIdx) throws BaseException {
        try {
            Friend receiveRequest = findFriendByIdx(friendIdx);
            if(receiveRequest.getFriend().getUserIndex().equals(userIdx))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
            else if (receiveRequest.getFriendStatus().equals(1))
                throw new DuplicateRequestException(DUPLICATE_FRIEND_REQUEST);

            Friend sendRequest = findFriendByUserAndFriend(receiveRequest.getFriend(), receiveRequest.getUser());

            receiveRequest.updateStatus(1);
            sendRequest.updateStatus(1);

            return receiveRequest;
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Integer deleteFriendRequest(Long userIdx, Long friendIdx) throws BaseException {
        try {
            Friend friend1 = findFriendByIdx(friendIdx);

            // 내 친구 목록이 아닐 시
            if(!friend1.getUser().getUserIndex().equals(userIdx))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            Friend friend2;
            friend2 = findFriendByUserAndFriend(friend1.getFriend(), friend1.getUser());

            friendRepository.delete(friend1);
            friendRepository.delete(friend2);

            return friend1.getFriendStatus();
        } catch (EmptyEntityException |
                 BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Friend updateIsFixed(Long userIdx, Long friendIdx) throws BaseException {
        try {
            Friend friend = findFriendByIdx(friendIdx);
            if(!friend.getUser().getUserIndex().equals(userIdx))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            friend.updateIsFixed(!friend.getFriendIsFixed());

            return friend;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteFriends(User user) throws BaseException {
        try {
            List<Friend> friends = friendRepository.findFriendsByFriend(user);
            friendRepository.deleteAll(friends);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

class FriendStudyTimeComparator implements Comparator<Friend> {
    @Override
    public int compare(Friend o1, Friend o2) {
        if(o1.getFriend().getUserTotalStudyTime() > o2.getFriend().getUserTotalStudyTime())
            return -1;
        else if(o1.getFriend().getUserTotalStudyTime() < o2.getFriend().getUserTotalStudyTime())
            return 1;
        return 0;
    }
}
