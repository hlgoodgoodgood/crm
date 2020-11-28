package com.bjpowernode.crm.base.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.util
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/28 16:27
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
public class ExcelUtil {

    //java-->Excel
    public static void writeToExcel(String path,List<List<String>> data,String[] titles){

        //创建工作簿
      /*  HSSFWorkbook workbook = new HSSFWorkbook();

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



        HSSFRow secondRow = sheet.createRow( 1);
        for(int i = 0 ; i < titles.length; i++){
            HSSFCell cell = secondRow.createCell(i);
            cell.setCellValue(titles[i]);
        }

        for(int i = 0; i < data.size();i++){
            List<String> perRowData = data.get(i);
            HSSFRow row = sheet.createRow( i+2);
            for(int j = 0 ; j < titles.length; j++){
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(perRowData.get(j));
            }
        }

        File file = new File(path);
        try{
            FileOutputStream outputStream = new FileOutputStream(file);

            //将Excel写入输出流中
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}