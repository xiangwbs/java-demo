package com.xwbing.controller.report;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xwbing.entity.dto.userDto;
import com.xwbing.service.sys.SysUserService;
import com.xwbing.util.CommonConstant;
import com.xwbing.util.ExcelUtil;
import com.xwbing.util.LoginSysUserUtil;

/**
 * 
 * 说明:  导出excel示例  <br/>
 * 创建日期: 2017年3月7日 下午4:38:44 <br/>
 * 作者: xwb
 */
@Controller
@RequestMapping("/report/")
public class ReportController {
    @Autowired
    private SysUserService sysUserService;
    private Logger log = LoggerFactory.getLogger(ReportController.class);
    /**
     * 导出报表
     * 
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "exportReport", method = RequestMethod.POST)
    public void exportReport(HttpServletResponse response) throws IOException {
        String userId = LoginSysUserUtil.getUserId();
        if (StringUtils.isEmpty(userId)) {
            log.error("不能获取登录用户信息，请重新登录");// 登录用户才能导出报表，这步也可以省略
        }
        String fileName = CommonConstant.USERREPORTFILENAME;//文件名 
        fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
        response.reset();
        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);// 指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ServletOutputStream output = response.getOutputStream();
        BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
        List<userDto> listDto = sysUserService.getReportList();//内容list
        if (CollectionUtils.isEmpty(listDto)) {
            return;
        }
        List<String[]> list = ExcelUtil.convert2List(listDto);
        String title = CommonConstant.USERREPORTFILENAME;
        String[] columns = CommonConstant.USERREPORTCOLUMNS;
        try {
            bufferedOutPut.flush();
            HSSFWorkbook wb = ExcelUtil.Export(title, columns, list);
            wb.write(bufferedOutPut);
            bufferedOutPut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bufferedOutPut.close();
        }
    }
}
