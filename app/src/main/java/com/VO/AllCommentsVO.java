package com.VO;

import com.model.BaseRespModel;

import java.io.Serializable;
import java.util.ArrayList;

public class AllCommentsVO extends BaseRespModel implements Serializable {

	private ArrayList<CommentsVO> data;

	public ArrayList<CommentsVO> getData() {
		return data;
	}

	public void setData(ArrayList<CommentsVO> data) {
		this.data = data;
	}
}
