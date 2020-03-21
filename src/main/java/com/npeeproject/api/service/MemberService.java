package com.npeeproject.api.service;

import com.npeeproject.api.advice.exception.MemberNotFoundException;
import com.npeeproject.api.advice.exception.ValidCustomException;
import com.npeeproject.api.model.Member;
import com.npeeproject.api.model.request.MemberRequestDto;
import com.npeeproject.api.model.response.MemberResponseDto;
import com.npeeproject.api.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponseDto save(MemberRequestDto memberRequestDto) {

        verifyDuplicateEmail(memberRequestDto.getEmail(), "email");
        return new MemberResponseDto(memberRepository.save(memberRequestDto.toEntity()));
    }

    private void verifyDuplicateEmail(String email, String field) {

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ValidCustomException("이미 사용중인 이메일입니다", field);
        }
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {

        return memberRepository
                .findAll()
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponseDto update(MemberRequestDto memberRequestDto) {

        verifyDuplicateEmail(memberRequestDto.getEmail(), "update-email");
        return new MemberResponseDto(memberRepository.save(memberRequestDto.toEntity(memberRequestDto.getId())));
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id) {

        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return member.map(MemberResponseDto::new).get();
        } else {
            return new MemberResponseDto(member.orElseThrow(MemberNotFoundException::new));
        }
    }

    @Transactional
    public void deleteById(Long id) {

        memberRepository.deleteById(id);
    }

}
