package org.apache.ydata.utils;

import com.github.pagehelper.PageInfo;
import org.apache.ydata.vo.Page;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.Pager;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {

    public static PageResult getPageResult(PageRequest pageRequest, PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPages(pageInfo.getPages());
        pageResult.setData(pageInfo.getList());
        return pageResult;
    }



    public static Pager getPager(PageResult pageResult) {
        //转换
        Page page = new Page();
        page.setPageNo(pageResult.getPageNum());
        page.setPageSize(pageResult.getPageSize());
        page.setCount(pageResult.getTotal());
        page.setStatDesc(pageResult.getStatDesc());
        if(pageResult.getData() instanceof com.github.pagehelper.Page) {
            page.setList(((com.github.pagehelper.Page) pageResult.getData()).getResult());
        } else if(pageResult.getData() instanceof ArrayList) {
            page.setList((List) pageResult.getData());
        }

        Pager pager = new Pager<>();
        try {
            pager.wrapPager(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }
}
