package com.superschach.superschach.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.AbstractGUI;

import javafx.scene.paint.Color;

public class Datenbank
{

	static
	{
		try
		{
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e)
		{
			throw new Error(e);
		}
	}

	private Connection connection;

	public Datenbank()
	{
		try
		{
			connection = DriverManager
					.getConnection(
							"jdbc:hsqldb:file:" + AbstractGUI.verzeichnis()
									+ "superschach.db" + ";shutdown=true",
							"sa", "");
			connection.createStatement().execute(
					"create table gui(feld varchar(32),wert varchar(32))");
			// connection.close();
			connection.setAutoCommit(true);
		} catch (Exception e)
		{
		}
	}

	public boolean speichereZug()
	{
		return true;
	}

	public void speichereEinstellungen(GUI gUI)
	{

		try
		{
			PreparedStatement del = connection
					.prepareStatement("DELETE FROM gui");
			del.execute();
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO gui VALUES(?,?)");
			preparedStatement.setString(1, "hintergrund");
			preparedStatement.setString(2, gUI.getHintergrund());
			preparedStatement.execute();

			preparedStatement.setString(1, "farbe_weiss");
			preparedStatement.setString(2,
					toRGBCode(gUI.getFarbe_weiss().getValue()));
			preparedStatement.execute();

			preparedStatement.setString(1, "farbe_schwarz");
			preparedStatement.setString(2,
					toRGBCode(gUI.getFarbe_schwarz().getValue()));
			preparedStatement.execute();

			preparedStatement.setString(1, "name");
			preparedStatement.setString(2, gUI.getName());
			preparedStatement.execute();

			preparedStatement.setString(1, "sounds");
			preparedStatement.setString(2, "" + gUI.getSounds().getValue());
			preparedStatement.execute();

			preparedStatement.setString(1, "zweid");
			preparedStatement.setString(2, "" + gUI.getZweid().getValue());
			preparedStatement.execute();

			preparedStatement.setString(1, "form");
			preparedStatement.setString(2, gUI.form);
			preparedStatement.execute();

			preparedStatement.setString(1, "farbe_von");
			preparedStatement.setString(2, gUI.getVonFarbe().getValue());
			preparedStatement.execute();

			preparedStatement.setString(1, "farbe_bis");
			preparedStatement.setString(2, gUI.getBisFarbe().getValue());
			preparedStatement.execute();

			preparedStatement.setString(1, "vollbild");
			preparedStatement.setString(2, "" + gUI.getStage().isFullScreen());
			preparedStatement.execute();

			preparedStatement.setString(1, "css");
			preparedStatement.setString(2, "" + gUI.getCss().getValue());
			preparedStatement.execute();
		} catch (Exception e)
		{
			gUI.spiel.meldungAusgeben(
					AbstractGUI.meldung("speichernFehlgeschlagen"));
		}
	}

	public String ladeEinstellungen(String feld) throws Exception
	{
		String ret = "";
		try
		{
			PreparedStatement prep = connection
					.prepareStatement("SELECT wert FROM gui WHERE feld=?");
			prep.setString(1, feld);
			ResultSet r=prep.executeQuery();
			if(r!=null)
			{
				r.next();
				ret=r.getString(1);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception();
		}
		return ret;
	}

	private String toRGBCode(Color color)
	{
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}
}
