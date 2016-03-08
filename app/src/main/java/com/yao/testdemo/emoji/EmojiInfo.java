package com.yao.testdemo.emoji;

import java.io.Serializable;

/**
 * ������Ϣ����
 * 
 * @author Yao
 */
public class EmojiInfo implements Serializable {

	public int emojiId;
	public String emojiValueOne;
	public String emojiValueTwo;
	
	public EmojiInfo(){}
	
	public EmojiInfo(int emojiId,String emojiValueOne,String emojiValueTwo){
		this.emojiId=emojiId;
		this.emojiValueOne=emojiValueOne;
		this.emojiValueTwo=emojiValueTwo;
	}
}
