package org.sopt.befit.mapper;

import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Products;
import org.sopt.befit.model.ProductReq;

import java.util.List;

@Mapper
public interface ProductsMapper {

    @Select("select k.*, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, (SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx}) as k WHERE k.brand_idx = b.idx;")
    List<ProductReq> findAll(@Param("curIdx") final int curIdx);

    @Select("select k.*, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, (SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.product_category_index = #{product_category_index}) as k WHERE k.brand_idx = b.idx ORDER BY date DESC;")
    List<ProductReq> findByCategoryNew(@Param("product_category_index") final int product_category_index, @Param("curIdx") final int curIdx);


    @Select("select k.*, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, (SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.product_category_index = #{product_category_index}) as k WHERE k.brand_idx = b.idx ORDER BY like_score DESC;")
    List<ProductReq> findByCategoryPopular(@Param("product_category_index") final int product_category_index, @Param("curIdx") final int curIdx);

    @Select("select k.*, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, (SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.brand_idx = #{brand_idx}) as k WHERE k.brand_idx = b.idx ORDER BY date DESC;")
    List<ProductReq> findByBrandNew(@Param("brand_idx") final int brand_idx, @Param("curIdx") final int curIdx);

    @Select("select k.*, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, (SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.brand_idx = #{brand_idx}) as k WHERE k.brand_idx = b.idx ORDER BY like_score DESC;")
    List<ProductReq> findByBrandPopular(@Param("brand_idx") final int brand_idx, @Param("curIdx") final int curIdx);


//    유정

    @Select("select p.* from product as p inner join (select idx, style1 from brand where gender=\"공용\" or gender=#{gender}) as b on b.style1=#{style} " +
            "and p.brand_idx=b.idx order by like_score desc limit #{limit_count}")
    List<ProductReq> getProductByStyle(@Param("gender") final String gender, @Param("style") final String style, @Param("limit_count") final int limit_count);

    // 특정 브랜드의 like_score가 높은 세 개의 데이터 조회
    @Select("select p.*, b.name_korean as brand_Korean_name from product as p inner join brand as b " +
            "on p.brand_idx = b.idx where brand_idx = #{brand_idx} order by like_score desc limit 3")
    List<ProductReq> getThreeProductByOneBrand(@Param("brand_idx") final int brand_idx);

    @Select("SELECT *, " +
            "(SELECT COUNT(*) FROM like_product WHERE user_idx = product.idx) AS product_like, " +
            "(SELECT name_korean FROM brand WHERE idx = product.brand_idx) AS brand_korean_name  " +
            "FROM product WHERE idx= #{product_idx};")
    ProductReq findProductById (@Param("product_idx") final int product_idx);

}
