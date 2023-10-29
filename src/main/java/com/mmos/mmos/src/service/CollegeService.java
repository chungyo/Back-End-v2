package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.CollegeRepository;
import com.mmos.mmos.src.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityRepository universityRepository;

    public University findUniversityByIdx(Long universityIdx) throws BaseException {
        return universityRepository.findById(universityIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_UNIVERSITY));
    }

    public List<College> findAllColleges(University university) throws BaseException {
        return collegeRepository.findAllByUniversity(university)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_COLLEGE));
    }

    @Transactional
    public College saveCollege(Long universityIdx, String name) throws BaseException {
        try {
            University university = findUniversityByIdx(universityIdx);

            College college = new College(name, university);
            university.addCollege(college);

            return collegeRepository.save(college);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<College> getColleges(Long universityIdx) throws BaseException {
        try {
            University university = findUniversityByIdx(universityIdx);
            return findAllColleges(university);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}