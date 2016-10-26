package com.jasonwjones.essterm;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

@Configuration
@ComponentScan(basePackages = {"com.jasonwjones"})
public class EssTermConfig {

	@Bean
	public EssTermSpring getEssTerm() {
		return new EssTermSpring();
	}
	
	@Bean
	public WindowBasedTextGUI getTextGUI() throws IOException {
	    Terminal terminal = new DefaultTerminalFactory().createTerminal();
	    Screen screen = new TerminalScreen(terminal);
	    screen.startScreen();
	    return new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	}
	
}
