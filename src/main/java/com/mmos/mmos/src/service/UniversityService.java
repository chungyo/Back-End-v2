package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_UNIVERSITY;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository universityRepository;

    public University findUniversity(Long universityIdx) throws BaseException {
        return universityRepository.findById(universityIdx)
                .orElseThrow(()-> new EmptyEntityException(EMPTY_UNIVERSITY));
    }
    public List<University> findUniversities() {
        return universityRepository.findAll();
    }

    @Transactional
    public University getUniversity(Long universityIdx) throws BaseException {
        try {
            return findUniversity(universityIdx);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<University> getUniversities() throws BaseException {
        try {
            return findUniversities();
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public University saveUniversity(String name) throws BaseException {
        try {
            University university = new University(name);
            return universityRepository.save(university);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
