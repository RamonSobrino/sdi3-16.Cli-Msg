package uo.sdi.client;

import uo.sdi.client.actions.MarcarCompletadaAction;
import uo.sdi.client.actions.NuevaTareaAction;
import uo.sdi.client.actions.VistaTareasHoyAction;
import uo.sdi.client.actions.VistaTareasRetrasadasAction;
import uo.sdi.client.util.MsgConfig;
import alb.util.console.Console;
import alb.util.menu.BaseMenu;

public class MainMenu extends BaseMenu{
	

	
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
		String user = Console.readString("User");
		String password = Console.readString("Password");
		
		MsgConfig mconf = MsgConfig.getInstance();
		mconf.setUser(user);
		mconf.setPassword(password);
		
		new MainMenu().execute();
	}
}
