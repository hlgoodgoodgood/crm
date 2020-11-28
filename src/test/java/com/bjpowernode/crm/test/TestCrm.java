package com.bjpowernode.crm.test;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.MD5Util;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.workbench.bean.Transaction;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.test
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/16 11:50
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
public class TestCrm {


    //测试主键生成UUID
    @Test
    public void test01(){
        String uuid = UUIDUtil.getUUID();

        System.out.println(uuid);
    }

    //测试MD5加密
    @Test
    public void test02(){
        //测试md5加密
        String admin = MD5Util.getMD5("xiaohong");

        System.out.println(admin);
    }

    //测试日期比较
    //now>time 返回值>0 now < time 返回值<0
    @Test
    public void test03(){
        String now = DateTimeUtil.getSysTime();

        String time = "2032-02-10";

        System.out.println(now.compareTo(time));
    }

    //测试自定义异常
    @Test
    public void test04(){
//        System.out.println(CrmExceptionEnum.LOGIN_ACCOUNT_ERROR.getMess());
        try{
            int a = 0;
            if(a == 0){
                throw new CrmException(CrmExceptionEnum.LOGIN_ACCOUNT_ERROR);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //测试查询所有市场活动信息数据
    @Test
    public void test05(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        ActivityMapper activityMapper = sqlSessionFactory.openSession().getMapper(ActivityMapper.class);
        List<Map<String, String>> maps = activityMapper.queryAllActivity(null);
        System.out.println(maps);
    }

    //回顾Iterable接口
    @Test
    public void test06(){
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        Iterable<String> iterator = ids;
        System.out.println(iterator);

    }

    //Map
    @Test
    public void test07(){
        Map<String,String> names = new TreeMap<>();
        names.put("a","张三");
        names.put("name","李四");
        names.put("def","王五");
        System.out.println(names);
    }

    //统计处在不同交易阶段的交易数量
    @Test
    public void test08(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        TransactionMapper transactionMapper = sqlSessionFactory.openSession().getMapper(TransactionMapper.class);
        List<Map<String, String>> maps = transactionMapper.queryTransactionEcharts();

        for (Map<String, String> map : maps) {
            System.out.println(map);
        }
    }


    //Java-->Excel
   /* @Test
    public void test09() throws Exception {

        //创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //创建一个工作表，并指定名字
        HSSFSheet sheet = workbook.createSheet("交易信息");


        //写解决第一行标题
        HSSFRow firstRow = sheet.createRow(0);

        //让单元格内容居中显示
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        //水平居中
        cellStyle.setAlignment((short)2);
        // 设置背景色
        cellStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        //设置加粗
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //设置字体样式
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 20);//设置字体大小
        cellStyle.setFont(font);
        HSSFCell cell1 = firstRow.createCell(0);
        cell1.setCellStyle(cellStyle);
        cell1.setCellValue("交易报表信息");
        HSSFCell cell2 = firstRow.createCell(1);


        //合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 16);
        sheet.addMergedRegion(region);

        //交易的字段名称
        String[] titles = {"所有者","金额","名称","预计成交日期","客户",
                "阶段","类型","来源","市场活动","联系人","创建人","创建时间"
                ,"修改人","修改时间","描述","联系纪要","下次联系时间"};

        HSSFRow secondRow = sheet.createRow( 1);
        for(int i = 0 ; i < titles.length; i++){
            HSSFCell cell = secondRow.createCell(i);
            cell.setCellValue(titles[i]);
        }

        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        TransactionMapper transactionMapper = sqlSessionFactory.openSession().getMapper(TransactionMapper.class);
        List<Transaction> transactions = transactionMapper.selectAll();


        Class<Transaction> transactionClass = Transaction.class;
        Method[] methods = transactionClass.getMethods();

        List<List<String>> data = new ArrayList<>();
        for(int i = 0 ; i < transactions.size(); i++){
            List<String> perRowData = new ArrayList<>();
            Transaction transaction = transactions.get(i);
            for(int j = 0 ; j < titles.length; j++){
                perRowData.add(transaction.getOwner());
                perRowData.add(transaction.getMoney());
                perRowData.add(transaction.getName());
                perRowData.add(transaction.getExpectedDate());
                perRowData.add(transaction.getCustomerId());
                perRowData.add(transaction.getStage());
                perRowData.add(transaction.getType());
                perRowData.add(transaction.getSource());
                perRowData.add(transaction.getActivityId());
                perRowData.add(transaction.getContactsId());
                perRowData.add(transaction.getCreateBy());
                perRowData.add(transaction.getCreateTime());
                perRowData.add(transaction.getEditBy());
                perRowData.add(transaction.getEditTime());
                perRowData.add(transaction.getDescription());
                perRowData.add(transaction.getContactSummary());
                perRowData.add(transaction.getNextContactTime());
            }
            data.add(perRowData);
        }
       
        for(int i = 0; i < data.size();i++){
            List<String> perRowData = data.get(i);
            HSSFRow row = sheet.createRow( i+2);
            for(int j = 0 ; j < titles.length; j++){
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(perRowData.get(j));
            }
        }
        File file = new File("D:/test.xls");
        FileOutputStream outputStream = new FileOutputStream(file);

        //将Excel写入输出流中
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }*/

    //使用Hutool完成Excel写入操作
    @Test
    public void test10(){
        BeanFactory beanFactory =
                new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) beanFactory.getBean("sqlSessionFactory");
        TransactionMapper transactionMapper = sqlSessionFactory.openSession().getMapper(TransactionMapper.class);
        List<Transaction> transactions = transactionMapper.selectAll();

        //交易的字段名称
        String[] titles = {"所有者","金额","名称","预计成交日期","客户",
                "阶段","类型","来源","市场活动","联系人","创建人","创建时间"
                ,"修改人","修改时间","描述","联系纪要","下次联系时间"};
        List<List<String>> data = new ArrayList<>();
        for(int i = 0 ; i < transactions.size(); i++){
            List<String> perRowData = new ArrayList<>();
            Transaction transaction = transactions.get(i);
            for(int j = 0 ; j < titles.length; j++){
                perRowData.add(transaction.getOwner());
                perRowData.add(transaction.getMoney());
                perRowData.add(transaction.getName());
                perRowData.add(transaction.getExpectedDate());
                perRowData.add(transaction.getCustomerId());
                perRowData.add(transaction.getStage());
                perRowData.add(transaction.getType());
                perRowData.add(transaction.getSource());
                perRowData.add(transaction.getActivityId());
                perRowData.add(transaction.getContactsId());
                perRowData.add(transaction.getCreateBy());
                perRowData.add(transaction.getCreateTime());
                perRowData.add(transaction.getEditBy());
                perRowData.add(transaction.getEditTime());
                perRowData.add(transaction.getDescription());
                perRowData.add(transaction.getContactSummary());
                perRowData.add(transaction.getNextContactTime());
            }
            data.add(perRowData);
        }

        ExcelWriter excelWriter = ExcelUtil.getWriter("d:/交易.xls");
        excelWriter.merge(titles.length - 1, "交易报表新");
        excelWriter.write(data, true);
        //关闭writer，释放内存
        excelWriter.close();
    }

}