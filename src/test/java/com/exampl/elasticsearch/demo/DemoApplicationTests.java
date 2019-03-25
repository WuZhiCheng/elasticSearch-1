package com.exampl.elasticsearch.demo;

import com.exampl.elasticsearch.demo.es.Item;
import com.exampl.elasticsearch.demo.es.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
/**
* https://blog.csdn.net/chen_2890/article/details/83895646
* @Author wuzhicheng
* @param  * @param null
* @Date 2019/3/25 14:29
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * @Description:创建索引，会根据Item类的@Document注解信息来创建
     * @Author: https://blog.csdn.net/chen_2890
     * @Date: 2018/9/29 0:51
     */
    @Test
    public void testCreateIndex() {
        try {
            elasticsearchTemplate.createIndex(Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List list = new ArrayList<>();
        Item item = new Item(1L, "小米手机7", " 手机","小米", 3499.00, "http://image.baidu.com/13123.jpg");
        list.add(item);
        Item item2 = new Item(2L, "华为手机7", " 手机","小米", 3499.00, "http://image.baidu.com/13123.jpg");
        list.add(item2);
        Item item3 = new Item(3L, "中通手机7", " 手机","小米", 3499.00, "http://image.baidu.com/13123.jpg");
        list.add(item3);
        //        itemRepository.save(item);
        itemRepository.saveAll(list);
        System.out.println("end");


        Iterable<Item> list2 = this.itemRepository.findAll(Sort.by("price").ascending());

        for (Item itm:list2){
            System.out.println(itm);
        }
        System.out.println("end2");

    }

    @Test
    public void insertList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.baidu.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    /**
     * @Description:matchQuery底层采用的是词条匹配查询
     * @Author: https://blog.csdn.net/chen_2890
     */
    @Test
    public void testMatchQuery(){
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米手机"));
        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * @Description:
     * termQuery:功能更强大，除了匹配字符串以外，还可以匹配
     * int/long/double/float/....
     * @Author: https://blog.csdn.net/chen_2890
     */
    @Test
    public void testTermQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.termQuery("price",998.0));
        // 查找
        Page<Item> page = this.itemRepository.search(builder.build());

        for(Item item:page){
            System.out.println(item);
        }
    }
    /**
     * @Description:布尔查询
     * @Author: https://blog.csdn.net/chen_2890
     */
    @Test
    public void testBooleanQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title","华为"))
                        .must(QueryBuilders.matchQuery("brand","华为")));
        // 查找
        Page<Item> page = this.itemRepository.search(builder.build());
        for(Item item:page){
            System.out.println(item);
        }
    }

    /**
     * @Description:模糊查询
     * @Author: https://blog.csdn.net/chen_2890
     */
    @Test
    public void testFuzzyQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.fuzzyQuery("title","faceoooo"));
        Page<Item> page = this.itemRepository.search(builder.build());
        for(Item item:page){
            System.out.println(item);
        }

    } 

    @Test
    public void testQuery() {
        Iterable<Item> list2 = itemRepository.findAll(Sort.by("price").ascending());
        for (Item itm:list2){
            System.out.println(itm);
        }
        System.out.println("end2");
    }

}
