package ua.yzcorp.controller;

import ua.yzcorp.view.Console;
import ua.yzcorp.view.Gui;
import ua.yzcorp.view.Message;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Run {
	public static int GUI = 0;
    public static void main(String[] args) {
		ConnectSQL connectSQL = new ConnectSQL();
    	try {
			switch (args[0]) {
				case "console":
					Console.start();
					break;
				case "gui":
					GUI = 1;
					Gui.start();
					break;
				default:
					System.out.println("Usage: java -jar target/swingy.jar [console | gui]");
					break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: java -jar target/swingy.jar [console | gui]");
		} catch (NoSuchElementException | NullPointerException e) {
			Message.goodBye();
		}
	}
}
