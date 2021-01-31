package com.shengde.search.service.impl;

import com.shengde.e3mall.common.pojo.SearchItem;
import com.shengde.e3mall.common.utils.E3Result;
import com.shengde.search.mapper.ItemMapper;
import com.shengde.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    ItemMapper itemMapper;
    @Autowired
    SolrServer solrServer;

    @Override
    public E3Result importAllItems() {
       try {
        //查询商品列表
        List<SearchItem> itemList = itemMapper.getItemList();
        //遍历商品列表
        for (SearchItem searchItem : itemList) {
            //创建文档对象
            SolrInputDocument document = new SolrInputDocument();
            //像文档对象中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            //把文档对象写入索引库
            solrServer.add(document);
            }
            //提交
           solrServer.commit();
            //返回导入成功
           return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"数据导入发生异常");
       }
    }
}
