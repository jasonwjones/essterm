package com.jasonwjones.essterm;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class Widget {

	public Widget() {
		File curDir = new File("essterm.properties");
		System.out.println(curDir.getAbsolutePath());
	}
	public String toString() {
		return "it's me, the widget";
	}
	
}
