package com.superschach.superschach.gui;

import java.io.Serializable;

import javafx.scene.shape.TriangleMesh;

public class MyMesh implements Serializable{
	private static final long serialVersionUID = 0L;
	
	private float[] punkte;
	private int[] faces;
	private float[] textCoords;
	
	public void setPoints(float[] f)
	{
		punkte=f;
	}
	
	public void setFaces(int[] f)
	{
		faces=f;
	}
	
	public void setTextCoords(float[] f)
	{
		textCoords=f;
	}
	
	public float[] getPoints()
	{
		return punkte;
	}
	public int[] getFaces()
	{
		return faces;
	}
	
	public float[] getTextCoords()
	{
		return textCoords;
	}
	
	public TriangleMesh getMesh()
	{
		TriangleMesh ret=new TriangleMesh();
		ret.getPoints().setAll(punkte);
		ret.getFaces().setAll(faces);
		ret.getTexCoords().setAll(textCoords);
		return ret;
	}
}