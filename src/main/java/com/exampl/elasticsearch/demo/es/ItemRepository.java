package com.exampl.elasticsearch.demo.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: wuzhicheng
 *  @Param:
 *       * 	Item:为实体类
 *       * 	Long:为Item实体类中主键的数据类型
 * @date: 14:02 2019/3/25
 * @company:北京今汇在线
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
}
