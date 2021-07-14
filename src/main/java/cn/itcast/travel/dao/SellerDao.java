package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根據id查詢
     * @param id
     * @return
     */
    public Seller findById(int id);
}
