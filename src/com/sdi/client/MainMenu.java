package com.sdi.client;

import com.sdi.client.actions.MarcarCompletadaAction;
import com.sdi.client.actions.NuevaTareaAction;
import com.sdi.client.actions.VistaTareasHoyAction;
import com.sdi.client.actions.VistaTareasRetrasadasAction;

import alb.util.console.Console;
import alb.util.menu.BaseMenu;

public class MainMenu extends BaseMenu{
	
	public final static String REST_SERVICE_URL = "http://localhost:8280/sdi2-16WEB/rest/UserServicesRest";
	public static String user;
	public static String password;
	
	public MainMenu() {
		menuOptions = new Object[][] {
				{ "Menu de usuario", null },
				{"Ver lista tareas Hoy", VistaTareasHoyAction.class},
				{"Ver lista tareas Retrasadas", VistaTareasRetrasadasAction.class},
				{"Marcar tarea completada", MarcarCompletadaAction.class},
				{"Nueva tarea", NuevaTareaAction.class},
				
		};
	}

	public static void main(String[] args) {
		//user = Console.readString("User");
		//password = Console.readString("Password");
		new MainMenu().execute();
	}
}
