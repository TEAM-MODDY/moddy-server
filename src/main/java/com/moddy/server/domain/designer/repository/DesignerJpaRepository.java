package com.moddy.server.domain.designer.repository;

import com.moddy.server.domain.designer.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DesignerJpaRepository extends JpaRepository<Designer, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into designer (id, hair_shop_address, hair_shop_detail_address, hair_shop_name, instagram_url, naver_place_url, introduction, kakao_open_chat_url) VALUES (:id, :hairShopAddress, :hairShopDetailAddress, :hairShopName, :instagramUrl, :naverPlaceUrl, :introduction, :kakaoOpenChatUrl)", nativeQuery = true)
    void designerRegister(@Param("id") Long id, @Param("hairShopAddress") String hairShopAddress, @Param("hairShopDetailAddress") String hairShopDetailAddress, @Param("hairShopName") String hairShopName, @Param("instagramUrl") String instagramUrl, @Param("naverPlaceUrl") String naverPlaceUrl, @Param("introduction") String introduction, @Param("kakaoOpenChatUrl") String kakaoOpenChatUrl);

    Optional<Designer> findById(Long userId);
}