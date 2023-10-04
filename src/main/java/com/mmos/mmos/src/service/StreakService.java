package com.mmos.mmos.src.service;

import com.mmos.mmos.src.repository.StreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final StreakRepository streakRepository;
}
