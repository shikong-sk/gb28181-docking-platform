package cn.skcks.docking.gb28181.common.page;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuppressWarnings({"unused"})
@Schema(title = "分页数据")
public class PageWrapper<T> {
    @Schema(title = "数据")
    private Collection<T> data;
    @Schema(title = "页码")
    private long page;
    @Schema(title = "每页大小")
    private long pageSize;
    @Schema(title = "总页数")
    private long pageTotal;
    @Schema(title = "数据总数")
    private long total;

    public static <T> PageWrapper<T> of(Collection<T> data, long page, long pageSize, long pageTotal, long total) {
        return new PageWrapper<>(data, page, pageSize, pageTotal, total);
    }

    public static <T> PageWrapper<T> of(PageInfo<T> pageInfo) {
        return new PageWrapper<>(pageInfo.getList(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getPages(),
                pageInfo.getTotal());
    }
}
