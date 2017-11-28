package smarthome.security.google.action

class GoogleActionUser {
	String lastSeen
	String userId
	String accessToken
	String locale
	List permissions = []	
	GoogleActionUserProfile profile
}
