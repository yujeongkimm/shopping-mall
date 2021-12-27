package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

//repository 통해 Member entity를 데이터베이스에 저장
public interface MemberRepository extends JpaRepository<Member, Long> {

    //회원 가입 시 중복 회원 검사(쿼리 메소드)
    Member findByEmail(String email);
}
