package com.eka.middleware.pub.util.postman;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bearer {

	private String key;
	private String value;
	private String type;

}