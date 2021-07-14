package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        //1.從redis查詢數據
        //1.1獲取jedis客戶端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可使用sortedset排序查詢
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查詢sortedset中的分數(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
        //2.判斷查詢的集合是否為空
        if(categorys == null || categorys.size()==0){
            System.out.println("從數據庫查詢...");
            //2.1如果集合為空,需要從數據庫中查詢,在寫入redis
            cs = categoryDao.findAll();
            //2.2將集合數據存儲到redis中category的key
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());

            }
        }else{
            System.out.println("從redis中查詢...");
//                //如果不為空則將數據set存入到list
            cs = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                cs.add(category);
            }
        }

        return cs;
    }

}
