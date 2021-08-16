package com.doter.common.security.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class IgnoreProperties {

    /**
     * 需要忽略的 URL 格式，不考虑请求方法
     */
    @Value("security.ignore.pattern")
    private List<String> pattern = Lists.newArrayList();

    /**
     * 需要忽略的 GET 请求
     *
     */
    @Value("security.ignore.get")
    private List<String> get = Lists.newArrayList();

    /**
     * 需要忽略的 POST 请求
     */
    @Value("security.ignore.post")
    private List<String> post = Lists.newArrayList();

    /**
     * 需要忽略的 DELETE 请求
     */
    @Value("security.ignore.delete")
    private List<String> delete = Lists.newArrayList();

    /**
     * 需要忽略的 PUT 请求
     */
    @Value("security.ignore.put")
    private List<String> put = Lists.newArrayList();

}