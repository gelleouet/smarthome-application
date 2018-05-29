package smarthome.core

enum MimeTypeEnum {
	PDF("application/pdf", "pdf"),
	WORD2003("application/msword", "doc"),
	EXCEL2003("application/vnd.ms-excel", "xls"),
	WORD2007("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
	EXCEL2007("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
	
	String mimeType
	String extension

	private MimeTypeEnum(String mimeType, String extension) {
		this.mimeType = mimeType
		this.extension = extension
	}
}
