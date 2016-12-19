package cn.itcast.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class TestPOI2Excel {
	@Test
	public void testWrite03Excel() throws Exception{
		//1创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//2创建工作表 
		HSSFSheet sheet=  workbook.createSheet("hello world");
		//3创建行
		HSSFRow row = sheet.createRow(3);//从0开始
		//4创建单元格
		HSSFCell cell =	row.createCell(3);//从0开始
		//写入内容
		cell.setCellValue("hello world");
		
		//输出到硬盘
		FileOutputStream outputStream = new FileOutputStream("E:\\MyEclipseCode\\tax\\测试.xls");
		//把Excel输出到具体的地址
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	@Test
	public void testWrite07Excel() throws Exception{
		//1创建工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//2创建工作表 
		XSSFSheet sheet=  workbook.createSheet("hello world");
		
		
		//3创建行
		XSSFRow row = sheet.createRow(3);//从0开始
		//4创建单元格
		XSSFCell cell =	row.createCell(3);//从0开始
		//写入内容
		cell.setCellValue("hello world");
		
		//输出到硬盘
		FileOutputStream outputStream = new FileOutputStream("E:\\MyEclipseCode\\tax\\测试.xlsx");
		//把Excel输出到具体的地址
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	
	
	
	
	@Test
	public void testRead03Excel() throws Exception{
		FileInputStream inputStream = new FileInputStream("E:\\MyEclipseCode\\tax\\测试.xls");
		//1读取工作簿
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		//2读取工作表 
		HSSFSheet sheet=  workbook.getSheetAt(0);
		//3读取行
		HSSFRow row = sheet.getRow(3);
		//4读取单元格
		HSSFCell cell =	row.getCell(3);
		//读取内容
		//输出到控制台
		System.out.println("第四行第四列的单元格内容为:"+cell.getStringCellValue());
		
		workbook.close();
		inputStream.close();
	}
	@Test
	public void testRead07Excel() throws Exception{
		FileInputStream inputStream = new FileInputStream("E:\\MyEclipseCode\\tax\\测试.xlsx");
		//1读取工作簿
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		//2读取工作表 
		XSSFSheet sheet=  workbook.getSheetAt(0);
		//3读取行
		XSSFRow row = sheet.getRow(3);
		//4读取单元格
		XSSFCell cell =	row.getCell(3);
		//读取内容
		//输出到控制台
		System.out.println("第四行第四列的单元格内容为:"+cell.getStringCellValue());
		
		workbook.close();
		inputStream.close();
	}
	
	@Test
	public void testRead03And07Excel() throws Exception{
		String fileName = "E:\\MyEclipseCode\\tax\\测试.xlsx";
		FileInputStream inputStream = new FileInputStream(fileName);
		if(fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){//判断是否是excel文档
			boolean is03Excel = fileName.matches("^.+\\.(?i)(xls)$");
			//1读取工作簿
			Workbook workbook = is03Excel?new HSSFWorkbook(inputStream): new XSSFWorkbook(inputStream);
			//2读取工作表 
			Sheet sheet=  workbook.getSheetAt(0);
			//3读取行
			Row row = sheet.getRow(3);
			//4读取单元格
			Cell cell =	row.getCell(3);
			//读取内容
			//输出到控制台
			System.out.println("第四行第四列的单元格内容为:"+cell.getStringCellValue());
			
			workbook.close();
			inputStream.close();
		}
	}
	
	//1-excel合并 属于工作薄 用于工作表       *.1:合并单元格
	//2-excel样式 属于工作薄 用于单元格       *.2:创建单元格样式
	//3-excle字体 属于工作薄 加载于样式 用于单元格 	*.3:
	@Test
	public void testExcelStyle() throws Exception{
		//1创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//1.1 创建合并单元格对象
		CellRangeAddress cellRangeAddress = new CellRangeAddress(3, 3, 3, 5);
		//1.2创建单元格样式对象
		HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
		hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		hssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//1.3创建字体
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗字体
		font.setFontHeightInPoints((short)16);//设置字体大小
		//加载字体
		hssfCellStyle.setFont(font);
		//单元格背景
		//设置背景填充模式
		hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置填充背景色
		hssfCellStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);//要给填充模式才行,不能只用这个
		//设置填充前景色
		hssfCellStyle.setFillForegroundColor(HSSFColor.RED.index);
		//2创建工作表 
		HSSFSheet sheet=  workbook.createSheet("hello world");
		//2.1加载合并单元格对象
		sheet.addMergedRegion(cellRangeAddress);
		//3创建行
		HSSFRow row = sheet.createRow(3);//从0开始
		//4创建单元格
		//4.2单元格加载样式
		HSSFCell cell =	row.createCell(3);//从0开始
		cell.setCellStyle(hssfCellStyle);
		//写入内容
		cell.setCellValue("hello world");
		
		//输出到硬盘
		FileOutputStream outputStream = new FileOutputStream("E:\\MyEclipseCode\\tax\\测试.xls");
		//把Excel输出到具体的地址
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	
	
}
