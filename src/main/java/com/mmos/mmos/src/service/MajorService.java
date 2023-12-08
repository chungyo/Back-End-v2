package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_MAJOR;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeService collegeService;


    public List<Major> findAllMajors(College college) throws BaseException {
        return majorRepository.findAllByCollege(college)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_MAJOR));
    }

    public Major findMajorByIdx(Long majorIdx) throws BaseException {
        return majorRepository.findById(majorIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_MAJOR));
    }

    @Transactional
    public Major saveMajor(Long collegeIdx, String name) throws BaseException {
        try {
            College college = collegeService.findCollegeByIdx(collegeIdx);
            Major major = new Major(name, college);

            college.addMajor(major);
            return majorRepository.save(major);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Major getMajor(Long majorIdx) throws BaseException {
        try {
            return findMajorByIdx(majorIdx);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Major> getMajors(Long collegeIdx) throws BaseException {
        try {
            // college 객체 가져오기
            College college = collegeService.findCollegeByIdx(collegeIdx);
            return findAllMajors(college);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}