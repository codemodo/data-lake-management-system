package project.components;

public class TempFileData {
	String path;
	String name;
	String contentType;
	
	public TempFileData(String path, String name, String contentType) {
		this.path = path;
		this.name = name;
		this.contentType = contentType;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
