package com.yao.testdemo.copyelememenu;

public class MenuItemBean {
	private String menuName;
	private String menuTag;
	private int menuSection;
	
	public MenuItemBean(String menuName,String menuTag,int menuSection){
		this.menuName=menuName;
		this.menuTag=menuTag;
		this.menuSection=menuSection;
	}
	public String getMenuName(){
		return this.menuName;
	}
	public String getMenuTag(){
		return this.menuTag;
	}
	public int getMenuSection(){
		return this.menuSection;
	}
	public void setMenuName(String menuName){
		this.menuName=menuName;
	}
	public void setMenuTag(String menuTag){
		this.menuTag=menuTag;
	}
	public void setMenuSection(int menuSection){
		this.menuSection=menuSection;
	}
	
}
