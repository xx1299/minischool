package com.s1mple.minischool.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trendsimg {

    @TableId
    private Long img_id;
    private Long tid;
    private String imgUrl;

}
