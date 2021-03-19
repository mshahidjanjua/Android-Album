package com.example.albumsapp;

import java.io.File;
import java.util.ArrayList;

public class FileData {
	private int fiID;
	private String fsName;
	private String fsThumbNailPath;
	private String fsLocalFilePath;
	private int fiOrientation;
	private int fiImageCount;
	private ArrayList<File> fsFiles;
	
	private boolean fbSelected; //this is used for the image listing
	
	public FileData() {
		super();
		
		this.fsName = "";
		this.fsThumbNailPath = "";
		this.fsLocalFilePath = "";
		this.fiID = 0;
		this.fiOrientation= 0;  
		this.fiImageCount = 0;
		this.fsFiles = null;
		this.fbSelected = false;
	}

	public void setFsName(String fsName) {
		this.fsName = fsName;
	}

	public void setFsThumbNailPath(String fsThumbNailPath) {
		this.fsThumbNailPath = fsThumbNailPath;
	}

	public void setFsLocalFilePath(String fsLocalFilePath) {
		this.fsLocalFilePath = fsLocalFilePath;
	}

	public void setFiID(int fiID) {
		this.fiID = fiID;
	}

	public void setFiImageCount(int fiImageCount) {
		this.fiImageCount = fiImageCount;
	}
	public void setFsFiles(ArrayList<File> fsFiles) {
		this.fsFiles = fsFiles;
	}

	public String getFsName() {
		return fsName;
	}

	public String getFsThumbNailPath() {
		return fsThumbNailPath;
	}

	public String getFsLocalFilePath() {
		return fsLocalFilePath;
	}

	public int getFiID() {
		return fiID;
	}

	public int getFiImageCount() {
		return fiImageCount;
	}
	public ArrayList<File> getFsFiles() {
		return fsFiles;
	}

	public boolean isFbSelected() {
		return fbSelected;
	}

	public void setFbSelected(boolean fbSelected) {
		this.fbSelected = fbSelected;
	}

	public int getFiOrientation() {
		return fiOrientation;
	}

	public void setFiOrientation(int fiOrientation) {
		this.fiOrientation = fiOrientation;
	}
	
}
