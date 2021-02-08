package com.alibaba.nacos.client.config.filter.impl;

import com.alibaba.nacos.api.config.filter.IConfigFilter;
import com.alibaba.nacos.api.config.filter.IConfigFilterChain;
import com.alibaba.nacos.api.config.filter.IConfigRequest;
import com.alibaba.nacos.api.config.filter.IConfigResponse;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 配置筛选器链管理。
 */
public class ConfigFilterChainManager implements IConfigFilterChain {

    private final List<IConfigFilter> filters = Lists.newArrayList();

    /**
     * Add filter.
     *
     * @param filter filter
     * @return this
     */
    public synchronized ConfigFilterChainManager addFilter(IConfigFilter filter) {
        // 根据order大小顺序插入
        int i = 0;
        while (i < this.filters.size()) {
            IConfigFilter currentValue = this.filters.get(i);
            if (currentValue.getFilterName().equals(filter.getFilterName())) {
                break;
            }
            if (filter.getOrder() >= currentValue.getOrder() && i < this.filters.size()) {
                i++;
            } else {
                this.filters.add(i, filter);
                break;
            }
        }

        if (i == this.filters.size()) {
            this.filters.add(i, filter);
        }
        return this;
    }

    @Override
    public void doFilter(IConfigRequest request, IConfigResponse response) throws NacosException {
        new VirtualFilterChain(this.filters).doFilter(request, response);
    }

    private static class VirtualFilterChain implements IConfigFilterChain {

        private final List<? extends IConfigFilter> additionalFilters;

        private int currentPosition = 0;

        public VirtualFilterChain(List<? extends IConfigFilter> additionalFilters) {
            this.additionalFilters = additionalFilters;
        }

        @Override
        public void doFilter(final IConfigRequest request, final IConfigResponse response) throws NacosException {
            if (this.currentPosition != this.additionalFilters.size()) {
                this.currentPosition++;
                IConfigFilter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
                nextFilter.doFilter(request, response, this);
            }
        }
    }

}
