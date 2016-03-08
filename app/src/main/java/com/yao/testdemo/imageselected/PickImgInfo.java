package com.yao.testdemo.imageselected;

import java.io.Serializable;

/**
 * 图片信息实体
 * @author Yao
 */
public class PickImgInfo implements Serializable{

	public String id;
	public String path;
	public String fileName;
	public int size;
	
	public boolean isCheck;

	public PickImgInfo() {
		super();
	}

	public PickImgInfo(String id, String path, String fileName, int size) {
		super();
		this.id = id;
		this.path = path;
		this.fileName = fileName;
		this.size = size;
	}

	@Override
	public String toString() {
		return "PickImgInfo [id=" + id + ", path=" + path + ", fileName="
				+ fileName + ", size=" + size + ", isCheck=" + isCheck + "]";
	}
	
}
