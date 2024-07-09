package com.lvxc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.domain.dto.demo.DemoTableDto;
import com.lvxc.user.entity.DemoTable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * demo表结构 服务类
 * </p>
 *
 * @author lvxc
 * @since 2023-08-16
 */
public interface IDemoTableService extends IService<DemoTable> {

    String batchDownLoad(HttpServletResponse response, DemoTableDto demoTableDto) throws IOException;

    void batchUpLoad(MultipartFile file, HttpServletRequest request);
}
