package cn.itcast.core.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.itcast.nsfw.user.entity.User;

public class ExcelUtil {
	/**
	 * 导出用户的所有列表到excel
	 * @param userList 用户列表
	 * @param outputStream 输出流
	 */
	public static void exportUserExcel(List<User> userList,
			ServletOutputStream outputStream) {
		try {
			//1创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			//1.1 创建合并单元格对象
			CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 4);
			//1.2头标题样式
			HSSFCellStyle style1= createCellSyle(workbook,(short) 16);
			//1.3列标题样式
			HSSFCellStyle style2 = createCellSyle(workbook,(short) 13);
			//2创建工作表
			HSSFSheet sheet =  workbook.createSheet("用户列表");
			//2.1加载合并单元格对象
			sheet.addMergedRegion(cellRangeAddress);
			//设置默认列宽
			sheet.setDefaultColumnWidth(25);
			//3创建行
			//3.1创建头标题行,并设置头标题
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell1 =  row1.createCell(0);
			//加载单元格样式
			cell1.setCellStyle(style1);
			cell1.setCellValue("用户列表");
			//3.2创建列表标题行，并设置列标题
			HSSFRow row2 = sheet.createRow(1);
			String[] titles = {"用户名","账号","所属部门","性别","电子邮箱"};
			for(int i=0;i<titles.length;i++){
				HSSFCell cell2 =  row2.createCell(i);
				//加载单元格样式
				cell2.setCellStyle(style2);
				cell2.setCellValue(titles[i]);
			}
			//4操作单元格 将用户写入excel
			if (userList!=null) {
				for(int j=0;j<userList.size();j++){
					HSSFRow row = sheet.createRow(j+2);
					HSSFCell cell11 = row.createCell(0);
					cell11.setCellValue(userList.get(j).getName());
					HSSFCell cell12 = row.createCell(1);
					cell12.setCellValue(userList.get(j).getAccount());
					HSSFCell cell13 = row.createCell(2);
					cell13.setCellValue(userList.get(j).getDept());
					HSSFCell cell14 = row.createCell(3);
					cell14.setCellValue(userList.get(j).isGender()?"男":"女");
					HSSFCell cell15 = row.createCell(4);
					cell15.setCellValue(userList.get(j).getEmail());
				}
			}
			//5输出
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 创建单元格样式
	 * @param workbook 工作薄
	 * @param fontSize 字体大小
	 * @return 单元格样式
	 */
	private static HSSFCellStyle createCellSyle(HSSFWorkbook workbook,short fontSize) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints(fontSize);
		//样式加载字体
		style.setFont(font);
		return style;
	}
}
