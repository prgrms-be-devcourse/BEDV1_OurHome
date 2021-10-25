package com.armand.ourhome.community.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DummyService {

    private final DummyRepository dummyRepository;

    public List<DummyDto> findAll(){
        return dummyRepository.findAll().stream()
                .map(DummyDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public DummyDto save(DummyDto dummyDto){
        Dummy save = dummyRepository.save(Dummy.of(dummyDto));
        return DummyDto.of(save);
    }
}
