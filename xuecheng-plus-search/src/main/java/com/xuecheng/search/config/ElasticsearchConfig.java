package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
//        //集群部署
//        //解析hostlist配置信息
//        String[] split = hostlist.split(",");
//        //创建HttpHost数组，其中存放es主机和端口的配置信息
//        HttpHost[] httpHostArray = new HttpHost[split.length];
//        for(int i=0;i<split.length;i++){
//            String item = split[i];
//            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
//        }
//        //创建RestHighLevelClient客户端
//        //集群创建
//        return new RestHighLevelClient(RestClient.builder(httpHostArray));

        //由于本项目为单人执行，故仅一台主机，不需要集群部署 可进行优化//此处为单节点创建
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(hostlist)));

    }


}