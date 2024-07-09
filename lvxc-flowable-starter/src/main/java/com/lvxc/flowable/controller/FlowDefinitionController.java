package com.lvxc.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.flowable.entity.dto.FlowProcDefDto;
import com.lvxc.flowable.entity.vo.DeployFormVo;
import com.lvxc.flowable.entity.vo.FlowSaveXmlVo;
import com.lvxc.flowable.service.FlowDefinitionService;
import com.lvxc.web.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/5 10:40
 **/
@Slf4j
@Api(tags = "流程定义")
@RestController
@RequestMapping("/flowable/definition")
public class FlowDefinitionController {

    @Autowired
    protected FlowDefinitionService flowDefinitionService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "流程定义列表")
    public ResponseResult<Page<FlowProcDefDto>> list(@ApiParam(value = "流程名称", required = false) @RequestParam(required = false) String name, @ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum, @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return ResponseResult.success(flowDefinitionService.list(name,pageNum, pageSize));
    }

    @ApiOperation(value = "保存流程设计器内的xml文件")
    @PostMapping("/save")
    public ResponseResult<String> save(@RequestBody FlowSaveXmlVo vo) {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(vo.getXml().getBytes(StandardCharsets.UTF_8));
            flowDefinitionService.importFile(vo.getName(), vo.getCategory(), in);
        } catch (Exception e) {
            log.error("导入失败:", e);
            return ResponseResult.error(-1, e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("关闭输入流出错", e);
            }
        }

        return ResponseResult.success("导入成功");
    }

    @ApiOperation(value = "流程挂载表单")
    @PostMapping("/addDeployForm")
    public ResponseResult addDeployForm(@RequestBody DeployFormVo deployFormVo) {
        int num = flowDefinitionService.insertSysDeployForm(deployFormVo);
        return num > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "删除流程")
    @DeleteMapping(value = "/delete")
    public ResponseResult delete(@ApiParam(value = "流程部署ID", required = true) @RequestParam String deployId) {
        flowDefinitionService.delete(deployId);
        return ResponseResult.success();
    }

    @ApiOperation(value = "读取xml文件")
    @GetMapping("/readXml")
    public ResponseResult<String> readXml(@ApiParam(value = "流程定义id") @RequestParam(value = "deployId") String deployId) {
        try {
            return flowDefinitionService.readXml(deployId);
        } catch (Exception e) {
            return ResponseResult.error(-1, "加载xml文件异常");
        }

    }

    @ApiOperation(value = "发布或下线流程定义")
    @PutMapping(value = "/updateState")
    public ResponseResult updateState(@ApiParam(value = "1:发布,2:下线", required = true) @RequestParam Integer state, @ApiParam(value = "流程部署ID", required = true) @RequestParam String deployId) {
        flowDefinitionService.updateState(state, deployId);
        return ResponseResult.success();
    }


}
