/**
 * 
 */
package com.mediacollector.collection;

/**
 * Die Datenbank-Klasse, die für die Erstellung, zur Verbindung zur Datenbank
 * und zum sauberen Ausführen von Anfragen zuständig ist.
 * Die Klasse ist als Singleton aufgebaut, es ist also nur eine Verbindung zur 
 * Datenbank möglich.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Database {
	
	private static Database instance = new Database();
	
	public static Database getInstance() {
		return instance;
	}
	
	public Database() {}
	
	public static void connectToDb() {}	
	public static void executeStatement(final String statement) {}	
	public static void executeStatements(final String[] statements) {}
	public static void create() {}
	public static void select() {}
	public static void update() {}
	public static void delete() {}

}
