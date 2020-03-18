package com.npeeproject.api.member;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MemberRequestDto {

    private Long id;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "[0~9]{11}", message = "11자리의 숫자로 구성되어야 합니다")
    private String phoneNumber;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "양식에 맞지 않습니다")
    private String email;

    public Member toEntity() {
        //String[] phones = parsePhone();
        //return new Member(name, phones[0], phones[1], phones[2], email);
        Member member = Member.builder()
                .name(name)
                .phone1(parsePhone()[0])
                .phone2(parsePhone()[1])
                .phone3(parsePhone()[2])
                .email(email)
                .build();
        return member;
    }

    private String[] parsePhone() {
        String[] phones = new String[3];
        phones[0] = phoneNumber.substring(0, 3);
        phones[1] = phoneNumber.substring(3, 7);
        phones[2] = phoneNumber.substring(7, 11);
        return phones;
    }

}