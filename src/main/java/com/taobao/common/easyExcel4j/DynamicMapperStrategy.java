package com.taobao.common.easyExcel4j;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.google.common.collect.Maps;
import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

/**
 * 动态规则匹配法<br>
 * 通过set()方法来建立excel的列与object的属性之间的对应关系
 * 
 * @author feiying.gh
 */
public class DynamicMapperStrategy extends AbstractMapperStrategy {

	public <T> DynamicMapperStrategy(Class<T> clazz) {
		super(new ExcelConfig(clazz));
	}
	
	public DynamicMapperStrategy(ExcelConfig config){
		super(config);
	}

	public <T> T getInstance() {
		EasyExcelCglibProxy proxy = new EasyExcelCglibProxy(config.getClazz());
		return proxy.getProxyInstance();
	}

	private ExcelObjectMapperDO add(String columnName, boolean required,
			Class<?> type) {
		ExcelObjectMapperDO eom = new ExcelObjectMapperDO();
		eom.setExcelColumnName(columnName);
		eom.setRequired(required);
		eom.setObjectFieldType(type);
		add(config.getClazz(), eom);
		return eom;
	}

	// String
	public String anyString(String columnName) {
		return anyString(columnName, true);
	}

	public String anyString(String columnName, boolean required) {
		add(columnName, required, String.class);
		return null;
	}

	// Integer
	public int anyInteger(String columnName) {
		return anyInteger(columnName, true);
	}

	public int anyInteger(String columnName, boolean required) {
		add(columnName, required, Integer.class);
		return 0;
	}

	// Long
	public long anyLong(String columnName) {
		return anyLong(columnName, true);
	}

	public long anyLong(String columnName, boolean required) {
		add(columnName, required, Long.class);
		return 0L;
	}

	// Double
	public double anyDouble(String columnName) {
		return anyDouble(columnName, true);
	}

	public double anyDouble(String columnName, boolean required) {
		add(columnName, required, Double.class);
		return 0d;
	}

	// Float
	public float anyFloat(String columnName) {
		return anyFloat(columnName, true);
	}

	public float anyFloat(String columnName, boolean required) {
		add(columnName, required, Float.class);
		return 0f;
	}

	// Byte
	public byte anyByte(String columnName) {
		return anyByte(columnName, true);
	}

	public byte anyByte(String columnName, boolean required) {
		add(columnName, required, Byte.class);
		return '0';
	}

	// Short
	public short anyShort(String columnName) {
		return anyShort(columnName, true);
	}

	public short anyShort(String columnName, boolean required) {
		add(columnName, required, Short.class);
		return '0';
	}

	// Char
	public char anyCharacter(String columnName) {
		return anyCharacter(columnName, true);
	}

	public char anyCharacter(String columnName, boolean required) {
		add(columnName, required, Character.class);
		return '0';
	}

	// Boolean
	public boolean anyBoolean(String columnName, String isTrueString,
			String isFalseString) {
		return anyBoolean(columnName, isTrueString, isFalseString, true);
	}

	public boolean anyBoolean(String columnName, String isTrueString,
			String isFalseString, boolean required) {
		Map<String, Boolean> map = Maps.newHashMap();
		map.put(isTrueString, Boolean.TRUE);
		map.put(isFalseString, Boolean.FALSE);
		add(columnName, required, Boolean.class).setValueMap(map);
		return true;
	}

	// Date
	public Date anyDate(String columnName) {
		return anyDate(columnName, true);
	}

	public Date anyDate(String columnName, boolean required) {
		add(columnName, required, Date.class);
		return null;
	}

	@Override
	public void init(FileItem fileItem) throws Exception {
		try {
			
			// 找到第一行
			HSSFRow row = EasyExcelUtils.getRow(fileItem, config.sheetNum(fileItem), config.startRowNum(fileItem));
			// 根据字段名称找列号
			Iterator<?> it = row.cellIterator();
			for (int i = 0; it.hasNext(); i++) {
				HSSFCell cell = (HSSFCell) it.next();
				for (ExcelObjectMapperDO eom : getMapperDOs()) {
					if (eom.getExcelColumnName().equals(
							EasyExcelUtils.getCellStringValue(cell))) {
						if(config.getMapType().getValue() == MapperEnum.horizontal.getValue()){
							eom.setExcelColumnNum(i+1);
						} else if (config.getMapType().getValue() == MapperEnum.vertical.getValue()){
							eom.setExcelColumnNum(i);
						}
						
					}
				}
			}
		} catch (Exception e) {
			clean();
			throw new Exception("init fileItem error.", e);
		}
	}

}
