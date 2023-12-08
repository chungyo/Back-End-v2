package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_COLLEGE;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityService universityService;

    public List<College> findAllColleges(University university) throws BaseException {
        return collegeRepository.findAllByUniversity(university)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_COLLEGE));
    }

    public College findCollegeByIdx(Long collegeIdx) throws BaseException {
        return collegeRepository.findById(collegeIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_COLLEGE));
    }

    @Transactional
    public College saveCollege(Long universityIdx, String name) throws BaseException {
        try {
            University university = universityService.getUniversity(universityIdx);

            College college = new College(name, university);
            university.addCollege(college);

            return collegeRepository.save(college);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<College> getColleges(Long universityIdx) throws BaseException {
        try {
            University university = universityService.getUniversity(universityIdx);
            return findAllColleges(university);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}