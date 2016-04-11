package smarthome.security

import org.springframework.security.acls.domain.AbstractPermission
import org.springframework.security.acls.model.Permission;

class SmartHomePermission extends AbstractPermission {
	public static final Permission READ = new SmartHomePermission(1 << 0, 'R' as char); // 1
	public static final Permission WRITE = new SmartHomePermission(1 << 1, 'W' as char); // 2
	public static final Permission EXEC = new SmartHomePermission(1 << 2, 'X' as char); // 4
	public static final Permission OWNER = new SmartHomePermission(1 << 3, 'O' as char); // 8

	protected SmartHomePermission(int mask) {
		super(mask)
	}

	protected SmartHomePermission(int mask, char code) {
		super(mask, code)
	}
}	
